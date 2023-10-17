package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "m_wr_season")
public class SeasionMasterEntity {
	
	@Id
	@Column(name = "int_season_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intSeasoonId;
	
	@Column(name = "vch_season")
	private String vchSeason;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	@Transient
	private String months;
	
	 
	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntSeasoonId() {
		return intSeasoonId;
	}

	public void setIntSeasoonId(Integer intSeasoonId) {
		this.intSeasoonId = intSeasoonId;
	}

	public String getVchSeason() {
		return vchSeason;
	}

	public void setVchSeason(String vchSeason) {
		this.vchSeason = vchSeason;
	}

	 

 
	 
	
	
	

}
