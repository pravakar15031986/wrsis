package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class ProcessBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Short processId;

	private String processName;

	private String description;

	private Boolean bitstatus;

	private Integer createdBy;

	private Timestamp createdOn;

	private Integer updatedBy;

	private Timestamp updatedOn;

	public Short getProcessId() {
		return processId;
	}

	public void setProcessId(Short processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(Boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}
