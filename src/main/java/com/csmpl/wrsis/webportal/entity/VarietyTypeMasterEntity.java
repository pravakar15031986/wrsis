package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_variety_type")
public class VarietyTypeMasterEntity {
	
	@Id
	@Column(name = "int_variety_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer varietyTypeId;
	
	@Column(name = "vch_variety_name")
	private String vchDesc;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;
	
	@Column(name = "intcreatedby")
	private int createdBy;
	
	@Column(name = "dtmcreatedon")
	private Date createdOn;
	
	@Column(name = "intupdatedby")
	private int updatedBy;
	
	@Column(name = "dtmupdatedon")
	private Date updatedOn;
	
	
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

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	

	public Integer getVarietyTypeId() {
		return varietyTypeId;
	}

	public void setVarietyTypeId(Integer varietyTypeId) {
		this.varietyTypeId = varietyTypeId;
	}

	public String getVchDesc() {
		return vchDesc;
	}

	public void setVchDesc(String vchDesc) {
		this.vchDesc = vchDesc;
	}

	
	 
	
	
	

}
