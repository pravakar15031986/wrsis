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
@Table(name="m_wr_wheat_differ_line")
public class WheatDifferentialLineEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1310896570000261307L;
	
	@Id
	@Column(name="int_wheat_dif_line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int wheatDifLineId;
	
	@Column(name="int_rust_type_id")
	private int rustTypeId;
	
	@Column(name="vch_dif_line")
	private String difLine;
	
	@Column(name="int_seed_src_id")
	private int seedSrcId;
	
	@Column(name="vch_gene")
	private String gene;
	
	@Column(name="vch_expect_lowit")
	private String expectLowIt;
	
	@Column(name="vch_desc")
	private String desc;
	
	@Column(name="bitstatus")
	private Boolean status;
	
	@Column(name="intcreatedby")
	private Integer createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private Integer updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;
	
	@Column(name="int_diff_set_no")
	private int diffSetNo;
	
	@Column(name="bit_first_line")
	private int firstLine;

	@Column(name="vch_race_phenotype")
	private String racePhenotype;
	
	@Column(name="int_seq_no")
	private int seqNo;
	
	public Integer getWheatDifLineId() {
		return wheatDifLineId;
	}

	public void setWheatDifLineId(Integer wheatDifLineId) {
		this.wheatDifLineId = wheatDifLineId;
	}

	public Integer getRustTypeId() {
		return rustTypeId;
	}

	public void setRustTypeId(Integer rustTypeId) {
		this.rustTypeId = rustTypeId;
	}

	public String getDifLine() {
		return difLine;
	}

	public void setDifLine(String difLine) {
		this.difLine = difLine;
	}

	public Integer getSeedSrcId() {
		return seedSrcId;
	}

	public void setSeedSrcId(Integer seedSrcId) {
		this.seedSrcId = seedSrcId;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getExpectLowIt() {
		return expectLowIt;
	}

	public void setExpectLowIt(String expectLowIt) {
		this.expectLowIt = expectLowIt;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Integer getDiffSetNo() {
		return diffSetNo;
	}

	public void setDiffSetNo(Integer diffSetNo) {
		this.diffSetNo = diffSetNo;
	}

	public int getFirstLine() {
		return firstLine;
	}

	public void setFirstLine(int firstLine) {
		this.firstLine = firstLine;
	}

	public String getRacePhenotype() {
		return racePhenotype;
	}

	public void setRacePhenotype(String racePhenotype) {
		this.racePhenotype = racePhenotype;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

}
