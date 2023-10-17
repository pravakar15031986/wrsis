package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="m_wr_question")
public class Question {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_question_id")
	private int qustionId;
    @Column(name="vch_question")
    private String qustion;
	@Column(name="int_ques_type")
    private int qustionType;
    @Column(name="int_ques_order")
    private int qustionOrder;
    @Column(name="int_option_count")
    private int qustionOptionCount;
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
    @Column(name="int_ivr_survey_id")
	private int ivrSurveyId;
    
    
	public int getQustionId() {
		return qustionId;
	}
	public void setQustionId(int qustionId) {
		this.qustionId = qustionId;
	}
	public String getQustion() {
		return qustion;
	}
	public void setQustion(String qustion) {
		this.qustion = qustion;
	}
	public int getQustionType() {
		return qustionType;
	}
	public void setQustionType(int qustionType) {
		this.qustionType = qustionType;
	}
	public int getQustionOrder() {
		return qustionOrder;
	}
	public void setQustionOrder(int qustionOrder) {
		this.qustionOrder = qustionOrder;
	}
	public int getQustionOptionCount() {
		return qustionOptionCount;
	}
	public void setQustionOptionCount(int qustionOptionCount) {
		this.qustionOptionCount = qustionOptionCount;
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
	public int getIvrSurveyId() {
		return ivrSurveyId;
	}
	public void setIvrSurveyId(int ivrSurveyId) {
		this.ivrSurveyId = ivrSurveyId;
	}
    
    
}
