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
@Table(name = "t_wr_monitor_fungicide")
public class MonitorFungicideEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_monitor_fungi_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer monitorfungicideid;

	@Column(name = "int_monitor_id")
	private Integer monitorid;

	@Column(name = "vch_monitor_no")
	private String monitorno;

	@Column(name = "int_fungicide_type_id")
	private Integer fungicideid;

	@Column(name = "vch_fungicide_used")
	private String fungicideused;

	@Column(name = "bitstatus")
	private Boolean bitStatus;

	@Column(name = "intcreatedby")
	private Integer createdBy;

	@Column(name = "dtmcreatedon")
	private Timestamp createdOn;

	@Column(name = "intupdatedby")
	private Integer updatedBy;

	@Column(name = "dtmupdatedon")
	private Timestamp updatedOn;

	public Integer getMonitorfungicideid() {
		return monitorfungicideid;
	}

	public void setMonitorfungicideid(Integer monitorfungicideid) {
		this.monitorfungicideid = monitorfungicideid;
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

	public Integer getFungicideid() {
		return fungicideid;
	}

	public void setFungicideid(Integer fungicideid) {
		this.fungicideid = fungicideid;
	}

	public String getFungicideused() {
		return fungicideused;
	}

	public void setFungicideused(String fungicideused) {
		this.fungicideused = fungicideused;
	}

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}
