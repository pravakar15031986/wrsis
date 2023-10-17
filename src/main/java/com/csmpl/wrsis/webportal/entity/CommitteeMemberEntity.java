package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_committee_member")
public class CommitteeMemberEntity {
	
	@Id
	@Column(name="int_committee_member_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int committeeMemberId;
	
	@Column(name="int_committee_id")
	private int committeeId;
	
	@Column(name="int_user_id")
	private int userId;

	@Column(name="bitstatus")
	private boolean status;
	
	@Column(name="intcreatedby")
	private int createdBy;
	
	@Column(name="dtmcreatedon")
	private Date createdOn;
	
	@Column(name="intupdatedby")
	private int updatedBy;
	
	@Column(name="dtmupdatedon")
	private Date updatedOn;

	public int getCommitteeMemberId() {
		return committeeMemberId;
	}

	public void setCommitteeMemberId(int committeeMemberId) {
		this.committeeMemberId = committeeMemberId;
	}

	public int getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(int committeeId) {
		this.committeeId = committeeId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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
