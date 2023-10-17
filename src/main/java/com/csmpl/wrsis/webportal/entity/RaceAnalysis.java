package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_race_analysis")
public class RaceAnalysis {
	
	@Id
	@Column(name = "int_race_analysis_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer raceAnalysisId;
	
	
	@Column(name = "int_sample_lab_tag_id")
	private Integer sampleTagId;
	
	@Column(name = "dt_incolutation_date")
	private Date incolutionDate;
	 
	@Column(name="int_race_status")
	private Integer raceStatus;
	
	@Column(name="int_first_wheat_line")
	private Integer firstWheatLine;
	
	@Column(name="bit_publish_status")
	private boolean publishStatus;
	
	@Column(name="vch_race_doc")
	private String raceDoc;
	
	@Column(name="vch_race_result")
	private String raceResult;
	
	
	
	
	

	public boolean isPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(boolean publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getRaceDoc() {
		return raceDoc;
	}

	public void setRaceDoc(String raceDoc) {
		this.raceDoc = raceDoc;
	}

	public String getRaceResult() {
		return raceResult;
	}

	public void setRaceResult(String raceResult) {
		this.raceResult = raceResult;
	}

	public Integer getFirstWheatLine() {
		return firstWheatLine;
	}

	public void setFirstWheatLine(Integer firstWheatLine) {
		this.firstWheatLine = firstWheatLine;
	}

	public Integer getRaceStatus() {
		return raceStatus;
	}

	public void setRaceStatus(Integer raceStatus) {
		this.raceStatus = raceStatus;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Integer getRaceAnalysisId() {
		return raceAnalysisId;
	}

	public void setRaceAnalysisId(Integer raceAnalysisId) {
		this.raceAnalysisId = raceAnalysisId;
	}

	public Integer getSampleTagId() {
		return sampleTagId;
	}

	public void setSampleTagId(Integer sampleTagId) {
		this.sampleTagId = sampleTagId;
	}

	public Date getIncolutionDate() {
		return incolutionDate;
	}

	public void setIncolutionDate(Date incolutionDate) {
		this.incolutionDate = incolutionDate;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

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
