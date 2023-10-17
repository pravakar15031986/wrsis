package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_delegation")
public class JobDelegationEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "int_delegation_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer delegationId;

	@Column(name = "int_aprov_auth_id")
	private Integer multiapprovalId;

	@Column(name = "int_delegate_to")
	private Integer delegateTo;

	@Column(name = "dtm_from_date")
	private Date datefrom;

	@Column(name = "dtm_to_date")
	private Date dateto;

	@Column(name = "vch_remarks")
	private String description;

	@Column(name = "bitstatus")
	private Boolean bitStatus;

	@Column(name = "intcreatedby")
	private Integer createdBy;

	@Column(name = "dtmcreatedon")
	private Timestamp createdOn;

	@Column(name = "intupdatedby")
	private Integer updatedBy;

	@Column(name = "dtmupdatedon")
	private Timestamp updatedOn;
	
	@Column(name = "bit_active_status")
	private Boolean activeStatus;

	
	

	public Boolean getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Boolean activeStatus) {
		this.activeStatus = activeStatus;
	}

	public Integer getDelegationId() {
		return delegationId;
	}

	public void setDelegationId(Integer delegationId) {
		this.delegationId = delegationId;
	}

	public Integer getMultiapprovalId() {
		return multiapprovalId;
	}

	public void setMultiapprovalId(Integer multiapprovalId) {
		this.multiapprovalId = multiapprovalId;
	}

	public Integer getDelegateTo() {
		return delegateTo;
	}

	public void setDelegateTo(Integer delegateTo) {
		this.delegateTo = delegateTo;
	}

	public Date getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(Date datefrom) {
		this.datefrom = datefrom;
	}

	public Date getDateto() {
		return dateto;
	}

	public void setDateto(Date dateto) {
		this.dateto = dateto;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(Boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

}
