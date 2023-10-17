package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_fungicide_effect")
public class FungicideEffectTypeMasterEntity {
	
	@Id
	@Column(name = "int_fung_effect_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intEffectId;
	
	@Column(name = "vch_effect_desc")
	private String vchDesc;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}


	public Integer getIntEffectId() {
		return intEffectId;
	}

	public void setIntEffectId(Integer intEffectId) {
		this.intEffectId = intEffectId;
	}

	public String getVchDesc() {
		return vchDesc;
	}

	public void setVchDesc(String vchDesc) {
		this.vchDesc = vchDesc;
	}

 
	 
	
	
	

}
