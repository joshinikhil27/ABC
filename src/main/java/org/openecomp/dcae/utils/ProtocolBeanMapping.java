package org.openecomp.dcae.utils;

import java.util.ArrayList;
import java.util.HashMap;


public class ProtocolBeanMapping {
	public static HashMap<String,ArrayList<String>> protocolBean = new HashMap<String,ArrayList<String>>();
	
	static {
		
		ArrayList<String> snmpBeanName = new ArrayList<String>();
		snmpBeanName.add("evelScalingSnmp");
		snmpBeanName.add("evelFaultMem");
		snmpBeanName.add("evelFaultCPU");
		protocolBean.put("snmp",snmpBeanName);
	
		
		
		ArrayList<String> syslogBeanName = new ArrayList<String>();
		syslogBeanName.add("evelsyslog");
		protocolBean.put("syslog", syslogBeanName);
		
		ArrayList<String> netconfBeanName = new ArrayList<String>();
		netconfBeanName.add("evelScaling");
		protocolBean.put("netconf", netconfBeanName);
	}
	
}
