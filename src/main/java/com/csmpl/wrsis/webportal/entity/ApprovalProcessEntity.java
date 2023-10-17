package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="m_wr_approval_process")

public class ApprovalProcessEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="int_approval_process_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int intProcessId;
	
	@Column(name="vch_process")
	private String vchProcessName;
	

	@Column(name="vch_desc")
	private String vchDescription;
	
	@Column(name="intcreatedby")
	private int intCreatedBy;
	
	
	
	@Column(name="dtmcreatedon")
	private Date dtmCreatedOn;
	
	@Column(name="intupdatedby")
	private int intUpdatedBy;
	
	@Column(name="dtmupdatedon")
	private Date dtmUpdatedOn;
	
	@Column(name="bitstatus")
	private boolean intDeletedFlag;
	
	@Transient
	private int userId;
	
	

	

	public int getIntProcessId() {
		return intProcessId;
	}

	public void setIntProcessId(int intProcessId) {
		this.intProcessId = intProcessId;
	}

	public String getVchProcessName() {
		return vchProcessName;
	}

	public void setVchProcessName(String vchProcessName) {
		this.vchProcessName = vchProcessName;
	}

	public String getVchDescription() {
		return vchDescription;
	}

	public void setVchDescription(String vchDescription) {
		this.vchDescription = vchDescription;
	}

	public int getIntCreatedBy() {
		return intCreatedBy;
	}

	public void setIntCreatedBy(int intCreatedBy) {
		this.intCreatedBy = intCreatedBy;
	}

	public Date getDtmCreatedOn() {
		return dtmCreatedOn;
	}

	public void setDtmCreatedOn(Date dtmCreatedOn) {
		this.dtmCreatedOn = dtmCreatedOn;
	}

	public int getIntUpdatedBy() {
		return intUpdatedBy;
	}

	public void setIntUpdatedBy(int intUpdatedBy) {
		this.intUpdatedBy = intUpdatedBy;
	}

	public Date getDtmUpdatedOn() {
		return dtmUpdatedOn;
	}

	public void setDtmUpdatedOn(Date dtmUpdatedOn) {
		this.dtmUpdatedOn = dtmUpdatedOn;
	}

	public boolean getIntDeletedFlag() {
		return intDeletedFlag;
	}

	public void setIntDeletedFlag(boolean intDeletedFlag) {
		this.intDeletedFlag = intDeletedFlag;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	
	
}
