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
@Table(name = "t_wr_monitor_cr_gr_stg")
public class MonitorGrowthStageEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_monitor_crgrstg_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer monitorcrgrstgid;

	@Column(name = "int_monitor_id")
	private Integer monitorid;

	@Column(name = "vch_monitor_no")
	private String monitorno;

	@Column(name = "int_cr_gr_stage_id")
	private Integer growthstageid;

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

	public Integer getMonitorcrgrstgid() {
		return monitorcrgrstgid;
	}

	public void setMonitorcrgrstgid(Integer monitorcrgrstgid) {
		this.monitorcrgrstgid = monitorcrgrstgid;
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

	public Integer getGrowthstageid() {
		return growthstageid;
	}

	public void setGrowthstageid(Integer growthstageid) {
		this.growthstageid = growthstageid;
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
