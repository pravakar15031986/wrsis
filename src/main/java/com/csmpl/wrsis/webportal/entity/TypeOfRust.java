package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name="m_wr_rust_type")
public class TypeOfRust {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_rust_type_id")
	private int rustId;
	
	@Column(name="vch_rust_type")
	private String typeOfRust;
	
	@Column(name="vch_rust_alias")
	private String shortName;
	
	@Column(name="vch_desc")
	private String rustDescription;
	
	@Column(name="bitstatus")
	private Boolean isDelete;
	
	@Column(name="intcreatedby")
	private Integer creadtedBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private Integer updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;
	
	
	public String getTypeOfRust() {
		return typeOfRust;
	}
	public void setTypeOfRust(String typeOfRust) {
		this.typeOfRust = typeOfRust;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getRustDescription() {
		return rustDescription;
	}
	public void setRustDescription(String rustDescription) {
		this.rustDescription = rustDescription;
	}
	public Integer getCreadtedBy() {
		return creadtedBy;
	}
	public void setCreadtedBy(Integer creadtedBy) {
		this.creadtedBy = creadtedBy;
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
	public int getRustId() {
		return rustId;
	}
	public void setRustId(int rustId) {
		this.rustId = rustId;
	}
	public Boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
