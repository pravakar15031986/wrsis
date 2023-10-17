package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_race_analysis_incolution")
public class RaceAnalysisInoculation {

	@Id
	@Column(name = "int_race_incolution_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer raceIncoulationId;

	@Column(name = "int_race_analysis_id")
	private Integer raceAnalysisId;

	@Column(name = "int_repeation_no")
	private Integer repeatationNo;

	@Column(name = "dt_incolutation_date")
	private Date inoculationDate;

	@Column(name = "dt_recording_date")
	private Date recordingDate;

	@Column(name = "vch_incolution_result")
	private String result;
	
	@Column(name = "vch_yellow_rust_result")
	private String vchYellowResult;
	
	

	public String getVchYellowResult() {
		return vchYellowResult;
	}

	public void setVchYellowResult(String vchYellowResult) {
		this.vchYellowResult = vchYellowResult;
	}

	@Column(name = "int_incolution_status")
	private Integer inoculationStatus;

	public Integer getInoculationStatus() {
		return inoculationStatus;
	}

	public Integer getRepeatationNo() {
		return repeatationNo;
	}

	public void setRepeatationNo(Integer repeatationNo) {
		this.repeatationNo = repeatationNo;
	}

	public void setInoculationStatus(Integer inoculationStatus) {
		this.inoculationStatus = inoculationStatus;
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

	public Integer getRaceIncoulationId() {
		return raceIncoulationId;
	}

	public void setRaceIncoulationId(Integer raceIncoulationId) {
		this.raceIncoulationId = raceIncoulationId;
	}

	public Integer getRaceAnalysisId() {
		return raceAnalysisId;
	}

	public void setRaceAnalysisId(Integer raceAnalysisId) {
		this.raceAnalysisId = raceAnalysisId;
	}

	 

	public Date getInoculationDate() {
		return inoculationDate;
	}

	public void setInoculationDate(Date inoculationDate) {
		this.inoculationDate = inoculationDate;
	}

	public Date getRecordingDate() {
		return recordingDate;
	}

	public void setRecordingDate(Date recordingDate) {
		this.recordingDate = recordingDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	 
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
