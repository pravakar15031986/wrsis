package com.csmpl.wrsis.webportal.bean;

import java.io.Serializable;

public class ApprovalProcessBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6157340430045896911L;
	private String txtProcessName;
	private String vchProcessName;
	private String desc;
	private boolean intDeletedFlag;

	private int userId;
	private int intProcessId;
	private int role_id;

	private int intApplicationId;

	private String alias_name;

	public int getIntApplicationId() {
		return intApplicationId;
	}

	public void setIntApplicationId(int intApplicationId) {
		this.intApplicationId = intApplicationId;
	}

	public String getTxtProcessName() {
		return txtProcessName;
	}

	public void setTxtProcessName(String txtProcessName) {
		this.txtProcessName = txtProcessName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public int getRole_id() {
		return role_id;
	}

	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}

	public String getAlias_name() {
		return alias_name;
	}

	public void setAlias_name(String alias_name) {
		this.alias_name = alias_name;
	}

}
