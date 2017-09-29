package org.openecomp.dcae.restapi.endpoints;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.openecomp.dcae.commonFunction.DmaapPropertyReader;
import org.openecomp.dcae.commonFunction.CommonStartup.QueueFullException;
import org.openecomp.dcae.restapi.model.vendor.netconf.VNetConf;
import org.openecomp.dcae.restapi.model.vendor.parent.Vendor;
import org.openecomp.dcae.restapi.model.vendor.snmp.Vsnmp;
import org.openecomp.dcae.restapi.model.vendor.syslog.VSyslog;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelFault;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelHeader;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelScalingMeasurement;
import org.openecomp.dcae.restapi.model.ves.syslog.EvelSyslog;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_CPU_USE;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_MEM_USE;
import org.openecomp.dcae.restapi.model.ves.syslog.MEASUREMENT_VNIC_PERFORMANCE;
import org.openecomp.dcae.utils.JSONUtil;
import org.openecomp.dcae.utils.ProtocolBeanMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.att.nsa.apiServer.endpoints.NsaBaseEndpoint;
import com.att.nsa.drumlin.service.framework.context.DrumlinRequestContext;
import com.att.nsa.drumlin.service.standards.HttpStatusCodes;
import com.att.nsa.drumlin.service.standards.MimeTypes;

public class GenericAdaptorNew extends NsaBaseEndpoint  {
	
	private static final Logger log = LoggerFactory.getLogger(GenericAdaptor.class);
	
	public static void singleEvent(DrumlinRequestContext ctx) throws IOException, SAXException, ParseException{
		
		InputStream istr = null;
		String msg = null;
		final UUID uuid = java.util.UUID.randomUUID();
		List<String> protocol = ctx.request().getHeader("protocol");
		
		istr = ctx.request().getBodyStream();		
		msg = IOUtils.toString(istr); 	
		log.info("received json "+msg);
		
		log.info("convert json  to object ");
		System.out.println("convert json  to object ");
		Vendor vendorObj = null;
		ArrayList<String> beanName = ProtocolBeanMapping.protocolBean.get(protocol.get(0));
		if(protocol.get(0).equalsIgnoreCase("snmp"))
		{
			vendorObj = JSONUtil.convertJSONtoObject(msg, Vsnmp.class);
		}
		else if(protocol.get(0).equalsIgnoreCase("syslog"))
		{
			vendorObj = JSONUtil.convertJSONtoObject(msg, VSyslog.class);
		}
		else if(protocol.get(0).equalsIgnoreCase("netconf"))
		{
			vendorObj = JSONUtil.convertJSONtoObject(msg, VNetConf.class);
		}
		
		
		String smookFile = "smooks-config-"+vendorObj.getVendorName()+"-"+vendorObj.getProtocol()+".xml";
		
		EvelHeader evelHeader = runSmooksTransformGeneric(beanName.get(0),vendorObj,smookFile);
		EvelHeader evelfaultcpu = null;
		EvelHeader evelfaultMem= null; 
		final JSONArray jsonArray = new JSONArray();
		if(protocol.get(0).equalsIgnoreCase("snmp"))
		{
			evelfaultMem = runSmooksTransformGeneric(beanName.get(1),vendorObj,smookFile);
			evelfaultcpu = runSmooksTransformGeneric(beanName.get(2),vendorObj,smookFile);
			
			setValueForMemoryFault((EvelFault)evelfaultMem);
			setValueForCpuFault((EvelFault)evelfaultcpu);
			
			String vesFormatScalingFaultMemJson = evelfaultMem.evel_json_encode_event();
			String vesFormatScalingFaultCPUJson = evelfaultcpu.evel_json_encode_event();
			
			JSONObject jsonObjectMem = new JSONObject(vesFormatScalingFaultMemJson);
			JSONObject jsonObjectCPU = new JSONObject(vesFormatScalingFaultCPUJson);
			
			jsonObjectMem.put("VESuniqueId", uuid);
			jsonObjectCPU.put("VESuniqueId", uuid);
			
			jsonArray.put(jsonObjectMem);
			jsonArray.put(jsonObjectCPU);
			
			// set value in measurement
			setValuessnmp( (EvelScalingMeasurement) evelHeader );
		}
		else if(protocol.get(0).equalsIgnoreCase("syslog"))
		{
			setValuesSyslog( (EvelSyslog) evelHeader );
		}
		else if(protocol.get(0).equalsIgnoreCase("netconf"))
		{
			setValuesNetConf( (EvelScalingMeasurement) evelHeader);
		}
		
		
		
		log.info("convert the measurment pojo to json");
		System.out.println("convert the measurment pojo to json");
		
		String vesFormatScalingMeasurementJson = evelHeader.evel_json_encode_event();
		JSONObject jsonObject = new JSONObject(vesFormatScalingMeasurementJson);
		
		log.info("converted ves json"+vesFormatScalingMeasurementJson);
		
		System.out.println("converted ves json"+vesFormatScalingMeasurementJson);
		
		
		
		
		jsonObject.put("VESuniqueId", uuid);
		jsonArray.put(jsonObject);

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
	
	/**
	 * 
	 * @param vsnmp
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	protected static EvelHeader runSmooksTransformGeneric(String beanName ,Object sourceObject , String smookFile) throws IOException, SAXException {
		//"D:/MM00498018/ATT/project/newopan/dcae/collectors/ves/";
		String path = CommonStartup.smookFilePath;
		String fileLocation = path + smookFile;
		log.info("fileLocation = "+fileLocation);
		Smooks smooks = new Smooks(fileLocation);

        try {
           
        	ExecutionContext executionContext = smooks.createExecutionContext();

            // Transform the source Order to the target LineOrder via a
            // JavaSource and JavaResult instance...
            JavaSource source = new JavaSource(sourceObject);
            JavaResult result = new JavaResult();

            // Configure the execution context to generate a report...
            executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

            smooks.filterSource(executionContext, source, result);
            
            //MEASUREMENT_VNIC_PERFORMANCE MEASUREMENT_VNIC_PERFORMANCE = (MEASUREMENT_VNIC_PERFORMANCE) result.getBean("measurementVnicPerformance");
          
            //EvelFault evelFault = (EvelFault) result.getBean("evelFaultCPU");
            //EvelFault evelFaultMem = (EvelFault) result.getBean("evelFaultMem");
            return (EvelHeader) result.getBean(beanName);
        } finally {
            smooks.close();
        }
        
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
	
	
	public static void setValuessnmp(EvelScalingMeasurement evelScalingMeasurement) throws ParseException
	{
		evelScalingMeasurement.evel_last_epoch_set(evelScalingMeasurement.getDate().getTime());
		evelScalingMeasurement.evel_start_epoch_set(evelScalingMeasurement.getDate().getTime());
		
		MEASUREMENT_CPU_USE mesaurementCpuUsagePer = (MEASUREMENT_CPU_USE) evelScalingMeasurement.getCpu_usage().get(0);
		MEASUREMENT_MEM_USE mesaurementmemUsagePer = (MEASUREMENT_MEM_USE) evelScalingMeasurement.getMem_usage().get(0);
		
		mesaurementmemUsagePer.memfree.SetValue(mesaurementmemUsagePer.memfree.getValue());
		mesaurementmemUsagePer.memused.SetValue(mesaurementmemUsagePer.memused.getValue());
		mesaurementmemUsagePer.memconfig.SetValue(mesaurementmemUsagePer.memconfig.getValue());
	}
	
	public static void setValueForMemoryFault(EvelFault evelFault)
	{
		
		evelFault.evel_fault_addl_info_add(evelFault.memoryConfigId, evelFault.getMemoryConfigured());
		evelFault.evel_fault_addl_info_add(evelFault.memoryFreeId, evelFault.getMemoryFree());
		
	}
	
	
	public static void setValueForCpuFault(EvelFault evelFault)
	{
		evelFault.evel_fault_addl_info_add(evelFault.cpuInfoId, evelFault.cupIdleTime);
	
		
	}
	
	public static void setValuesSyslog(EvelSyslog syEvelSyslog)
	{
		syEvelSyslog.setValuesForOptionalField();
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
	
	public static void setValuesNetConf(EvelScalingMeasurement evelScalingMeasurement) throws ParseException
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
