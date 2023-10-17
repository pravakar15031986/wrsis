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
@Table(name = "t_wr_recommendation")
public class RecommendationEntiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_recom_id")
	private int recomendId;

	@Column(name = "vch_recom_no")
	private String recomendNo;
	@ManyToOne
	@JoinColumn(name = "int_advisory_id")
	private AdvisoryEntiry advisoryId;

	@Column(name = "vch_advisory_no")
	private String advisoryNo;

	@Column(name = "int_recom_type")
	private int recomendType;

	@Column(name = "vch_recom_file_name")
	private String recomendFileName;

	@Column(name = "vch_recom_summary")
	private String recomendSummary;

	@Column(name = "int_recom_status")
	private int recomendStatus;

	@Column(name = "int_publish_by")
	private int publishedBy;

	@Column(name = "dt_publish_on")
	private Date publishedDate;

	@Column(name = "vch_publish_remarks")
	private String publishedRemark;

	@Column(name = "int_forward_by")
	private int forwordBy;

	@Column(name = "dt_forward_on")
	private Date forwardDate;

	@Column(name = "vch_forward_remarks")
	private String forwardRemark;

	@Column(name = "bitstatus")
	private boolean status;

	@Column(name = "intcreatedby")
	private int createdBy;

	@Column(name = "dtmcreatedon")
	private Date createdOn;

	@Column(name = "intupdatedby")
	private int updatedBy;
	@Column(name = "dtmupdatedon")
	private Date updatedOn;

	@Column(name = "bit_sms_required")
	private Boolean isSmsRequired;

	@Column(name = "vch_sms_content")
	private String smsContent;

	public Boolean getIsSmsRequired() {
		return isSmsRequired;
	}

	public void setIsSmsRequired(Boolean isSmsRequired) {
		this.isSmsRequired = isSmsRequired;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public int getRecomendId() {
		return recomendId;
	}

	public void setRecomendId(int recomendId) {
		this.recomendId = recomendId;
	}

	public String getRecomendNo() {
		return recomendNo;
	}

	public void setRecomendNo(String recomendNo) {
		this.recomendNo = recomendNo;
	}

	public AdvisoryEntiry getAdvisoryId() {
		return advisoryId;
	}

	public void setAdvisoryId(AdvisoryEntiry advisoryId) {
		this.advisoryId = advisoryId;
	}

	public String getAdvisoryNo() {
		return advisoryNo;
	}

	public void setAdvisoryNo(String advisoryNo) {
		this.advisoryNo = advisoryNo;
	}

	public int getRecomendType() {
		return recomendType;
	}

	public void setRecomendType(int recomendType) {
		this.recomendType = recomendType;
	}

	public String getRecomendFileName() {
		return recomendFileName;
	}

	public void setRecomendFileName(String recomendFileName) {
		this.recomendFileName = recomendFileName;
	}

	public String getRecomendSummary() {
		return recomendSummary;
	}

	public void setRecomendSummary(String recomendSummary) {
		this.recomendSummary = recomendSummary;
	}

	public int getRecomendStatus() {
		return recomendStatus;
	}

	public void setRecomendStatus(int recomendStatus) {
		this.recomendStatus = recomendStatus;
	}

	public int getPublishedBy() {
		return publishedBy;
	}

	public void setPublishedBy(int publishedBy) {
		this.publishedBy = publishedBy;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public String getPublishedRemark() {
		return publishedRemark;
	}

	public void setPublishedRemark(String publishedRemark) {
		this.publishedRemark = publishedRemark;
	}

	public int getForwordBy() {
		return forwordBy;
	}

	public void setForwordBy(int forwordBy) {
		this.forwordBy = forwordBy;
	}

	public Date getForwardDate() {
		return forwardDate;
	}

	public void setForwardDate(Date forwardDate) {
		this.forwardDate = forwardDate;
	}

	public String getForwardRemark() {
		return forwardRemark;
	}

	public void setForwardRemark(String forwardRemark) {
		this.forwardRemark = forwardRemark;
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
