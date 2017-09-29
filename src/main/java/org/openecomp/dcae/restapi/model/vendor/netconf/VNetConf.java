package org.openecomp.dcae.restapi.model.vendor.netconf;

import java.util.ArrayList;

import org.openecomp.dcae.restapi.model.vendor.parent.Vendor;

public class VNetConf extends Vendor{

	public Double totalPackets;
	
	public Double totalBytes;
	
	public String timeWindow;

	public ArrayList<String> optinalField;
	
	public Double getTotalPackets() {
		return totalPackets;
	}

	public void setTotalPackets(Double totalPackets) {
		this.totalPackets = totalPackets;
	}

	public Double getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(Double totalBytes) {
		this.totalBytes = totalBytes;
	}

	public String getTimeWindow() {
		return timeWindow;
	}

	public void setTimeWindow(String timeWindow) {
		this.timeWindow = timeWindow;
	}

	public ArrayList<String> getOptinalField() {
		return optinalField;
	}

	public void setOptinalField(ArrayList<String> optinalField) {
		this.optinalField = optinalField;
	}
	
	
	
}
