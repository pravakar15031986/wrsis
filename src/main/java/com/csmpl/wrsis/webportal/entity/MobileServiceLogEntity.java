package com.csmpl.wrsis.webportal.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_mobile_service_log")
public class MobileServiceLogEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_mob_ser_log_id")
	private BigInteger intmobserlogid;

	@Column(name = "vch_ip_address")
	private String vchipaddress;

	@Column(name = "vch_device_id")
	private String vchdeviceid;

	@Column(name = "int_user_id")
	private Integer intuserid;

	@Column(name = "vch_service_name")
	private String vchservicename;

	@Column(name = "vch_req_details")
	private String vchreqdetails;

	@Column(name = "dtm_req_time")
	private Date dtmreqtime;

	@Column(name = "vch_res_details")
	private String vchresdetails;

	@Column(name = "dtm_res_time")
	private Date dtmrestime;

	@Column(name = "bit_status")
	private boolean bitstatus;

	public BigInteger getIntmobserlogid() {
		return intmobserlogid;
	}

	public void setIntmobserlogid(BigInteger intmobserlogid) {
		this.intmobserlogid = intmobserlogid;
	}

	public String getVchipaddress() {
		return vchipaddress;
	}

	public void setVchipaddress(String vchipaddress) {
		this.vchipaddress = vchipaddress;
	}

	public String getVchdeviceid() {
		return vchdeviceid;
	}

	public void setVchdeviceid(String vchdeviceid) {
		this.vchdeviceid = vchdeviceid;
	}

	public Integer getIntuserid() {
		return intuserid;
	}

	public void setIntuserid(Integer intuserid) {
		this.intuserid = intuserid;
	}

	public String getVchservicename() {
		return vchservicename;
	}

	public void setVchservicename(String vchservicename) {
		this.vchservicename = vchservicename;
	}

	public String getVchreqdetails() {
		return vchreqdetails;
	}

	public void setVchreqdetails(String vchreqdetails) {
		this.vchreqdetails = vchreqdetails;
	}

	public Date getDtmreqtime() {
		return dtmreqtime;
	}

	public void setDtmreqtime(Date dtmreqtime) {
		this.dtmreqtime = dtmreqtime;
	}

	public String getVchresdetails() {
		return vchresdetails;
	}

	public void setVchresdetails(String vchresdetails) {
		this.vchresdetails = vchresdetails;
	}

	public Date getDtmrestime() {
		return dtmrestime;
	}

	public void setDtmrestime(Date dtmrestime) {
		this.dtmrestime = dtmrestime;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

}
