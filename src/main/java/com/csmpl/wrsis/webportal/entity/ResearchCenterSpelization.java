package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_research_center_spelization")
public class ResearchCenterSpelization {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_center_spelize_id")
	private int centerSpelizeId;
    
	@ManyToOne
	@JoinColumn(name="int_research_center_id")
	private ResearchCenter researchCenterId;
	
	@ManyToOne
	@JoinColumn(name="int_spelization_id")
    private TypeOfRust spelizationId;
    
    @Column(name="bitstatus")
    private boolean status;
    @Column(name="intcreatedby")
    private int creaatedBy;
    @Column(name="dtmcreatedon")
    private Date createdOn;
    @Column(name="intupdatedby")
    private int updatedBy;
    @Column(name="dtmupdatedon")
    private Date updatedOn;
	public int getCenterSpelizeId() {
		return centerSpelizeId;
	}
	public void setCenterSpelizeId(int centerSpelizeId) {
		this.centerSpelizeId = centerSpelizeId;
	}
	
	public boolean isStatus() {
		return status;
	}
	public ResearchCenter getResearchCenterId() {
		return researchCenterId;
	}
	public void setResearchCenterId(ResearchCenter researchCenterId) {
		this.researchCenterId = researchCenterId;
	}
	public TypeOfRust getSpelizationId() {
		return spelizationId;
	}
	public void setSpelizationId(TypeOfRust spelizationId) {
		this.spelizationId = spelizationId;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public int getCreaatedBy() {
		return creaatedBy;
	}
	public void setCreaatedBy(int creaatedBy) {
		this.creaatedBy = creaatedBy;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
    
    
}
