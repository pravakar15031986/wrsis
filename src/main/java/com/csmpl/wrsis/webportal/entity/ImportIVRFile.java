package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_import_ivr_files")
public class ImportIVRFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_import_ivr_id")
	private Integer imprtIVRId;
	@Column(name = "vch_ivr_file_name")
	private String fileName;
	@Column(name = "int_ivr_record_count")
	private int recordCount;
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
	@Column(name = "text_api_response")
	private String apiResponse;
	

	public Integer getImprtIVRId() {
		return imprtIVRId;
	}

	public void setImprtIVRId(Integer imprtIVRId) {
		this.imprtIVRId = imprtIVRId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
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

	public String getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(String apiResponse) {
		this.apiResponse = apiResponse;
	}

	
}
