package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_cr_growth_stage")
public class GrowthMasterEntity {
	
	@Id
	@Column(name = "int_cr_gr_stage_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intCrGrStageId;
	
	@Column(name = "vch_stage")
	private String vchStage;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntCrGrStageId() {
		return intCrGrStageId;
	}

	public void setIntCrGrStageId(Integer intCrGrStageId) {
		this.intCrGrStageId = intCrGrStageId;
	}

	public String getVchStage() {
		return vchStage;
	}

	public void setVchStage(String vchStage) {
		this.vchStage = vchStage;
	}

	 
	 
	
	
	

}
