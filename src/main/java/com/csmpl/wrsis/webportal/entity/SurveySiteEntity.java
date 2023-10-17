package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_survey_site_details")
public class SurveySiteEntity {

	@Id
	@Column(name = "int_survey_site_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer surveySiteId;

	@Column(name = "int_survey_id")
	private Integer surveyId;

	@Column(name = "vch_survey_number")
	private String surveyNumber;

	@Column(name = "int_site_type_id")
	private Integer siteTypeId;
	
	@Column(name = "int_wheat_type_id")
	private Integer wheatTypeId;
	
	@Column(name="vch_other_variety")
	private String otherVariety;
	
	@Column(name="vch_other_wheatType")
	private String  otherWheat;
	
	

	public String getOtherVariety() {
		return otherVariety;
	}

	public void setOtherVariety(String otherVariety) {
		this.otherVariety = otherVariety;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable=false,insertable=false,name="int_wheat_type_id", referencedColumnName="int_wheat_type_id")
	private WheatTypeMasterEntity wheatTypeMasterEntity;
	 
	
	public WheatTypeMasterEntity getWheatTypeMasterEntity() {
		return wheatTypeMasterEntity;
	}

	public void setWheatTypeMasterEntity(WheatTypeMasterEntity wheatTypeMasterEntity) {
		this.wheatTypeMasterEntity = wheatTypeMasterEntity;
	}

	@Column(name = "vch_area")
	private String area;
	
	@Column(name = "bitstatus")
	private boolean bitstatus;
	
	@Column(name = "intcreatedby")
	private Integer createdBy;

	@Column(name = "dtmcreatedon")
	private Date createdOn;
	
	@Column(name = "intupdatedby")
	private Integer updatedBy;
	@Column(name = "int_cr_gr_stage_id")
	private Integer growthId;
	
	@Column(name="vch_variety")
	private String varity;
	
	
	
	public String getVarity() {
		return varity;
	}

	public void setVarity(String varity) {
		this.varity = varity;
	}

	public Integer getGrowthId() {
		return growthId;
	}

	public void setGrowthId(Integer growthId) {
		this.growthId = growthId;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "dtmupdatedon")
	private Date updatedOn;

	public Integer getSurveySiteId() {
		return surveySiteId;
	}

	public void setSurveySiteId(Integer surveySiteId) {
		this.surveySiteId = surveySiteId;
	}

	public Integer getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Integer surveyId) {
		this.surveyId = surveyId;
	}

	public String getSurveyNumber() {
		return surveyNumber;
	}

	public void setSurveyNumber(String surveyNumber) {
		this.surveyNumber = surveyNumber;
	}

	public Integer getSiteTypeId() {
		return siteTypeId;
	}

	public void setSiteTypeId(Integer siteTypeId) {
		this.siteTypeId = siteTypeId;
	}

	public Integer getWheatTypeId() {
		return wheatTypeId;
	}

	public void setWheatTypeId(Integer wheatTypeId) {
		this.wheatTypeId = wheatTypeId;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	 
	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	public String getOtherWheat() {
		return otherWheat;
	}

	public void setOtherWheat(String otherWheat) {
		this.otherWheat = otherWheat;
	}

	
}
