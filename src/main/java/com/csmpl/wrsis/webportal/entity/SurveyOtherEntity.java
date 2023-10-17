package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_other_details")
public class SurveyOtherEntity {
	
	@Id
	@Column(name = "int_survey_oth_dtl_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer surveyOtherId;
	
	@Column(name = "int_survey_id")
	private Integer surveyId;
	
	@Column(name = "vch_survey_number")
	private String surveyNo;
	
	@Column(name = "int_is_irrigated")
	private boolean irrigated;
	@Column(name = "int_is_weed_control")
	private boolean weedControl;
	@Column(name = "int_moisture_id")
	private Integer moistureId;
	@Column(name = "int_soil_color_id")
	private Integer soilColorId;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	 
	public Integer getSurveyOtherId() {
		return surveyOtherId;
	}

	public void setSurveyOtherId(Integer surveyOtherId) {
		this.surveyOtherId = surveyOtherId;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}
 
	public boolean isIrrigated() {
		return irrigated;
	}

	public void setIrrigated(boolean irrigated) {
		this.irrigated = irrigated;
	}

	public String getSurveyNo() {
		return surveyNo;
	}

	public void setSurveyNo(String surveyNo) {
		this.surveyNo = surveyNo;
	}

	public boolean isWeedControl() {
		return weedControl;
	}

	public void setWeedControl(boolean weedControl) {
		this.weedControl = weedControl;
	}

	public Integer getMoistureId() {
		return moistureId;
	}

	public void setMoistureId(Integer moistureId) {
		this.moistureId = moistureId;
	}

	public Integer getSoilColorId() {
		return soilColorId;
	}

	public void setSoilColorId(Integer soilColorId) {
		this.soilColorId = soilColorId;
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

	 
	 
	
	
	

}
