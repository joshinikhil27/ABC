package org.openecomp.dcae.restapi.model.vendor.syslog;

import org.openecomp.dcae.restapi.model.vendor.parent.Vendor;

//syslog format
//Sep  7 12:25:27 192.168.2.1 bgpd[2148]: 192.168.2.3 rcvd 200.20.0.0/24
public class VSyslog extends Vendor{
	
	public String sysdate;
	
	public String sourceIP;
	
	public String syslogFacility;
	
	public String message;
	
	public String processName;
	
	public String getSysdate() {
		return sysdate;
	}
	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}
	public String getSyslogFacility() {
		return syslogFacility;
	}
	public void setSyslogFacility(String syslogFacility) {
		this.syslogFacility = syslogFacility;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
	
	

}
