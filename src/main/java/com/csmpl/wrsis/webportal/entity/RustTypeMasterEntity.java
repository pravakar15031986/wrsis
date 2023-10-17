package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_rust_type")
public class RustTypeMasterEntity {
	
	@Id
	@Column(name = "int_rust_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer intRustTypeId;
	
	@Column(name = "vch_rust_type")
	private String vchRustType;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	 
	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getIntRustTypeId() {
		return intRustTypeId;
	}

	public void setIntRustTypeId(Integer intRustTypeId) {
		this.intRustTypeId = intRustTypeId;
	}

	public String getVchRustType() {
		return vchRustType;
	}

	public void setVchRustType(String vchRustType) {
		this.vchRustType = vchRustType;
	}

	

	 
	 
	
	
	

}
