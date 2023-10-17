package com.csmpl.wrsis.webportal.bean;

public class GisFileLogBean {
	private String updateDate;

	private String fileName;
	private Integer fileId;
	private String fileUploadedOn;
	private String status;

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	

	public String getFileUploadedOn() {
		return fileUploadedOn;
	}

	public void setFileUploadedOn(String fileUploadedOn) {
		this.fileUploadedOn = fileUploadedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GisFileLogBean [updateDate=" + updateDate + ", fileName=" + fileName + ", fileId=" + fileId
				+ ", fileUploadedOn=" + fileUploadedOn + ", status=" + status + "]";
	}

	

}
