package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_season")
public class SeasonMaster {
	 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_season_id")
	private int seasonId;
	
	@Column(name="vch_season")
	private String seasonName;
	
	@Column(name="vch_desc")
	private String desc;
	
	@Column(name="bitstatus")
	private boolean status;
	
	@Column(name="intcreatedby")
	private Integer createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;

	@Column(name="intupdatedby")
	private Integer updateBy;
	
	@Column(name="dtmupdatedon")
	private Date updatOn;
	
	
	public int getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(int seasonId) {
		this.seasonId = seasonId;
	}
	
	public String getSeasonName() {
		return seasonName;
	}
	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
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
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdatOn() {
		return updatOn;
	}
	public void setUpdatOn(Date updatOn) {
		this.updatOn = updatOn;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	     
}
