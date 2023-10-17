package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_wheat_type")
public class WheatTypeMasterEntity {
	
	@Id
	@Column(name = "int_wheat_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intWheatTypeId;
	
	@Column(name = "vch_wheat_type")
	private String vchDesc;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntWheatTypeId() {
		return intWheatTypeId;
	}

	public void setIntWheatTypeId(Integer intWheatTypeId) {
		this.intWheatTypeId = intWheatTypeId;
	}

	public String getVchDesc() {
		return vchDesc;
	}

	public void setVchDesc(String vchDesc) {
		this.vchDesc = vchDesc;
	}

 
	 
	
	
	

}
