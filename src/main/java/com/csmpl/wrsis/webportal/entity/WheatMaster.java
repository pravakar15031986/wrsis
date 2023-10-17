package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_wheat_type")
public class WheatMaster {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_wheat_type_id")
    private int wheatTypeId;
   
	@Column(name="vch_wheat_type")
    private String wheatName;
	public int getWheatTypeId() {
		return wheatTypeId;
	}
	public void setWheatTypeId(int wheatTypeId) {
		this.wheatTypeId = wheatTypeId;
	}
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
	
	public String getWheatName() {
		return wheatName;
	}
	public void setWheatName(String wheatName) {
		this.wheatName = wheatName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
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
    
    
}
