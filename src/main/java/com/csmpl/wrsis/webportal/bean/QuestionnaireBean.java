package com.csmpl.wrsis.webportal.bean;

import java.util.List;

public class QuestionnaireBean {

	private int qustionBeanId;

	private String qustionBean;

	private int qustionTypeBean;

	private int qustionOrderBean;

	private int qustionOptionCountBean;

	private boolean statusBean;

	private int createdByBean;

	private int optionIdBean;

	private int optionNumberBean;

	private String optionValueBean;

	private String optionInfoBean;

	private List<String> optionList;

	private List<String> inOptionValue;

	private List<String> inOptionInfo;

	private String[] x;
	private String[] y;

	private String optionBeanListString;

	private List<OptionBean> optionBeanList;

	public String getOptionBeanListString() {
		return optionBeanListString;
	}

	public void setOptionBeanListString(String optionBeanListString) {
		this.optionBeanListString = optionBeanListString;
	}

	public int getQustionBeanId() {
		return qustionBeanId;
	}

	public void setQustionBeanId(int qustionBeanId) {
		this.qustionBeanId = qustionBeanId;
	}

	public String getQustionBean() {
		return qustionBean;
	}

	public void setQustionBean(String qustionBean) {
		this.qustionBean = qustionBean;
	}

	public int getQustionTypeBean() {
		return qustionTypeBean;
	}

	public void setQustionTypeBean(int qustionTypeBean) {
		this.qustionTypeBean = qustionTypeBean;
	}

	public int getQustionOrderBean() {
		return qustionOrderBean;
	}

	public void setQustionOrderBean(int qustionOrderBean) {
		this.qustionOrderBean = qustionOrderBean;
	}

	public int getQustionOptionCountBean() {
		return qustionOptionCountBean;
	}

	public void setQustionOptionCountBean(int qustionOptionCountBean) {
		this.qustionOptionCountBean = qustionOptionCountBean;
	}

	public boolean isStatusBean() {
		return statusBean;
	}

	public void setStatusBean(boolean statusBean) {
		this.statusBean = statusBean;
	}

	public int getCreatedByBean() {
		return createdByBean;
	}

	public void setCreatedByBean(int createdByBean) {
		this.createdByBean = createdByBean;
	}

	public int getOptionIdBean() {
		return optionIdBean;
	}

	public void setOptionIdBean(int optionIdBean) {
		this.optionIdBean = optionIdBean;
	}

	public int getOptionNumberBean() {
		return optionNumberBean;
	}

	public void setOptionNumberBean(int optionNumberBean) {
		this.optionNumberBean = optionNumberBean;
	}

	public String getOptionValueBean() {
		return optionValueBean;
	}

	public void setOptionValueBean(String optionValueBean) {
		this.optionValueBean = optionValueBean;
	}

	public String getOptionInfoBean() {
		return optionInfoBean;
	}

	public void setOptionInfoBean(String optionInfoBean) {
		this.optionInfoBean = optionInfoBean;
	}

	public List<String> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}

	public List<String> getInOptionValue() {
		return inOptionValue;
	}

	public void setInOptionValue(List<String> inOptionValue) {
		this.inOptionValue = inOptionValue;
	}

	public List<String> getInOptionInfo() {
		return inOptionInfo;
	}

	public void setInOptionInfo(List<String> inOptionInfo) {
		this.inOptionInfo = inOptionInfo;
	}

	public String[] getX() {
		return x;
	}

	public void setX(String[] x) {
		this.x = x;
	}

	public String[] getY() {
		return y;
	}

	public void setY(String[] y) {
		this.y = y;
	}

	public List<OptionBean> getOptionBeanList() {
		return optionBeanList;
	}

	public void setOptionBeanList(List<OptionBean> optionBeanList) {
		this.optionBeanList = optionBeanList;
	}

}
