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
@Table(name="m_wr_question_option")
public class QuestionOption {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	 @Column(name="int_ques_option_id")
	 private int optionId;
	 
	 @ManyToOne
	 @JoinColumn(name="int_question_id")
	 private Question qustionId;
	 @Column(name="int_option_no")
	 private int optionNumber;
	 @Column(name="vch_option_value")
	 private String optionValue;
	 @Column(name="vch_option_info")
	 private String optionInfo;
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
	public int getOptionId() {
		return optionId;
	}
	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}
	public Question getQustionId() {
		return qustionId;
	}
	public void setQustionId(Question qustionId) {
		this.qustionId = qustionId;
	}
	public int getOptionNumber() {
		return optionNumber;
	}
	public void setOptionNumber(int optionNumber) {
		this.optionNumber = optionNumber;
	}
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getOptionInfo() {
		return optionInfo;
	}
	public void setOptionInfo(String optionInfo) {
		this.optionInfo = optionInfo;
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
	 
	 
}
