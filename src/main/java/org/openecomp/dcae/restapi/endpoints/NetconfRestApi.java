package org.openecomp.dcae.restapi.endpoints;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.milyn.Smooks;
import org.milyn.container.ExecutionContext;
import org.milyn.event.report.HtmlReportGenerator;
import org.milyn.payload.JavaResult;
import org.milyn.payload.JavaSource;
import org.openecomp.dcae.commonFunction.CommonStartup;
import org.openecomp.dcae.commonFunction.CustomExceptionLoader;
import org.openecomp.dcae.commonFunction.CommonStartup.QueueFullException;
import org.openecomp.dcae.restapi.model.vendor.netconf.VNetConf;
import org.openecomp.dcae.restapi.model.vendor.syslog.VSyslog;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelFault;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelHeader;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelOptionDouble;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelOptionInt;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelScalingMeasurement;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelSyslog;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_VNIC_PERFORMANCE;
import org.openecomp.dcae.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.att.nsa.apiServer.endpoints.NsaBaseEndpoint;
import com.att.nsa.drumlin.service.framework.context.DrumlinRequestContext;
import com.att.nsa.drumlin.service.standards.HttpStatusCodes;
import com.att.nsa.drumlin.service.standards.MimeTypes;

public class NetconfRestApi extends NsaBaseEndpoint { 
	
	private static final Logger log = LoggerFactory.getLogger(NetconfRestApi.class);
	
	public static void netConfSingleEvent(DrumlinRequestContext ctx) throws IOException, SAXException, ParseException {
	
		InputStream istr = null;
		String msg = null;
		final UUID uuid = java.util.UUID.randomUUID();
		
		istr = ctx.request().getBodyStream();		
		msg = IOUtils.toString(istr); 	
		log.info("received json "+msg);
		
		log.info("convert json  to object ");
		System.out.println("convert json  to object ");
		
		VNetConf vnetConf = JSONUtil.convertJSONtoObject(msg, VNetConf.class);
		
		log.info("transform the pojo to measurment pojo");
		System.out.println("transform the pojo to measurment pojo");
		
		EvelScalingMeasurement evelScalingMeasurement = runSmooksTransform(vnetConf);
		setValues(evelScalingMeasurement);
		
		log.info("convert the measurment pojo to json");
		System.out.println("convert the measurment pojo to json");
		
		String vesFormatScalingMeasurementJson = evelScalingMeasurement.evel_json_encode_event();
		JSONObject jsonObject = new JSONObject(vesFormatScalingMeasurementJson);
		
		log.info("converted ves json"+vesFormatScalingMeasurementJson);
		
		System.out.println("converted ves json"+vesFormatScalingMeasurementJson);
		
		
		
		
		jsonObject.put("VESuniqueId", uuid);
		final JSONArray jsonArray = new JSONArray().put(jsonObject);

		try {
			
			CommonStartup.handleEvents(jsonArray);
			System.out.println("exit the method");
			
		}  catch (JSONException | NullPointerException | IOException e) {
			e.printStackTrace();
			log.error("Couldn't parse JSON Array - HttpStatusCodes.k400_badRequest" + HttpStatusCodes.k400_badRequest
					+ " Message:" + e.getMessage() + istr.toString() + msg);
			CommonStartup.eplog.info("EVENT_RECEIPT_FAILURE: Invalid user request" + e.toString() + msg) ;
		
			respondWithCustomMsginJson(ctx, HttpStatusCodes.k400_badRequest, "Couldn't parse JSON object");
			return;
		} catch (QueueFullException e) {
			e.printStackTrace();
			log.error("Collector internal queue full  :" + e.getMessage() + msg);
			CommonStartup.eplog.info("EVENT_RECEIPT_FAILURE: QueueFull" + e.toString() + msg);
			respondWithCustomMsginJson(ctx, HttpStatusCodes.k503_serviceUnavailable, "Queue full");
			return;
		} finally {
			if (istr != null) {
				safeClose(istr);
			}
		}
	}
	
	protected static EvelScalingMeasurement runSmooksTransform(VNetConf vnetConf) throws IOException, SAXException {
        Smooks smooks = new Smooks("smooks-config-netconf.xml");

        try {
            ExecutionContext executionContext = smooks.createExecutionContext();

            // Transform the source Order to the target LineOrder via a
            // JavaSource and JavaResult instance...
            JavaSource source = new JavaSource(vnetConf);
            JavaResult result = new JavaResult();

            // Configure the execution context to generate a report...
            executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

            smooks.filterSource(executionContext, source, result);
            
           /* MEASUREMENT_VNIC_PERFORMANCE MEASUREMENT_VNIC_PERFORMANCE = (MEASUREMENT_VNIC_PERFORMANCE) result.getBean("measurementVnicPerformance");
            ArrayList<MEASUREMENT_VNIC_PERFORMANCE> MEASUREMENT_VNIC_PERFORMANCEs = (ArrayList<MEASUREMENT_VNIC_PERFORMANCE>) result.getBean("measurementVnicPerformances");
            EvelOptionDouble dd = (EvelOptionDouble) result.getBean("totalPackets");
            EvelOptionDouble fff = (EvelOptionDouble) result.getBean("totalBytes");*/
            
            return (EvelScalingMeasurement) result.getBean("evelScaling");
        } finally {
            smooks.close();
        }
    }
	
	
	public static void setValues(EvelScalingMeasurement evelScalingMeasurement) throws ParseException
	{
		String dates[] = evelScalingMeasurement.getTimeWindow().split(" - ");
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date startDate=format.parse(dates[0]);
		Date endDate=format.parse(dates[1]);
		
		//MEASUREMENT_VNIC_PERFORMANCE perf = evelScalingMeasurement.evel_measurement_new_vnic_performance("ETH0", "true");
		evelScalingMeasurement.evel_last_epoch_set(endDate.getTime());
		evelScalingMeasurement.evel_start_epoch_set(startDate.getTime());
		
		MEASUREMENT_VNIC_PERFORMANCE mesaurementVnicPer = (MEASUREMENT_VNIC_PERFORMANCE) evelScalingMeasurement.getVnic_usage().get(0);
		//evelScalingMeasurement.evel_vnic_performance_rx_octets_acc_set(perf, mesaurementVnicPer.recvd_octets_acc.getValue());
		//evelScalingMeasurement.evel_vnic_performance_rx_total_pkt_acc_set(perf, mesaurementVnicPer.recvd_total_packets_acc.getValue());	
		mesaurementVnicPer.vnic_id = "ETH0";
		mesaurementVnicPer.valuesaresuspect = "true";
		mesaurementVnicPer.recvd_total_packets_acc.SetValue(mesaurementVnicPer.recvd_total_packets_acc.GetValue());
		mesaurementVnicPer.recvd_octets_acc.SetValue(mesaurementVnicPer.recvd_octets_acc.GetValue());
		//evelScalingMeasurement.evel_meas_vnic_performance_add(perf);	
	}
	
	public static void respondWithCustomMsginJson(DrumlinRequestContext ctx, int sc, String msg) {
		String[] str = null;
		String ExceptionType = "GeneralException";

		str = CustomExceptionLoader.LookupMap(String.valueOf(sc), msg);
		System.out.println("Post CustomExceptionLoader.LookupMap" + str);

		if (str != null) {

			if (str[0].matches("SVC")) {
				ExceptionType = "ServiceException";
			} else if (str[1].matches("POL")) {
				ExceptionType = "PolicyException";
			}

			JSONObject jb = new JSONObject().put("requestError",
					new JSONObject().put(ExceptionType, new JSONObject().put("MessagID", str[0]).put("text", str[1])));

			log.debug("Constructed json error : " + jb.toString());
			ctx.response().sendErrorAndBody(sc, jb.toString(), MimeTypes.kAppJson);
		} else {
			JSONObject jb = new JSONObject().put("requestError",
					new JSONObject().put(ExceptionType, new JSONObject().put("Status", sc).put("Error", msg)));
			ctx.response().sendErrorAndBody(sc, jb.toString(), MimeTypes.kAppJson);
		}

	}
	

	public static void safeClose(FileReader fr) {
		if (fr != null) {
			try {
				fr.close();
			} catch (IOException e) {
				log.error("Error closing file reader stream : " + e.toString());
			}
		}

	}

	public static void safeClose(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				log.error("Error closing Input stream : " + e.toString());
			}
		}

	}
}
