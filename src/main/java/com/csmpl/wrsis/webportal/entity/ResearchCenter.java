package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_research_center")
public class ResearchCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_research_center_id")
	private int researchCenterId;
	@Column(name = "vch_research_center_name")
	private String researchCenterName;
	@Column(name = "int_is_lab")
	private boolean labStatus;
	@Column(name = "vch_desc")
	private String desc;
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

	public int getResearchCenterId() {
		return researchCenterId;
	}

	public void setResearchCenterId(int researchCenterId) {
		this.researchCenterId = researchCenterId;
	}

	public String getResearchCenterName() {
		return researchCenterName;
	}

	public void setResearchCenterName(String researchCenterName) {
		this.researchCenterName = researchCenterName;
	}

	public boolean isLabStatus() {
		return labStatus;
	}

	public void setLabStatus(boolean labStatus) {
		this.labStatus = labStatus;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
