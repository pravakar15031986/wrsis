package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_race_group")
public class RaceGroupEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5124075798249201645L;

	@Id
	@Column(name="int_race_group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int raceGroupId;
	
	@Column(name="int_rust_type_id")
	private int rustTypeId;
	
	@Column(name="int_max_value")
	private int maxValue;
	
	@Column(name="bitstatus")
	private boolean status;

	public int getRaceGroupId() {
		return raceGroupId;
	}

	public void setRaceGroupId(int raceGroupId) {
		this.raceGroupId = raceGroupId;
	}

	public int getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(int rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
}
