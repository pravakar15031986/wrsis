package com.csmpl.wrsis.webportal.entity;
	
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Debendra Nayak
 *
 */
@Entity
@Table(name="t_wr_ivr_survey_details")
public class IVRSurveyDetails {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_ivr_survey_id")
	private int ivrSurveyId;
	
	@Column(name="int_survey_id")
	private int surveyId;
	
	@Column(name="vch_survey_name")
	private String surveyName;
	
	@Column(name = "dt_survey_date")
	private Date surveyDate;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name = "bitstatus")
	private boolean bitstatus;

	public int getIvrSurveyId() {
		return ivrSurveyId;
	}

	public void setIvrSurveyId(int ivrSurveyId) {
		this.ivrSurveyId = ivrSurveyId;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyName() {
		return surveyName;
	}

	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	public Date getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(Date surveyDate) {
		this.surveyDate = surveyDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}
	
	

}
