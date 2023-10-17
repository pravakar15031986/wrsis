package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_race_score_master")
public class RaceScoringMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_race_score_id")
	private int raceScoreId;
	@ManyToOne
	@JoinColumn(name = "int_rust_type_id")
	private TypeOfRust rustId;

	@Column(name = "vch_infection_type_value")
	private String infectionType;

	@Column(name = "vch_l_h_value")
	private String hlValue;

	@Column(name = "bitstatus")
	private boolean status;
	@Column(name = "intcreatedby")
	private int createdBy;
	@Column(name = "dtmcreatedon")
	private Date createdDate;
	@Column(name = "intupdatedby")
	private int updatedBy;
	@Column(name = "dtmupdatedon")
	private Date updatedOn;

	public int getRaceScoreId() {
		return raceScoreId;
	}

	public void setRaceScoreId(int raceScoreId) {
		this.raceScoreId = raceScoreId;
	}

	public TypeOfRust getRustId() {
		return rustId;
	}

	public void setRustId(TypeOfRust rustId) {
		this.rustId = rustId;
	}

	public String getInfectionType() {
		return infectionType;
	}

	public void setInfectionType(String infectionType) {
		this.infectionType = infectionType;
	}

	public String getHlValue() {
		return hlValue;
	}

	public void setHlValue(String hlValue) {
		this.hlValue = hlValue;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
