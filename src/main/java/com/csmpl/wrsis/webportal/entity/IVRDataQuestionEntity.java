package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_ivr_data_ques")
public class IVRDataQuestionEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 @Column(name="int_ivr_ques_id")
	 private int ivrQustionId;
	 @ManyToOne
	 @JoinColumn(name="int_ivr_data_id")
	 private IVRDataEntity ivrDataId;
	
	 @Column(name="int_question_id")
	 private int qustionId; 
	 @Column(name="vch_response_val")
	 private String qustionResponse;
	 @Column(name="bitstatus")
	 private boolean status;
	 @Column(name="intcreatedby")
	 private int createdBy;
	 @Column(name="dtmcreatedon")
	 private Date createdOn;
	 @Column(name="intupdatedby")
	 private int updateBy;
	 @Column(name="dtmupdatedon")
	 private Date updatedOn;
	public int getIvrQustionId() {
		return ivrQustionId;
	}
	public void setIvrQustionId(int ivrQustionId) {
		this.ivrQustionId = ivrQustionId;
	}
	public IVRDataEntity getIvrDataId() {
		return ivrDataId;
	}
	public void setIvrDataId(IVRDataEntity ivrDataId) {
		this.ivrDataId = ivrDataId;
	}
	
	public int getQustionId() {
		return qustionId;
	}
	public void setQustionId(int qustionId) {
		this.qustionId = qustionId;
	}
	public String getQustionResponse() {
		return qustionResponse;
	}
	public void setQustionResponse(String qustionResponse) {
		this.qustionResponse = qustionResponse;
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
	public int getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	 
}
