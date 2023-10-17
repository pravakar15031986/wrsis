package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_rust_incident")
public class RustIncidentEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_incident_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer incidentId;

	@Column(name = "int_user_id")
	private Integer userId;

	@Column(name = "dt_incident_date")
	private Date rustDate;

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

	@ManyToOne
	@JoinColumn(name = "int_demography_id")
	private DemographicEntity demographyId;

	@ManyToOne
	@JoinColumn(name = "int_season_id")
	private SeasionMasterEntity seasonId;

	@Column(name = "vch_year")
	private String year;

	@Column(name = "vch_longitude")
	private String longitude;

	@Column(name = "vch_latitude")
	private String latitude;

	public Integer getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(Integer incidentId) {
		this.incidentId = incidentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getRustDate() {
		return rustDate;
	}

	public void setRustDate(Date rustDate) {
		this.rustDate = rustDate;
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

	public DemographicEntity getDemographyId() {
		return demographyId;
	}

	public void setDemographyId(DemographicEntity demographyId) {
		this.demographyId = demographyId;
	}

	public SeasionMasterEntity getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(SeasionMasterEntity seasonId) {
		this.seasonId = seasonId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
