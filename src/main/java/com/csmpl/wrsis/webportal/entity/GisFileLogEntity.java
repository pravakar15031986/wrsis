package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_forecast_upload_file_log")
public class GisFileLogEntity {

	@Id
	@Column(name = "int_forecast_upload_file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer uploadFileId;

	@Column(name = "vch_file_name")
	private String fileName;

	@Column(name = "vch_file_loc")
	private String fileLoc;

	@Column(name = "dtm_upload_date")
	public Date uploadedDate;

	@Column(name = "bitstatus")
	private boolean bitStatus;
	
	@Column(name = "dtm_created_on")
	public Date Createdon;

	public Integer getUploadFileId() {
		return uploadFileId;
	}

	public void setUploadFileId(Integer uploadFileId) {
		this.uploadFileId = uploadFileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLoc() {
		return fileLoc;
	}

	public void setFileLoc(String fileLoc) {
		this.fileLoc = fileLoc;
	}
	

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(Date uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	public Date getCreatedon() {
		return Createdon;
	}

	public void setCreatedon(Date createdon) {
		Createdon = createdon;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	@Override
	public String toString() {
		return "GisFileLogEntity [uploadFileId=" + uploadFileId + ", fileName=" + fileName + ", fileLoc=" + fileLoc
				+ ", Createdon=" + Createdon + ", bitStatus=" + bitStatus + "]";
	}

}
