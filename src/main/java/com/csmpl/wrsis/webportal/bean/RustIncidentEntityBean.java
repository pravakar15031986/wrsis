package com.csmpl.wrsis.webportal.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.csmpl.wrsis.webportal.entity.SeasionMasterEntity;

public class RustIncidentEntityBean {

	private Integer incidentId;

	private Integer userId;

	private Date rustDate;

	private Boolean bitStatus;

	private Integer createdBy;

	private Timestamp createdOn;

	private Integer updatedBy;

	private Timestamp updatedOn;

	private String woredaName;
	private String kebeleName;

	private Integer regionId;
	private Integer zoneId;
	private Integer woredaId;
	private Integer kebeleId;
	private Integer yearId;
	private Integer seasonTypeId;
	private String incidendDate;
	private String seasonName;
	private String queostions;
	private List<String> questionsData;

	private String zoneName;
	private String regionName;

	private String userFullName;
	private Integer sNo;
	private String modalView;

	public List<String> getQuestionsData() {
		return questionsData;
	}

	public void setQuestionsData(List<String> questionsData) {
		this.questionsData = questionsData;
	}

	public String getQueostions() {
		return queostions;
	}

	public void setQueostions(String queostions) {
		this.queostions = queostions;
	}

	private SeasionMasterEntity seasonId;

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getZoneId() {
		return zoneId;
	}

	public void setZoneId(Integer zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getWoredaId() {
		return woredaId;
	}

	public void setWoredaId(Integer woredaId) {
		this.woredaId = woredaId;
	}

	public Integer getKebeleId() {
		return kebeleId;
	}

	public void setKebeleId(Integer kebeleId) {
		this.kebeleId = kebeleId;
	}

	public Integer getYearId() {
		return yearId;
	}

	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}

	public Integer getSeasonTypeId() {
		return seasonTypeId;
	}

	public void setSeasonTypeId(Integer seasonTypeId) {
		this.seasonTypeId = seasonTypeId;
	}

	public String getIncidendDate() {
		return incidendDate;
	}

	public void setIncidendDate(String incidendDate) {
		this.incidendDate = incidendDate;
	}

	private String year;

	private String longitude;

	private String latitude;
	// Created By Raman
	private String incidentDate;

	public String getIncidentDate() {
		return incidentDate;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setIncidentDate(String incidentDate) {
		this.incidentDate = incidentDate;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

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

	public String getWoredaName() {
		return woredaName;
	}

	public void setWoredaName(String woredaName) {
		this.woredaName = woredaName;
	}

	public String getKebeleName() {
		return kebeleName;
	}

	public void setKebeleName(String kebeleName) {
		this.kebeleName = kebeleName;
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

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getModalView() {
		return modalView;
	}

	public void setModalView(String modalView) {
		this.modalView = modalView;
	}

}
