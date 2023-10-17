package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_rust_details")
public class SurveyRustDetailsEntity {
	
	@Id
	@Column(name = "int_survey_rust_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intSurveyRustId;
	
	
	@Column(name = "int_survey_id")
	private Integer surveyId;
	
	@Column(name = "vch_survey_number")
	private String surveyNo;
	@Column(name = "int_rust_type_id")
	private Integer rustTypeId;
	
	@OneToOne
	@JoinColumn(updatable=false,insertable=false,name="int_rust_type_id", referencedColumnName="int_rust_type_id")
	private RustTypeMasterEntity rustType;
	
	

	public RustTypeMasterEntity getRustType() {
		return rustType;
	}

	public void setRustType(RustTypeMasterEntity rustType) {
		this.rustType = rustType;
	}

	@Column(name = "vch_incident")
	private String incident;
	
	@Column(name = "vch_severity")
	private String severity;
	
	@Column(name = "int_rust_reaction_id")
	private Integer rustReactionId;
	
	@Column(name = "vch_head_incident")
	private String headIncident;
	
	@Column(name = "vch_head_severity")
	private String headSeverity;

 

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

	 
	 

	public Integer getIntSurveyRustId() {
		return intSurveyRustId;
	}

	public void setIntSurveyRustId(Integer intSurveyRustId) {
		this.intSurveyRustId = intSurveyRustId;
	}

	public Integer getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(Integer rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public String getIncident() {
		return incident;
	}

	public void setIncident(String incident) {
		this.incident = incident;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public Integer getRustReactionId() {
		return rustReactionId;
	}

	public void setRustReactionId(Integer rustReactionId) {
		this.rustReactionId = rustReactionId;
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

	public String getHeadIncident() {
		return headIncident;
	}

	public void setHeadIncident(String headIncident) {
		this.headIncident = headIncident;
	}

	public String getHeadSeverity() {
		return headSeverity;
	}

	public void setHeadSeverity(String headSeverity) {
		this.headSeverity = headSeverity;
	}
	 
	
	
	

}
