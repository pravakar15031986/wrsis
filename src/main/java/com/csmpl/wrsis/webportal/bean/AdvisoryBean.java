package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class AdvisoryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -809196407818862897L;

	private int advisoryId;

	private String advisoryNo;

	private String summaryFromDate;

	private String summaryToDate;

	private String advFileName;

	private MultipartFile advisory;

	private String advRemark;

	private String publishDate;

	private int publishedBy;

	private int advStatus;

	private boolean bitStatus;

	private int createdBy;

	private String createdOn;

	private int updatedBy;

	private Date updatedOn;

	private Integer sNo;
	private String action;

	public int getAdvisoryId() {
		return advisoryId;
	}

	public String getAdvisoryNo() {
		return advisoryNo;
	}

	public String getAdvFileName() {
		return advFileName;
	}

	public MultipartFile getAdvisory() {
		return advisory;
	}

	public String getAdvRemark() {
		return advRemark;
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

	public void setAdvFileName(String advFileName) {
		this.advFileName = advFileName;
	}

	public void setAdvisory(MultipartFile advisory) {
		this.advisory = advisory;
	}

	public void setAdvRemark(String advRemark) {
		this.advRemark = advRemark;
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

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public String getSummaryFromDate() {
		return summaryFromDate;
	}

	public String getSummaryToDate() {
		return summaryToDate;
	}

	public void setSummaryFromDate(String summaryFromDate) {
		this.summaryFromDate = summaryFromDate;
	}

	public void setSummaryToDate(String summaryToDate) {
		this.summaryToDate = summaryToDate;
	}

	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
