package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_sample_sampleids")
public class SampleSurveyDetailsEntity {
	
	@Id
	@Column(name = "int_survey_sample_sampleid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer surveySampleId;
	
	@Column(name = "int_survey_sample_id")
	private Integer sampleId;
	
	
	@Column(name = "int_survey_id")
	private Integer surveyId;
	
	@Column(name = "vch_survey_number")
	private String surveyNo;
	@Column(name = "vch_sampleid_value")
	private String sampleValue;
	@Column(name = "bit_tag_status")
	private boolean bitTagStatus; 
 
	


 

	public boolean isBitTagStatus() {
		return bitTagStatus;
	}

	public void setBitTagStatus(boolean bitTagStatus) {
		this.bitTagStatus = bitTagStatus;
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

	 
	 
 
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

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

	public Integer getSampleId() {
		return sampleId;
	}

	public void setSampleId(Integer sampleId) {
		this.sampleId = sampleId;
	}

	 
	public Integer getSurveySampleId() {
		return surveySampleId;
	}

	public void setSurveySampleId(Integer surveySampleId) {
		this.surveySampleId = surveySampleId;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	 
	
	
	

}
