package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;

public class VarietyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int varietyTypeId;

	public int getVarietyTypeId() {
		return varietyTypeId;
	}

	public void setVarietyTypeId(int varietyTypeId) {
		this.varietyTypeId = varietyTypeId;
	}

	public String getVchDesc() {
		return vchDesc;
	}

	public void setVchDesc(String vchDesc) {
		this.vchDesc = vchDesc;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
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

	private String vchDesc;

	private boolean bitStatus;

	private int createdBy;

	private Date createdOn;

	private int updatedBy;

	private Date updatedOn;

}
