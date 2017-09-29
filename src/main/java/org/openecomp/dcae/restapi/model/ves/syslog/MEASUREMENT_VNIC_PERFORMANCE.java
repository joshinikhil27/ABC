package org.openecomp.dcae.restapi.model.ves.syslog;

public class MEASUREMENT_VNIC_PERFORMANCE {
	public String vnic_id;
    public String valuesaresuspect;
    
    public MEASUREMENT_VNIC_PERFORMANCE() {
	
    	
    	recvd_bcast_packets_acc= new EvelOptionDouble();
	    recvd_bcast_packets_delta= new EvelOptionDouble();

	    recvd_discarded_packets_acc= new EvelOptionDouble();
	    recvd_discarded_packets_delta= new EvelOptionDouble();
	    recvd_error_packets_acc= new EvelOptionDouble();
	    recvd_error_packets_delta= new EvelOptionDouble();
	    recvd_mcast_packets_acc= new EvelOptionDouble();
	    recvd_mcast_packets_delta= new EvelOptionDouble();
	    recvd_octets_acc= new EvelOptionDouble();
	    recvd_octets_delta= new EvelOptionDouble();
	    recvd_total_packets_acc= new EvelOptionDouble();
	    recvd_total_packets_delta= new EvelOptionDouble();
	    recvd_ucast_packets_acc= new EvelOptionDouble();
	    recvd_ucast_packets_delta= new EvelOptionDouble();
	    tx_bcast_packets_acc= new EvelOptionDouble();
	    tx_bcast_packets_delta= new EvelOptionDouble();
	    tx_discarded_packets_acc= new EvelOptionDouble();
	    tx_discarded_packets_delta= new EvelOptionDouble();
	    tx_error_packets_acc= new EvelOptionDouble();
	    tx_error_packets_delta= new EvelOptionDouble();
	    tx_mcast_packets_acc= new EvelOptionDouble();
	    tx_mcast_packets_delta= new EvelOptionDouble();
	    tx_octets_acc= new EvelOptionDouble();
	    tx_octets_delta= new EvelOptionDouble();
	    tx_total_packets_acc= new EvelOptionDouble();
	    tx_total_packets_delta= new EvelOptionDouble();
	    tx_ucast_packets_acc= new EvelOptionDouble();
	    tx_ucast_packets_delta= new EvelOptionDouble();
	   }
	  /***************************************************************************/
	  /* Optional fields                                                         */
	  /***************************************************************************/
	  /*Cumulative count of broadcast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_bcast_packets_acc;
	  /*Count of broadcast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_bcast_packets_delta;
	  /*Cumulative count of discarded packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_discarded_packets_acc;
	  /*Count of discarded packets received within the measurement interval*/
	  public EvelOptionDouble recvd_discarded_packets_delta;
	  /*Cumulative count of error packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_error_packets_acc;
	  /*Count of error packets received within the measurement interval*/
	  public EvelOptionDouble recvd_error_packets_delta;
	  /*Cumulative count of multicast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_mcast_packets_acc;
	  /*Count of mcast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_mcast_packets_delta;
	  /*Cumulative count of octets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_octets_acc;
	  /*Count of octets received within the measurement interval*/
	  public EvelOptionDouble recvd_octets_delta;
	  /*Cumulative count of all packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_total_packets_acc;
	  /*Count of all packets received within the measurement interval*/
	  public EvelOptionDouble recvd_total_packets_delta;
	  /*Cumulative count of unicast packets received as read at the end of
	   the measurement interval*/
	  public EvelOptionDouble recvd_ucast_packets_acc;
	  /*Count of unicast packets received within the measurement interval*/
	  public EvelOptionDouble recvd_ucast_packets_delta;
	  /*Cumulative count of transmitted broadcast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_bcast_packets_acc;
	  /*Count of transmitted broadcast packets within the measurement interval*/
	  public EvelOptionDouble tx_bcast_packets_delta;
	  /*Cumulative count of transmit discarded packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_discarded_packets_acc;
	  /*Count of transmit discarded packets within the measurement interval*/
	  public EvelOptionDouble tx_discarded_packets_delta;
	  /*Cumulative count of transmit error packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_error_packets_acc;
	  /*Count of transmit error packets within the measurement interval*/
	  public EvelOptionDouble tx_error_packets_delta;
	  /*Cumulative count of transmit multicast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_mcast_packets_acc;
	  /*Count of transmit multicast packets within the measurement interval*/
	  public EvelOptionDouble tx_mcast_packets_delta;
	  /*Cumulative count of transmit octets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_octets_acc;
	  /*Count of transmit octets received within the measurement interval*/
	  public EvelOptionDouble tx_octets_delta;
	  /*Cumulative count of all transmit packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_total_packets_acc;
	  /*Count of transmit packets within the measurement interval*/
	  public EvelOptionDouble tx_total_packets_delta;
	  /*Cumulative count of all transmit unicast packets at the end of
	   the measurement interval*/
	  public EvelOptionDouble tx_ucast_packets_acc;
	  /*Count of transmit unicast packets within the measurement interval*/
	  public EvelOptionDouble tx_ucast_packets_delta;
	  
	
	public EvelOptionDouble getRecvd_total_packets_acc() {
		return recvd_total_packets_acc;
	}
	public void setRecvd_total_packets_acc(EvelOptionDouble recvd_total_packets_acc) {
		this.recvd_total_packets_acc = recvd_total_packets_acc;
	}
	public EvelOptionDouble getRecvd_octets_acc() {
		return recvd_octets_acc;
	}
	public void setRecvd_octets_acc(EvelOptionDouble recvd_octets_acc) {
		this.recvd_octets_acc = recvd_octets_acc;
	}
}
