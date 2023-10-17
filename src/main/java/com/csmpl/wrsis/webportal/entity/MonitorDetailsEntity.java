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
@Table(name = "t_wr_monitor_details")
public class MonitorDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_monitor_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer monitorid;

	@Column(name = "vch_monitor_no")
	private String monitorno;

	@Column(name = "int_recom_id")
	private Integer recomid;

	@Column(name = "vch_recom_no")
	private String recomno;

	@Column(name = "int_woreda_id")
	private Integer woredaid;

	@Column(name = "vch_severity_pc")
	private String severitypc;

	@Column(name = "vch_incidences_pc")
	private String incidencespc;

	@Column(name = "vch_sowing_land")
	private String sowingland;

	@Column(name = "vch_infected_land")
	private String infectedland;

	@Column(name = "vch_controlled_land")
	private String controlledland;

	@Column(name = "int_male_farmer")
	private Integer malefarmer;

	@Column(name = "int_female_farmer")
	private Integer femalefarmer;

	@Column(name = "int_total_farmer")
	private Integer totalfarmer;

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
	
	@Column(name = "int_monitor_status")
	private Integer monitorStatus;
	
	@Column(name = "int_data_collect_mode")
	private Integer collectMode;
	
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

	public Integer getWoredaid() {
		return woredaid;
	}

	public void setWoredaid(Integer woredaid) {
		this.woredaid = woredaid;
	}

	public String getSeveritypc() {
		return severitypc;
	}

	public void setSeveritypc(String severitypc) {
		this.severitypc = severitypc;
	}

	public String getIncidencespc() {
		return incidencespc;
	}

	public void setIncidencespc(String incidencespc) {
		this.incidencespc = incidencespc;
	}

	public String getSowingland() {
		return sowingland;
	}

	public void setSowingland(String sowingland) {
		this.sowingland = sowingland;
	}

	public String getInfectedland() {
		return infectedland;
	}

	public void setInfectedland(String infectedland) {
		this.infectedland = infectedland;
	}

	public String getControlledland() {
		return controlledland;
	}

	public void setControlledland(String controlledland) {
		this.controlledland = controlledland;
	}

	public Integer getMalefarmer() {
		return malefarmer;
	}

	public void setMalefarmer(Integer malefarmer) {
		this.malefarmer = malefarmer;
	}

	public Integer getFemalefarmer() {
		return femalefarmer;
	}

	public void setFemalefarmer(Integer femalefarmer) {
		this.femalefarmer = femalefarmer;
	}

	public Integer getTotalfarmer() {
		return totalfarmer;
	}

	public void setTotalfarmer(Integer totalfarmer) {
		this.totalfarmer = totalfarmer;
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

	public Integer getMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(Integer monitorStatus) {
		this.monitorStatus = monitorStatus;
	}

	public Integer getCollectMode() {
		return collectMode;
	}

	public void setCollectMode(Integer collectMode) {
		this.collectMode = collectMode;
	}
	

}
