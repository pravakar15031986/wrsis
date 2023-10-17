package com.csmpl.wrsis.webportal.entity;

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
@Table(name="t_wr_ivr_data")
public class IVRDataEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_ivr_data_id")
	private int ivrDataId;
	
	@ManyToOne
	@JoinColumn(name="int_import_ivr_id")
	private ImportIVRFile ivrFileId;
	
	@Column(name="vch_mobileno")
	private String mobile;
    @Column(name="dtm_call_data_time")
    private Date callDataTime;
    @Column(name="int_region_id")
    private int regionId;
    @Column(name="int_zone_id")
    private int zoneId;
    @Column(name="int_woreda_id")
    private int woredaId;
    @Column(name="bitstatus")
    private boolean status;
    @Column(name="intcreatedby")
    private int createdBy;
    @Column(name="dtmcreatedon")
    private Date createdOn;
    @Column(name="intupdatedby")
    private int updatedBy;
    @Column(name="dtmupdatedon")
    private Date updatedOn;
    @Column(name="vch_language")
    private String language;
    @Column(name="vch_client_type")
    private String clientType;
    @Column(name="vch_gender")
    private String gender;
    @Column(name="int_survey_id")
    private int surveyId;
    @Column(name="vch_responded")
    private String responded;
    @Column(name="vch_survey_title")
    private String surveyTitle;
    @Column(name="vch_region")
    private String regionName;
    @Column(name="vch_zone")
    private String zoneName;
    @Column(name="vch_woreda")
    private String woredaName;
    
    
    
    
	public int getIvrDataId() {
		return ivrDataId;
	}
	public void setIvrDataId(int ivrDataId) {
		this.ivrDataId = ivrDataId;
	}
	public ImportIVRFile getIvrFileId() {
		return ivrFileId;
	}
	public void setIvrFileId(ImportIVRFile ivrFileId) {
		this.ivrFileId = ivrFileId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getCallDataTime() {
		return callDataTime;
	}
	public void setCallDataTime(Date callDataTime) {
		this.callDataTime = callDataTime;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public int getWoredaId() {
		return woredaId;
	}
	public void setWoredaId(int woredaId) {
		this.woredaId = woredaId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}
	public String getResponded() {
		return responded;
	}
	public void setResponded(String responded) {
		this.responded = responded;
	}
	public String getSurveyTitle() {
		return surveyTitle;
	}
	public void setSurveyTitle(String surveyTitle) {
		this.surveyTitle = surveyTitle;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public String getWoredaName() {
		return woredaName;
	}
	public void setWoredaName(String woredaName) {
		this.woredaName = woredaName;
	}
	
	
    
}
