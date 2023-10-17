package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_fungicide_details")
public class FungicideDetailsEntity {
	
	@Id
	@Column(name = "int_survey_fungicide_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fungicideId;
	
	
	@Column(name = "int_survey_id")
	private Integer surveyId;
	
	@Column(name = "vch_survey_number")
	private String surveyNo;
	@Column(name = "int_fungicide_type_id")
	private Integer fungicideTypeId;
	
	@Column(name = "dt_spray_date")
	private Date sprayDate;
	
	@Column(name = "vch_dose")
	private String dose;
	
	@Column(name = "int_fung_effect_id")
	private Integer fungEffectId;
	
	@Column(name="vch_other_fungicide")
	private String otherFungi;
	
	


 

	public String getOtherFungi() {
		return otherFungi;
	}

	public void setOtherFungi(String otherFungi) {
		this.otherFungi = otherFungi;
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

	public Integer getFungicideId() {
		return fungicideId;
	}

	public void setFungicideId(Integer fungicideId) {
		this.fungicideId = fungicideId;
	}

	public Integer getFungicideTypeId() {
		return fungicideTypeId;
	}

	public void setFungicideTypeId(Integer fungicideTypeId) {
		this.fungicideTypeId = fungicideTypeId;
	}

	public Date getSprayDate() {
		return sprayDate;
	}

	public void setSprayDate(Date sprayDate) {
		this.sprayDate = sprayDate;
	}

	public String getDose() {
		return dose;
	}

	public void setDose(String dose) {
		this.dose = dose;
	}

	public Integer getFungEffectId() {
		return fungEffectId;
	}

	public void setFungEffectId(Integer fungEffectId) {
		this.fungEffectId = fungEffectId;
	}

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
