package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_sample_lab_tag_details")
public class SampleLabTagDetails {
	
	@Id
	@Column(name = "int_sample_lab_tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampleLabTagId;
	
	
	@Column(name = "int_survey_sample_sampleid")
	private Integer sampleIds;
	
	 
	@Column(name = "int_survey_sample_id")
	private Integer sampleId;
	
	@Column(name = "int_survey_id")
	private Integer surveyId;
	
	@Column(name = "vch_survey_number")
	private String surveyNo;
	 
	@Column(name = "vch_sampleid_value")
	private String sampleIdValue;
	
	@Column(name = "int_rust_type_id")
	private Integer rustTypeId;
	
	@Column(name = "int_region_id")
	private Integer regionId;
	
	@Column(name = "dt_survey_date")
	private Date surveyDate;
	
	@Column(name = "int_research_center_id")
	private Integer researchCenterId;
	
	@Column(name = "int_sample_status")
	private Integer raceStatus;
	
	 
	@Column(name = "vch_race_result")
	private String raceResult;

	@Column(name = "bit_external_lab")
	private boolean externallab;

	@Column(name = "bitstatus")
	private boolean bitstatus;

	@Column(name = "intcreatedby")
	private Integer createdBy;

	@Column(name = "dtmcreatedon")
	private Date createdOn;

	@Column(name = "intupdatedby")
	private Integer updatedBy;

	@Column(name = "dtmupdatedon")
	private Date updatedOn;

	@Column(name="vch_reject_remarks")
	private String rejectRemark;
	 
	
	public String getRejectRemark() {
		return rejectRemark;
	}

	public void setRejectRemark(String rejectRemark) {
		this.rejectRemark = rejectRemark;
	}

	public Integer getSampleLabTagId() {
		return sampleLabTagId;
	}

	public void setSampleLabTagId(Integer sampleLabTagId) {
		this.sampleLabTagId = sampleLabTagId;
	}

	public Integer getSampleIds() {
		return sampleIds;
	}

	public void setSampleIds(Integer sampleIds) {
		this.sampleIds = sampleIds;
	}

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public String getSampleIdValue() {
		return sampleIdValue;
	}

	public void setSampleIdValue(String sampleIdValue) {
		this.sampleIdValue = sampleIdValue;
	}

	public Integer getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(Integer rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}

	public Integer getResearchCenterId() {
		return researchCenterId;
	}

	public void setResearchCenterId(Integer researchCenterId) {
		this.researchCenterId = researchCenterId;
	}

	public Integer getRaceStatus() {
		return raceStatus;
	}

	public void setRaceStatus(Integer raceStatus) {
		this.raceStatus = raceStatus;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getRaceResult() {
		return raceResult;
	}

	public void setRaceResult(String raceResult) {
		this.raceResult = raceResult;
	}

	public boolean isExternallab() {
		return externallab;
	}

	public void setExternallab(boolean externallab) {
		this.externallab = externallab;
	}

 
	
	

}
