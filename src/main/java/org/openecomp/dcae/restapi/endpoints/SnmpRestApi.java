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
import org.openecomp.dcae.restapi.model.vendor.snmp.Vsnmp;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelScalingMeasurement;


import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_CPU_USE;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_MEM_USE;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_VNIC_PERFORMANCE;
import org.openecomp.dcae.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.att.nsa.apiServer.endpoints.NsaBaseEndpoint;
import com.att.nsa.drumlin.service.framework.context.DrumlinRequestContext;
import com.att.nsa.drumlin.service.standards.HttpStatusCodes;
import com.att.nsa.drumlin.service.standards.MimeTypes;

public class SnmpRestApi extends NsaBaseEndpoint { 
	
	private static final Logger log = LoggerFactory.getLogger(SnmpRestApi.class);
	
	public static void snmpSingleEvent(DrumlinRequestContext ctx) throws IOException, SAXException, ParseException
	{
		InputStream istr = null;
		String msg = null;
		final UUID uuid = java.util.UUID.randomUUID();
		
		istr = ctx.request().getBodyStream();		
		msg = IOUtils.toString(istr); 	
		log.info("received json "+msg);
		
		log.info("convert json  to object ");
		System.out.println("convert json  to object ");
		
		Vsnmp vsnmp = JSONUtil.convertJSONtoObject(msg, Vsnmp.class);
		
		log.info("transform the pojo to measurment pojo");
		System.out.println("transform the pojo to measurment pojo");
		
		EvelScalingMeasurement evelScalingMeasurement = runSmooksTransform(vsnmp);
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
		System.out.println("exit");
	}
	
	
	
	protected static EvelScalingMeasurement runSmooksTransform(Vsnmp vsnmp) throws IOException, SAXException {
        Smooks smooks = new Smooks("smooks-config-snmp.xml");

        try {
           
        	ExecutionContext executionContext = smooks.createExecutionContext();

            // Transform the source Order to the target LineOrder via a
            // JavaSource and JavaResult instance...
            JavaSource source = new JavaSource(vsnmp);
            JavaResult result = new JavaResult();

            // Configure the execution context to generate a report...
            executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

            smooks.filterSource(executionContext, source, result);
            
            //MEASUREMENT_VNIC_PERFORMANCE MEASUREMENT_VNIC_PERFORMANCE = (MEASUREMENT_VNIC_PERFORMANCE) result.getBean("measurementVnicPerformance");
            ArrayList<MEASUREMENT_CPU_USE> MEASUREMENT_CPU_USE = (ArrayList<MEASUREMENT_CPU_USE>) result.getBean("cupUsages");
            MEASUREMENT_CPU_USE dd = (MEASUREMENT_CPU_USE) result.getBean("cupUsage");
            MEASUREMENT_MEM_USE fff = (MEASUREMENT_MEM_USE) result.getBean("memUsage");
            
            return (EvelScalingMeasurement) result.getBean("evelScalingSnmp");
        } finally {
            smooks.close();
        }
    }
	
	
	public static void setValues(EvelScalingMeasurement evelScalingMeasurement) throws ParseException
	{
		evelScalingMeasurement.evel_last_epoch_set(evelScalingMeasurement.getDate().getTime());
		evelScalingMeasurement.evel_start_epoch_set(evelScalingMeasurement.getDate().getTime());
		
		MEASUREMENT_CPU_USE mesaurementCpuUsagePer = (MEASUREMENT_CPU_USE) evelScalingMeasurement.getCpu_usage().get(0);
		MEASUREMENT_MEM_USE mesaurementmemUsagePer = (MEASUREMENT_MEM_USE) evelScalingMeasurement.getMem_usage().get(0);
		
		mesaurementmemUsagePer.memfree.SetValue(mesaurementmemUsagePer.memfree.getValue());
		mesaurementmemUsagePer.memused.SetValue(mesaurementmemUsagePer.memused.getValue());
		mesaurementmemUsagePer.memconfig.SetValue(mesaurementmemUsagePer.memconfig.getValue());
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
