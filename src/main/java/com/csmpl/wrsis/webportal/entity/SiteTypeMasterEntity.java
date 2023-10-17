package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_survey_site_type")
public class SiteTypeMasterEntity {
	
	@Id
	@Column(name = "int_site_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intSiteTypeId;
	
	@Column(name = "vch_site_name")
	private String vchSiteName;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntSiteTypeId() {
		return intSiteTypeId;
	}

	public void setIntSiteTypeId(Integer intSiteTypeId) {
		this.intSiteTypeId = intSiteTypeId;
	}

	public String getVchSiteName() {
		return vchSiteName;
	}

	public void setVchSiteName(String vchSiteName) {
		this.vchSiteName = vchSiteName;
	}

	 
	
	
	

}
