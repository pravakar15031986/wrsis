package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="m_wr_seed_source")
public class SeedSourceEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5780569250988305375L;

	@Id
	@Column(name="int_seed_src_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer seedSrcId;
	
	@Column(name="vch_seed_src")
	private String seedSrc;
	
	@Column(name="vch_desc")
	private String desc;
	
	@Column(name="bitstatus")
	private Boolean status;
	
	@Column(name="intcreatedby")
	private Integer createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private Integer updatededBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;

	public Integer getSeedSrcId() {
		return seedSrcId;
	}

	public void setSeedSrcId(Integer seedSrcId) {
		this.seedSrcId = seedSrcId;
	}

	public String getSeedSrc() {
		return seedSrc;
	}

	public void setSeedSrc(String seedSrc) {
		this.seedSrc = seedSrc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
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

	public Integer getUpdatededBy() {
		return updatededBy;
	}

	public void setUpdatededBy(Integer updatededBy) {
		this.updatededBy = updatededBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
	
}
