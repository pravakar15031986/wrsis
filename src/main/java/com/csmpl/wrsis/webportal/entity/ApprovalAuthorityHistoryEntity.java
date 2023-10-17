package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_approval_history")
public class ApprovalAuthorityHistoryEntity {
	
	@Id
	@Column(name = "int_approval_history_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer aproveId;
	
	@Column(name = "int_approval_process_id")
	private Integer aprovalPId;
	@Column(name = "int_stage_no")
	private Integer stageNo;
	@Column(name = "int_application_id")
	private Integer surveyId;
 
	@Column(name = "int_pending_at")
	private Integer pendingAt;
	@Column(name = "int_sent_from")
	private Integer sentFrom;

	@Column(name = "bitstatus")
	private boolean status;
	@Column(name="intcreatedby")
	private int creadtedBy;
	@Column(name="dtmcreatedon")
	private Date createdOn;
    @Column(name="intupdatedby")
    private int updatedBy;
    @Column(name="dtmupdatedon")
    private Date updatedOn;


	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Integer getAproveId() {
		return aproveId;
	}

	public void setAproveId(Integer aproveId) {
		this.aproveId = aproveId;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public Integer getPendingAt() {
		return pendingAt;
	}

	public void setPendingAt(Integer pendingAt) {
		this.pendingAt = pendingAt;
	}

	public Integer getSentFrom() {
		return sentFrom;
	}

	public void setSentFrom(Integer sentFrom) {
		this.sentFrom = sentFrom;
	}

	public Integer getAprovalPId() {
		return aprovalPId;
	}

	public void setAprovalPId(Integer aprovalPId) {
		this.aprovalPId = aprovalPId;
	}

	public Integer getStageNo() {
		return stageNo;
	}

	public void setStageNo(Integer stageNo) {
		this.stageNo = stageNo;
	}

	public int getCreadtedBy() {
		return creadtedBy;
	}

	public void setCreadtedBy(int creadtedBy) {
		this.creadtedBy = creadtedBy;
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
	
	
	 
}
	