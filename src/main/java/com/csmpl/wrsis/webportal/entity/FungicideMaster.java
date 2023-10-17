package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="m_wr_fungicide_type")
public class FungicideMaster {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_fungicide_type_id")
	private int fungicideId;
	
	@Column(name="vch_fungicide")
	private String fungicideName;
	@Column(name="vch_fungicide_alias")
	private String fungicideAliasName;
	@Column(name="vch_desc")
	private String description;
	@Column(name="bitstatus")
	private boolean status;
	@Column(name="intcreatedby")
	private Integer createdBy;
    @Column(name="dtmcreatedon") 
    private Date createdOn;
    @Column(name="intupdatedby") 
    private Integer updatedBy;
    @Column(name="dtmupdatedon")
	private Date updatedOn;
    @Column(name="vch_quantity")
    private String quantity;
    
	public String getFungicideName() {
		return fungicideName;
	}
	public void setFungicideName(String fungicideName) {
		this.fungicideName = fungicideName;
	}
	public String getFungicideAliasName() {
		return fungicideAliasName;
	}
	public void setFungicideAliasName(String fungicideAliasName) {
		this.fungicideAliasName = fungicideAliasName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public int getFungicideId() {
		return fungicideId;
	}
	public void setFungicideId(int fungicideId) {
		this.fungicideId = fungicideId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
}
