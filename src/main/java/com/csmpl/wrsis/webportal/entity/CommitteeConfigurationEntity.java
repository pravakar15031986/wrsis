package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="m_wr_committee")
public class CommitteeConfigurationEntity {
	
	@Id
	@Column(name="int_committee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int committeeId;
	
	@Column(name="vch_committee_name")
	private String committeName;
	
	@Column(name="int_role_id")
	private int roleId;
	
	@Column(name="vch_desc")
	private String desc;

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

	
	
	
	public int getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(int committeeId) {
		this.committeeId = committeeId;
	}

	public String getCommitteName() {
		return committeName;
	}

	public void setCommitteName(String committeName) {
		this.committeName = committeName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	@Override
	public String toString() {
		return "CommitteeConfigurationEntity [committeeId=" + committeeId + ", committeName=" + committeName
				+ ", roleId=" + roleId + ", desc=" + desc + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + "]";
		
		
	}
	
}
