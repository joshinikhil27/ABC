package org.openecomp.dcae.restapi.model.ves.syslog;

public class MEASUREMENT_MEM_USE {
	  String id;
	  String vmid;
	  double membuffsz;
	  public EvelOptionDouble memcache;
	  public EvelOptionDouble memconfig;
	  public EvelOptionDouble memfree;
	  public EvelOptionDouble slabrecl;
	  public EvelOptionDouble slabunrecl;
	  public EvelOptionDouble memused;
	  
	  
		public MEASUREMENT_MEM_USE() {
			id="";
			membuffsz = 0.0;
			memcache = new EvelOptionDouble();
		    memconfig= new EvelOptionDouble();
		    memfree= new EvelOptionDouble();
		    slabrecl= new EvelOptionDouble();
		    slabunrecl= new EvelOptionDouble();
		    memused= new EvelOptionDouble();
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getVmid() {
			return vmid;
		}
		public void setVmid(String vmid) {
			this.vmid = vmid;
		}
		public double getMembuffsz() {
			return membuffsz;
		}
		public void setMembuffsz(double membuffsz) {
			this.membuffsz = membuffsz;
		}
		public EvelOptionDouble getMemcache() {
			return memcache;
		}
		public void setMemcache(EvelOptionDouble memcache) {
			this.memcache = memcache;
		}
		public EvelOptionDouble getMemconfig() {
			return memconfig;
		}
		public void setMemconfig(EvelOptionDouble memconfig) {
			this.memconfig = memconfig;
		}
		public EvelOptionDouble getMemfree() {
			return memfree;
		}
		public void setMemfree(EvelOptionDouble memfree) {
			this.memfree = memfree;
		}
		public EvelOptionDouble getSlabrecl() {
			return slabrecl;
		}
		public void setSlabrecl(EvelOptionDouble slabrecl) {
			this.slabrecl = slabrecl;
		}
		public EvelOptionDouble getSlabunrecl() {
			return slabunrecl;
		}
		public void setSlabunrecl(EvelOptionDouble slabunrecl) {
			this.slabunrecl = slabunrecl;
		}
		public EvelOptionDouble getMemused() {
			return memused;
		}
		public void setMemused(EvelOptionDouble memused) {
			this.memused = memused;
		}
	  
	  
	}