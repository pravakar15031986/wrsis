package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;

public class YellowRacePhenotypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int racePhenotypeId;

	private int geneticGroupId;

	private String geneticGroupName;

	private String yrIsolateCode;

	private String raceName;

	private String virulencePhenotype;

	private boolean status;

	private int createdBy;

	private Date createdOn;

	private int updatedBy;

	private Date upatedOn;

	public String getGeneticGroupName() {
		return geneticGroupName;
	}

	public void setGeneticGroupName(String geneticGroupName) {
		this.geneticGroupName = geneticGroupName;
	}

	public int getRacePhenotypeId() {
		return racePhenotypeId;
	}

	public int getGeneticGroupId() {
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

	public void setRacePhenotypeId(int racePhenotypeId) {
		this.racePhenotypeId = racePhenotypeId;
	}

	public void setGeneticGroupId(int geneticGroupId) {
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

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpatedOn(Date upatedOn) {
		this.upatedOn = upatedOn;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
