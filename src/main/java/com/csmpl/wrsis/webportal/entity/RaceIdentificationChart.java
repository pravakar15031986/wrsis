package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_race_identification_chart")
public class RaceIdentificationChart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_race_chart_id")
	private int raceChartId;
	
	@Column(name="vch_first_seq")
	private String fisrtSeq;
	
	@Column(name="vch_second_seq")
	private String secondSeq;
	
	@Column(name="vch_third_seq")
	private String thirdSeq;
	
	@Column(name="vch_fourth_seq")
	private String fourthSeq;
	
	@Column(name="vch_result_name")
	private String nameResult;
	
	@Column(name="vch_dsc")
	private String description;
	
	@Column(name="bitstatus")
	private boolean status;
	
	@Column(name="intcreatedby")
	private int createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private int updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updateOn;
	
	public int getRaceChartId() {
		return raceChartId;
	}
	public void setRaceChartId(int raceChartId) {
		this.raceChartId = raceChartId;
	}
	public String getFisrtSeq() {
		return fisrtSeq;
	}
	public void setFisrtSeq(String fisrtSeq) {
		this.fisrtSeq = fisrtSeq;
	}
	public String getSecondSeq() {
		return secondSeq;
	}
	public void setSecondSeq(String secondSeq) {
		this.secondSeq = secondSeq;
	}
	public String getThirdSeq() {
		return thirdSeq;
	}
	public void setThirdSeq(String thirdSeq) {
		this.thirdSeq = thirdSeq;
	}
	public String getFourthSeq() {
		return fourthSeq;
	}
	public void setFourthSeq(String fourthSeq) {
		this.fourthSeq = fourthSeq;
	}
	
	
	public String getNameResult() {
		return nameResult;
	}
	public void setNameResult(String nameResult) {
		this.nameResult = nameResult;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdateOn() {
		return updateOn;
	}
	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}
	
	
}
