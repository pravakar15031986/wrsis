package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;

public class WheatDifferentialLineBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7568267184709883182L;

	private int wheatDifLineId;

	private int rustTypeId;

	private String difLine;

	private int seedSrcId;

	private String gene;

	private String expectLowIt;

	private int diffSetNo;

	private int firstLine;

	private String desc;

	private boolean status;

	private Integer createdBy;

	private Date createdOn;

	private Integer updatedBy;

	private Date updatedOn;

	private String rustTypeName;

	private String seedSourceName;

	private String seqNo;

	private String racephenotype;

	public String getSeqNo() {
		return seqNo;
	}

	public String getRacephenotype() {
		return racephenotype;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public void setRacephenotype(String racephenotype) {
		this.racephenotype = racephenotype;
	}

	public int getWheatDifLineId() {
		return wheatDifLineId;
	}

	public int getRustTypeId() {
		return rustTypeId;
	}

	public String getDifLine() {
		return difLine;
	}

	public int getSeedSrcId() {
		return seedSrcId;
	}

	public String getGene() {
		return gene;
	}

	public String getExpectLowIt() {
		return expectLowIt;
	}

	public int getDiffSetNo() {
		return diffSetNo;
	}

	public int getFirstLine() {
		return firstLine;
	}

	public String getDesc() {
		return desc;
	}

	public boolean getStatus() {
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

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public String getRustTypeName() {
		return rustTypeName;
	}

	public String getSeedSourceName() {
		return seedSourceName;
	}

	public void setWheatDifLineId(int wheatDifLineId) {
		this.wheatDifLineId = wheatDifLineId;
	}

	public void setRustTypeId(int rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public void setDifLine(String difLine) {
		this.difLine = difLine;
	}

	public void setSeedSrcId(int seedSrcId) {
		this.seedSrcId = seedSrcId;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public void setExpectLowIt(String expectLowIt) {
		this.expectLowIt = expectLowIt;
	}

	public void setDiffSetNo(int diffSetNo) {
		this.diffSetNo = diffSetNo;
	}

	public void setFirstLine(int firstLine) {
		this.firstLine = firstLine;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setStatus(boolean status) {
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

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public void setRustTypeName(String rustTypeName) {
		this.rustTypeName = rustTypeName;
	}

	public void setSeedSourceName(String seedSourceName) {
		this.seedSourceName = seedSourceName;
	}

}
