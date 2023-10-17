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
@Table(name="m_wr_yellow_race_phenotype")
public class YellowRacePhenotypeEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_race_phenotype_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer racePhenotypeId;
	
	@Column(name = "int_genetic_group_id")
	private Integer geneticGroupId;
	
	@Column(name = "vch_yr_isolate_code")
	private String yrIsolateCode;
	
	@Column(name = "vch_race_name")
	private String raceName;
	
	@Column(name = "vch_virulence_phenotype")
	private String virulencePhenotype;
	
	@Column(name = "bitstatus")
	private Boolean status;
	
	@Column(name = "intcreatedby")
	private Integer createdBy;
	
	@Column(name = "dtmcreatedon")
	private Date createdOn;
	
	@Column(name = "intupdatedby")
	private Integer updatedBy;
	
	@Column(name = "dtmupdatedon")
	private Date upatedOn;

	public Integer getRacePhenotypeId() {
		return racePhenotypeId;
	}

	public Integer getGeneticGroupId() {
		return geneticGroupId;
	}

	public String getYrIsolateCode() {
		return yrIsolateCode;
	}

	public String getRaceName() {
		return raceName;
	}

	public String getVirulencePhenotype() {
		return virulencePhenotype;
	}

	public Boolean getStatus() {
		return status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpatedOn() {
		return upatedOn;
	}

	public void setRacePhenotypeId(Integer racePhenotypeId) {
		this.racePhenotypeId = racePhenotypeId;
	}

	public void setGeneticGroupId(Integer geneticGroupId) {
		this.geneticGroupId = geneticGroupId;
	}

	public void setYrIsolateCode(String yrIsolateCode) {
		this.yrIsolateCode = yrIsolateCode;
	}

	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}

	public void setVirulencePhenotype(String virulencePhenotype) {
		this.virulencePhenotype = virulencePhenotype;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpatedOn(Date upatedOn) {
		this.upatedOn = upatedOn;
	}
	
	
}
