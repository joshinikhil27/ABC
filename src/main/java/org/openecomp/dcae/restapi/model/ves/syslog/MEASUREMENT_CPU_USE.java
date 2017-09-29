package org.openecomp.dcae.restapi.model.ves.syslog;

/**************************************************************************//**
 * CPU Usage.
 * JSON equivalent field: cpuUsage
 *****************************************************************************/
public class MEASUREMENT_CPU_USE {
  String id;
  double usage;
  public EvelOptionDouble idle;
  public EvelOptionDouble intrpt;
  public EvelOptionDouble nice;
  public EvelOptionDouble softirq;
  public EvelOptionDouble steal;
  public EvelOptionDouble sys;
  public EvelOptionDouble user;
  public EvelOptionDouble wait;
  
  
  
public MEASUREMENT_CPU_USE() {
	
	idle = new EvelOptionDouble();
    intrpt = new EvelOptionDouble();
    nice = new EvelOptionDouble();
    softirq = new EvelOptionDouble();
    steal = new EvelOptionDouble();
    sys = new EvelOptionDouble();
    user = new EvelOptionDouble();
    wait = new EvelOptionDouble();
}

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public double getUsage() {
	return usage;
}
public void setUsage(double usage) {
	this.usage = usage;
}
public EvelOptionDouble getIdle() {
	return idle;
}
public void setIdle(EvelOptionDouble idle) {
	this.idle = idle;
}
public EvelOptionDouble getIntrpt() {
	return intrpt;
}
public void setIntrpt(EvelOptionDouble intrpt) {
	this.intrpt = intrpt;
}
public EvelOptionDouble getNice() {
	return nice;
}
public void setNice(EvelOptionDouble nice) {
	this.nice = nice;
}
public EvelOptionDouble getSoftirq() {
	return softirq;
}
public void setSoftirq(EvelOptionDouble softirq) {
	this.softirq = softirq;
}
public EvelOptionDouble getSteal() {
	return steal;
}
public void setSteal(EvelOptionDouble steal) {
	this.steal = steal;
}
public EvelOptionDouble getSys() {
	return sys;
}
public void setSys(EvelOptionDouble sys) {
	this.sys = sys;
}
public EvelOptionDouble getUser() {
	return user;
}
public void setUser(EvelOptionDouble user) {
	this.user = user;
}
public EvelOptionDouble getWait() {
	return wait;
}
public void setWait(EvelOptionDouble wait) {
	this.wait = wait;
}
  
  
}
