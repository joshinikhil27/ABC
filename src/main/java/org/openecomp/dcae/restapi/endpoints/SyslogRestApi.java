package org.openecomp.dcae.restapi.endpoints;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.openecomp.dcae.restapi.model.vendor.syslog.VSyslog;

import org.openecomp.dcae.restapi.model.ves.syslog.EvelSyslog;
import org.openecomp.dcae.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.att.nsa.apiServer.endpoints.NsaBaseEndpoint;
import com.att.nsa.drumlin.service.framework.context.DrumlinRequestContext;
import com.att.nsa.drumlin.service.standards.HttpStatusCodes;
import com.att.nsa.drumlin.service.standards.MimeTypes;

import org.openecomp.dcae.restapi.model.ves.syslog.EvelFault;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelHeader;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelOptionInt;

public class SyslogRestApi extends NsaBaseEndpoint {

	private static final Logger log = LoggerFactory.getLogger(SyslogRestApi.class);

	public static void syslogSingleEvent(DrumlinRequestContext ctx) throws IOException, SAXException {
		
		InputStream istr = null;
		String msg = null;
		final UUID uuid = java.util.UUID.randomUUID();
		
		istr = ctx.request().getBodyStream();		
		msg = IOUtils.toString(istr); 	
		log.info("received json "+msg);
		
		log.info("convert json  to object ");
		System.out.println("convert json  to object ");
		
		VSyslog vsyslog = JSONUtil.convertJSONtoObject(msg, VSyslog.class);
		
		log.info("transform the pojo to syslog pojo");
		System.out.println("transform the pojo to syslog pojo");
		
		EvelSyslog syEvelSyslog = runSmooksTransform(vsyslog);
		
		
		log.info("set the value which is not comming");
		System.out.println("set the value which is not comming");
		syEvelSyslog.setValuesForOptionalField();
		setValues(syEvelSyslog);
		
		log.info("convert the syslog pojo to json");
		System.out.println("convert the syslog pojo to json");
		
		String vesFormatSyslogJson = syEvelSyslog.evel_json_encode_event();
		JSONObject jsonObject = new JSONObject(vesFormatSyslogJson);
		
		log.info("converted ves json"+vesFormatSyslogJson);
		
		System.out.println("converted ves json"+vesFormatSyslogJson);
		System.out.println("exit the method");
		
		
		
		jsonObject.put("VESuniqueId", uuid);
		final JSONArray jsonArray = new JSONArray().put(jsonObject);

		try {
			
			CommonStartup.handleEvents(jsonArray);
			
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
	
	
	protected static EvelSyslog runSmooksTransform(VSyslog vsyslog) throws IOException, SAXException {
        Smooks smooks = new Smooks("smooks-config.xml");

        try {
            ExecutionContext executionContext = smooks.createExecutionContext();

            // Transform the source Order to the target LineOrder via a
            // JavaSource and JavaResult instance...
            JavaSource source = new JavaSource(vsyslog);
            JavaResult result = new JavaResult();

            // Configure the execution context to generate a report...
            executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

            smooks.filterSource(executionContext, source, result);

            return (EvelSyslog) result.getBean("evelsyslog");
        } finally {
            smooks.close();
        }
    }
	
	
	public static void setValues(EvelSyslog syEvelSyslog)
	{
		syEvelSyslog.evel_last_epoch_set(syEvelSyslog.getSysDate().getTime());
		syEvelSyslog.evel_start_epoch_set(syEvelSyslog.getSysDate().getTime());
		//EvelFault.evel_source_type(EvelFault.EVEL_SOURCE_TYPES.EVEL_SOURCE_VIRTUAL_NETWORK_FUNCTION);
		syEvelSyslog.setEvent_source_type(EvelFault.EVEL_SOURCE_TYPES.EVEL_SOURCE_VIRTUAL_NETWORK_FUNCTION);
		syEvelSyslog.setSyslog_tag("NILVALUE");
		syEvelSyslog.setEvent_domain(EvelHeader.DOMAINS.EVEL_DOMAIN_SYSLOG);
		//  fault_source_type = evel_source_type(event_source_type);
		syEvelSyslog.evel_syslog_facility_set(syEvelSyslog.getFacility());
		//EvelOptionInt syslogFacility = syEvelSyslog.getSyslog_facility();
		//syslogFacility.SetValue(syslogFacility.getValue());
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
