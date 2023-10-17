package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_moisture")
public class MoistureMasterEntity {
	
	@Id
	@Column(name = "int_moisture_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intMoistureId;
	
	@Column(name = "vch_moisture")
	private String vchMoisture;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntMoistureId() {
		return intMoistureId;
	}

	public void setIntMoistureId(Integer intMoistureId) {
		this.intMoistureId = intMoistureId;
	}

	public String getVchMoisture() {
		return vchMoisture;
	}

	public void setVchMoisture(String vchMoisture) {
		this.vchMoisture = vchMoisture;
	}
	
	
	

}
