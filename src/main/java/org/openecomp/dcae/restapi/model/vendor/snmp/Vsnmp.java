package org.openecomp.dcae.restapi.model.vendor.snmp;

import java.util.ArrayList;
import java.util.List;

import org.openecomp.dcae.restapi.model.vendor.parent.Vendor;

public class Vsnmp extends Vendor {

	public String vendorName;
	public String protocol;
	
	public String cpuIdentifier;
	public Integer percentUsage;
	public String  vmIdentifier;
	public Double memoryConfigured;
	public Double memoryUsed;
	public Double memoryFree;
	public String date;
	
	//fault events field
	
	public String eventSeverityMem;
	public String eventSeveritycpu;
	public String eventSourceType;
	public String eventCategory;
	public String alarmConditionMemory;
	public String alarmConditionCPU;
	public String specificProblemMemory;
	public String specificProblemCPU;
	public String vfStatus;

	public String memoryConfigId;
	public String memoryFreeId;
	public String cpuInfoId;
	public String cupIdleTime;
	
	public String getCpuIdentifier() {
		return cpuIdentifier;
	}
	public void setCpuIdentifier(String cpuIdentifier) {
		this.cpuIdentifier = cpuIdentifier;
	}
	public Integer getPercentUsage() {
		return percentUsage;
	}
	public void setPercentUsage(Integer percentUsage) {
		this.percentUsage = percentUsage;
	}
	public Double getMemoryConfigured() {
		return memoryConfigured;
	}
	public void setMemoryConfigured(Double memoryConfigured) {
		this.memoryConfigured = memoryConfigured;
	}
	public Double getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(Double memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public Double getMemoryFree() {
		return memoryFree;
	}
	public void setMemoryFree(Double memoryFree) {
		this.memoryFree = memoryFree;
	}
	public String getVmIdentifier() {
		return vmIdentifier;
	}
	public void setVmIdentifier(String vmIdentifier) {
		this.vmIdentifier = vmIdentifier;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	
	//fault's setter and getter
	
	
	
	
	public String getEventSeverityMem() {
		return eventSeverityMem;
	}
	public void setEventSeverityMem(String eventSeverityMem) {
		this.eventSeverityMem = eventSeverityMem;
	}
	public String getEventSeveritycpu() {
		return eventSeveritycpu;
	}
	public void setEventSeveritycpu(String eventSeveritycpu) {
		this.eventSeveritycpu = eventSeveritycpu;
	}
	
	public String getEventSourceType() {
		return eventSourceType;
	}
	public void setEventSourceType(String eventSourceType) {
		this.eventSourceType = eventSourceType;
	}
	
	
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	
	
	public String getAlarmConditionMemory() {
		return alarmConditionMemory;
	}
	public void setAlarmConditionMemory(String alarmConditionMemory) {
		this.alarmConditionMemory = alarmConditionMemory;
	}
	public String getAlarmConditionCPU() {
		return alarmConditionCPU;
	}
	public void setAlarmConditionCPU(String alarmConditionCPU) {
		this.alarmConditionCPU = alarmConditionCPU;
	}
	
	
	
	public String getSpecificProblemMemory() {
		return specificProblemMemory;
	}
	public void setSpecificProblemMemory(String specificProblemMemory) {
		this.specificProblemMemory = specificProblemMemory;
	}
	public String getSpecificProblemCPU() {
		return specificProblemCPU;
	}
	public void setSpecificProblemCPU(String specificProblemCPU) {
		this.specificProblemCPU = specificProblemCPU;
	}
	public String getVfStatus() {
		return vfStatus;
	}
	public void setVfStatus(String vfStatus) {
		this.vfStatus = vfStatus;
	}
	public String getMemoryConfigId() {
		return memoryConfigId;
	}
	public void setMemoryConfigId(String memoryConfigId) {
		this.memoryConfigId = memoryConfigId;
	}
	public String getMemoryFreeId() {
		return memoryFreeId;
	}
	public void setMemoryFreeId(String memoryFreeId) {
		this.memoryFreeId = memoryFreeId;
	}
	public String getCpuInfoId() {
		return cpuInfoId;
	}
	public void setCpuInfoId(String cpuInfoId) {
		this.cpuInfoId = cpuInfoId;
	}
	public String getCupIdleTime() {
		return cupIdleTime;
	}
	public void setCupIdleTime(String cupIdleTime) {
		this.cupIdleTime = cupIdleTime;
	}
	
	
	
	
	
	
	
}
