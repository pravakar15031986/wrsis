package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_surveyor_details")
public class SurveyorDetailsEntity {

	@Id
	@Column(name = "int_surveyor_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer surveyorDetailsId;

	@Column(name = "int_survey_id")
	private Integer surveyId;

	@Column(name = "vch_survey_number")
	private String surveyNumber;
	@Column(name = "int_research_center_id")
	private Integer centerId;
	

	@Column(name = "int_surveyor_user_id")
	private Integer surveyorUserId;
	
	@Column(name = "vch_surveyor_name")
	private String surveyorName;
	
	@Column(name = "int_country_id")
	private Integer countryId;
 
	
	
	
	
	
	

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

	public Integer getSurveyorDetailsId() {
	return surveyorDetailsId;
}

public void setSurveyorDetailsId(Integer surveyorDetailsId) {
	this.surveyorDetailsId = surveyorDetailsId;
}

public Integer getSurveyId() {
	return surveyId;
}

public void setSurveyId(Integer surveyId) {
	this.surveyId = surveyId;
}

public String getSurveyNumber() {
	return surveyNumber;
}

public void setSurveyNumber(String surveyNumber) {
	this.surveyNumber = surveyNumber;
}

public Integer getCenterId() {
	return centerId;
}

public void setCenterId(Integer centerId) {
	this.centerId = centerId;
}

public Integer getSurveyorUserId() {
	return surveyorUserId;
}

public void setSurveyorUserId(Integer surveyorUserId) {
	this.surveyorUserId = surveyorUserId;
}

public String getSurveyorName() {
	return surveyorName;
}

public void setSurveyorName(String surveyorName) {
	this.surveyorName = surveyorName;
}

public Integer getCountryId() {
	return countryId;
}

public void setCountryId(Integer countryId) {
	this.countryId = countryId;
}

public Integer getUpdatedBy() {
	return updatedBy;
}

public void setUpdatedBy(Integer updatedBy) {
	this.updatedBy = updatedBy;
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
