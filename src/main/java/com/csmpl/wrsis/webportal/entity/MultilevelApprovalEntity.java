package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_approval_authority")
public class MultilevelApprovalEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_aprov_auth_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer multiapprovalId;

	@Column(name = "int_approval_process_id")
	private Integer processId;

	@Column(name = "int_stage_no")
	private Integer stageNo;

	@Column(name = "int_role_id")
	private Integer roleId;

	@Column(name = "int_primary_auth_id")
	private Integer authId;

	@Column(name = "int_time_line")
	private Integer timeLine;

	@Column(name = "vch_auth_action_ids")
	private String actionIds;

	@Column(name = "int_delegate_status")
	private Boolean delegateStatus;

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

	public Integer getMultiapprovalId() {
		return multiapprovalId;
	}

	public void setMultiapprovalId(Integer multiapprovalId) {
		this.multiapprovalId = multiapprovalId;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getStageNo() {
		return stageNo;
	}

	public void setStageNo(Integer stageNo) {
		this.stageNo = stageNo;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getAuthId() {
		return authId;
	}

	public void setAuthId(Integer authId) {
		this.authId = authId;
	}

	public Integer getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(Integer timeLine) {
		this.timeLine = timeLine;
	}

	public String getActionIds() {
		return actionIds;
	}

	public void setActionIds(String actionIds) {
		this.actionIds = actionIds;
	}

	

	public Boolean getDelegateStatus() {
		return delegateStatus;
	}

	public void setDelegateStatus(Boolean delegateStatus) {
		this.delegateStatus = delegateStatus;
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
