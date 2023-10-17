package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;

public class DelegationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int approvalProcessId;
	private int organisationId;
	private int approvalTo;
	private String datefrom;
	private String dateto;
	private String description;
	private int multiapprovalId;
	private int levelApproval;
	private int userId;
	private int delegationId;
	private int stageNo;
	private String processName;
	private String approveBy;
	private String approveTo;
	private String bitStatus;
	private String remarks;
	private int processId;

	private String activeStatus;

	public String getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}

	public int getApprovalProcessId() {
		return approvalProcessId;
	}

	public void setApprovalProcessId(int approvalProcessId) {
		this.approvalProcessId = approvalProcessId;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public int getApprovalTo() {
		return approvalTo;
	}

	public void setApprovalTo(int approvalTo) {
		this.approvalTo = approvalTo;
	}

	public String getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}

	public String getDateto() {
		return dateto;
	}

	public void setDateto(String dateto) {
		this.dateto = dateto;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMultiapprovalId() {
		return multiapprovalId;
	}

	public void setMultiapprovalId(int multiapprovalId) {
		this.multiapprovalId = multiapprovalId;
	}

	public int getLevelApproval() {
		return levelApproval;
	}

	public void setLevelApproval(int levelApproval) {
		this.levelApproval = levelApproval;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getDelegationId() {
		return delegationId;
	}

	public void setDelegationId(int delegationId) {
		this.delegationId = delegationId;
	}

	public int getStageNo() {
		return stageNo;
	}

	public void setStageNo(int stageNo) {
		this.stageNo = stageNo;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public String getApproveTo() {
		return approveTo;
	}

	public void setApproveTo(String approveTo) {
		this.approveTo = approveTo;
	}

	public String getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(String bitStatus) {
		this.bitStatus = bitStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

}
