package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_wr_advisory_details")
public class AdvisoryEntiry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2824876950917676289L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="int_advisory_id")
	private int advisoryId;
	
	@Column(name="vch_advisory_no")
	private String advisoryNo;
	
	@Column(name="dt_summary_from_date")
	private Date summaryFromDate;
	
	@Column(name="dt_summary_to_date")
	private Date summaryToDate;
	
	@Column(name="vch_advisory_file_name")
	private String advFileName;
	
	@Column(name="vch_advisory_remarks")
	private String advRemark;
	
	@Column(name="dtm_publish_date")
	private Date publishDate;
	
	@Column(name="int_published_by")
	private int publishedBy;
	
	@Column(name="int_advisory_status")
	private int advStatus;
	
	@Column(name="bitstatus")
	private boolean bitStatus;
	
	@Column(name="intcreatedby")
	private int createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private int updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;

	public int getAdvisoryId() {
		return advisoryId;
	}

	public String getAdvisoryNo() {
		return advisoryNo;
	}

	public Date getSummaryFromDate() {
		return summaryFromDate;
	}

	public Date getSummaryToDate() {
		return summaryToDate;
	}

	public String getAdvFileName() {
		return advFileName;
	}

	public String getAdvRemark() {
		return advRemark;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public int getPublishedBy() {
		return publishedBy;
	}

	public int getAdvStatus() {
		return advStatus;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setAdvisoryId(int advisoryId) {
		this.advisoryId = advisoryId;
	}

	public void setAdvisoryNo(String advisoryNo) {
		this.advisoryNo = advisoryNo;
	}

	public void setSummaryFromDate(Date summaryFromDate) {
		this.summaryFromDate = summaryFromDate;
	}

	public void setSummaryToDate(Date summaryToDate) {
		this.summaryToDate = summaryToDate;
	}

	public void setAdvFileName(String advFileName) {
		this.advFileName = advFileName;
	}

	public void setAdvRemark(String advRemark) {
		this.advRemark = advRemark;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public void setPublishedBy(int publishedBy) {
		this.publishedBy = publishedBy;
	}

	public void setAdvStatus(int advStatus) {
		this.advStatus = advStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
}
