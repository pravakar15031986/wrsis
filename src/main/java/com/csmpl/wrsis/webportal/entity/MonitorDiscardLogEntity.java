package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_monitor_discard_log")
public class MonitorDiscardLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_mon_dis_log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer monitordisid;

	@Column(name = "int_monitor_id")
	private Integer monitorid;

	@Column(name = "vch_monitor_no")
	private String monitorno;

	@Column(name = "int_recom_id")
	private Integer recomid;

	@Column(name = "vch_recom_no")
	private String recomno;

	@Column(name = "vch_reject_remarks")
	private String rejectRemarks;

	@Column(name = "int_discarded_by")
	private Integer discardBy;

	@Column(name = "dtm_discarded_on")
	private Timestamp discardOn;

	@Column(name = "bit_status")
	private Boolean bitStatus;

	public Integer getMonitordisid() {
		return monitordisid;
	}

	public void setMonitordisid(Integer monitordisid) {
		this.monitordisid = monitordisid;
	}

	public Integer getMonitorid() {
		return monitorid;
	}

	public void setMonitorid(Integer monitorid) {
		this.monitorid = monitorid;
	}

	public String getMonitorno() {
		return monitorno;
	}

	public void setMonitorno(String monitorno) {
		this.monitorno = monitorno;
	}

	public Integer getRecomid() {
		return recomid;
	}

	public void setRecomid(Integer recomid) {
		this.recomid = recomid;
	}

	public String getRecomno() {
		return recomno;
	}

	public void setRecomno(String recomno) {
		this.recomno = recomno;
	}

	public String getRejectRemarks() {
		return rejectRemarks;
	}

	public void setRejectRemarks(String rejectRemarks) {
		this.rejectRemarks = rejectRemarks;
	}

	public Integer getDiscardBy() {
		return discardBy;
	}

	public void setDiscardBy(Integer discardBy) {
		this.discardBy = discardBy;
	}

	public Timestamp getDiscardOn() {
		return discardOn;
	}

	public void setDiscardOn(Timestamp discardOn) {
		this.discardOn = discardOn;
	}

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

}
