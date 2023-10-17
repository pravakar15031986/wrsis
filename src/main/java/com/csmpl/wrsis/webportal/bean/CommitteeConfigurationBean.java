package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CommitteeConfigurationBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String committeeName;
	private String committeeRole;
	private int committeeRoleId;
	private String orgName;
	private String userRole;
	private int roleId;
	private List<Integer> selectedUsers;
	private String committeeDesc;
	private int committeeId;

	private int userId;
	private String userName;
	private String password;
	private String fullName;
	private String photoFileName;
	private String photoFilePath;
	private String email;
	private String mobile;
	private char firstLogin;
	private List<Integer> groupId;
	private int intLevelDetailId;
	private int createdBy;
	private Date createdOn;
	private Date updatedOn;
	private int updatedBy;
	private String bitStatus;
	private char loginFailAttempt;
	private boolean mapStatus;
	private int researchCenterId;
	private String designation;
	private String roleName;
	private int memUserTypeId;
	private int intdesigid;
	private List<Integer> selectedRoleList;

	private String organizationName;

	private String rcName;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getCommitteeName() {
		return committeeName;
	}

	public void setCommitteeName(String committeeName) {
		this.committeeName = committeeName;
	}

	public String getCommitteeRole() {
		return committeeRole;
	}

	public void setCommitteeRole(String committeeRole) {
		this.committeeRole = committeeRole;
	}

	public int getCommitteeRoleId() {
		return committeeRoleId;
	}

	public void setCommitteeRoleId(int committeeRoleId) {
		this.committeeRoleId = committeeRoleId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getCommitteeDesc() {
		return committeeDesc;
	}

	public void setCommitteeDesc(String committeeDesc) {
		this.committeeDesc = committeeDesc;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhotoFileName() {
		return photoFileName;
	}

	public void setPhotoFileName(String photoFileName) {
		this.photoFileName = photoFileName;
	}

	public String getPhotoFilePath() {
		return photoFilePath;
	}

	public void setPhotoFilePath(String photoFilePath) {
		this.photoFilePath = photoFilePath;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public char getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(char firstLogin) {
		this.firstLogin = firstLogin;
	}

	public List<Integer> getGroupId() {
		return groupId;
	}

	public void setGroupId(List<Integer> groupId) {
		this.groupId = groupId;
	}

	public int getIntLevelDetailId() {
		return intLevelDetailId;
	}

	public void setIntLevelDetailId(int intLevelDetailId) {
		this.intLevelDetailId = intLevelDetailId;
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

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(String bitStatus) {
		this.bitStatus = bitStatus;
	}

	public char getLoginFailAttempt() {
		return loginFailAttempt;
	}

	public void setLoginFailAttempt(char loginFailAttempt) {
		this.loginFailAttempt = loginFailAttempt;
	}

	public boolean isMapStatus() {
		return mapStatus;
	}

	public void setMapStatus(boolean mapStatus) {
		this.mapStatus = mapStatus;
	}

	public int getResearchCenterId() {
		return researchCenterId;
	}

	public void setResearchCenterId(int researchCenterId) {
		this.researchCenterId = researchCenterId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getMemUserTypeId() {
		return memUserTypeId;
	}

	public void setMemUserTypeId(int memUserTypeId) {
		this.memUserTypeId = memUserTypeId;
	}

	public int getIntdesigid() {
		return intdesigid;
	}

	public void setIntdesigid(int intdesigid) {
		this.intdesigid = intdesigid;
	}

	public List<Integer> getSelectedRoleList() {
		return selectedRoleList;
	}

	public void setSelectedRoleList(List<Integer> selectedRoleList) {
		this.selectedRoleList = selectedRoleList;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRcName() {
		return rcName;
	}

	public void setRcName(String rcName) {
		this.rcName = rcName;
	}

	public List<Integer> getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(List<Integer> selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public int getCommitteeId() {
		return committeeId;
	}

	public void setCommitteeId(int committeeId) {
		this.committeeId = committeeId;
	}

}
