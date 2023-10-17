package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_rust_reaction")
public class ReactionMasterEntity {
	
	@Id
	@Column(name = "int_rust_reaction_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intRustReactionId;
	
	@Column(name = "int_rust_type_id")
	private Integer intRustTypeId;
	
	@Column(name = "vch_rust_reaction_name")
	private String vchDesc;
	
	public Integer getIntRustReactionId() {
		return intRustReactionId;
	}

	public void setIntRustReactionId(Integer intRustReactionId) {
		this.intRustReactionId = intRustReactionId;
	}

	public Integer getIntRustTypeId() {
		return intRustTypeId;
	}

	public void setIntRustTypeId(Integer intRustTypeId) {
		this.intRustTypeId = intRustTypeId;
	}

	public String getVchDesc() {
		return vchDesc;
	}

	public void setVchDesc(String vchDesc) {
		this.vchDesc = vchDesc;
	}

	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	

 
	 
	
	
	

}
