package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_race_analysis_score")
public class RaceAnalysisScore {
	
	@Id
	@Column(name = "int_race_score_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer raceScoreId;
	
	
	@Column(name = "int_race_incolution_id")
	private Integer inoculationId;
	
	@Column(name = "int_race_analysis_id")
	private Integer raceAnalysisId;
	@Column(name = "int_wheat_diff_line_id")
	private Integer wheatLineId;
	
	@Column(name="vch_h_l_val")
	private String hlValue;
	@Column(name="vch_inf_type")
	private String infType;
	
	public String getInfType() {
		return infType;
	}

	public void setInfType(String infType) {
		this.infType = infType;
	}

	@Column(name="vch_diff_remarks")
	private String remarks;

 

	@Column(name = "bitstatus")
	private boolean bitstatus;

	@Column(name = "intcreatedby")
	private Integer createdBy;

	@Column(name = "dtmcreatedon")
	private Date createdOn;

	@Column(name = "intupdatedby")
	private Integer updatedBy;

	@Column(name = "dtmupdatedon")
	private Date updatedOn;
	
	 

	public Integer getRaceScoreId() {
		return raceScoreId;
	}

	public void setRaceScoreId(Integer raceScoreId) {
		this.raceScoreId = raceScoreId;
	}

	

	public Integer getInoculationId() {
		return inoculationId;
	}

	public void setInoculationId(Integer inoculationId) {
		this.inoculationId = inoculationId;
	}

	public Integer getRaceAnalysisId() {
		return raceAnalysisId;
	}

	public void setRaceAnalysisId(Integer raceAnalysisId) {
		this.raceAnalysisId = raceAnalysisId;
	}

	public Integer getWheatLineId() {
		return wheatLineId;
	}

	public void setWheatLineId(Integer wheatLineId) {
		this.wheatLineId = wheatLineId;
	}

	public String getHlValue() {
		return hlValue;
	}

	public void setHlValue(String hlValue) {
		this.hlValue = hlValue;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	 
	
	
	

}
