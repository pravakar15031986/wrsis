package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_survey_discard_log")
public class SurveyDiscardLogEntity {

	@Id
	@Column(name="int_svy_dis_log_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer discardLogId;
	
	@Column(name="int_survey_id")
	private Integer surveyId;
	
	@Column(name="vch_survey_no")
	private String surveyNo;
	
	@Column(name="vch_reject_remarks")
	private String remark;
	
	@Column(name="int_discarded_by")
	private Integer discardBy;
	
	@Column(name="dtm_discarded_on")
	private Date discardedOn;
	
	@Column(name="bit_status")
	private boolean status;

	public Integer getDiscardLogId() {
		return discardLogId;
	}

	public void setDiscardLogId(Integer discardLogId) {
		this.discardLogId = discardLogId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDiscardBy() {
		return discardBy;
	}

	public void setDiscardBy(Integer discardBy) {
		this.discardBy = discardBy;
	}

	public Date getDiscardedOn() {
		return discardedOn;
	}

	public void setDiscardedOn(Date discardedOn) {
		this.discardedOn = discardedOn;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
