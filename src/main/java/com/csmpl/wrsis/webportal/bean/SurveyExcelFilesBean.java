package com.csmpl.wrsis.webportal.bean;

import javax.persistence.Transient;

public class SurveyExcelFilesBean {

	private Integer fileSurveyId;
	private String fileName;
	private Integer count;
	private Boolean status;
	private Integer createdBy;
	private String createdOn;
	private Integer updatedBy;
	private String updatedOn;
	@Transient
	private String fileDecodeName;
	private Integer slNo;
	private String countViewLink;
	private String fileDownloadLink;

	public String getFileDecodeName() {
		return fileDecodeName;
	}

	public void setFileDecodeName(String fileDecodeName) {
		this.fileDecodeName = fileDecodeName;
	}

	public Integer getFileSurveyId() {
		return fileSurveyId;
	}

	public void setFileSurveyId(Integer fileSurveyId) {
		this.fileSurveyId = fileSurveyId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getSlNo() {
		return slNo;
	}

	public void setSlNo(Integer slNo) {
		this.slNo = slNo;
	}

	public String getCountViewLink() {
		return countViewLink;
	}

	public void setCountViewLink(String countViewLink) {
		this.countViewLink = countViewLink;
	}

	public String getFileDownloadLink() {
		return fileDownloadLink;
	}

	public void setFileDownloadLink(String fileDownloadLink) {
		this.fileDownloadLink = fileDownloadLink;
	}

}
