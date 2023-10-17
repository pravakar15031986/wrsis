package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_sample_type")
public class SampleTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_sample_type_id")
	private int sampleId;
	@Column(name = "vch_sample_type")
	private String sampleName;
	@Column(name = "vch_desc")
	private String description;
	@Column(name = "bitstatus")
	private boolean status;
	@Column(name = "intcreatedby")
	private int createdBy;
	@Column(name = "dtmcreatedon")
	private Date creadtedOn;
	@Column(name = "intupdatedby")
	private int updatedBy;
	@Column(name = "dtmupdatedon")
	private Date updatedOn;
	@Column(name = "int_rust_type_id")
	private int rustTypeId;

	public int getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(int rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public int getSampleId() {
		return sampleId;
	}

	public void setSampleId(int sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Date getCreadtedOn() {
		return creadtedOn;
	}

	public void setCreadtedOn(Date creadtedOn) {
		this.creadtedOn = creadtedOn;
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
