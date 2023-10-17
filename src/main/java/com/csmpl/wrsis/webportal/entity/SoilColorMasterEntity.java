package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_soil_color")
public class SoilColorMasterEntity {
	
	@Id
	@Column(name = "int_soil_color_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intSoilColorId;
	
	@Column(name = "vch_soil_color")
	private String vchSoilColor;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	public Integer getIntSoilColorId() {
		return intSoilColorId;
	}

	public void setIntSoilColorId(Integer intSoilColorId) {
		this.intSoilColorId = intSoilColorId;
	}

	public String getVchSoilColor() {
		return vchSoilColor;
	}

	public void setVchSoilColor(String vchSoilColor) {
		this.vchSoilColor = vchSoilColor;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}
	
	
	

}
