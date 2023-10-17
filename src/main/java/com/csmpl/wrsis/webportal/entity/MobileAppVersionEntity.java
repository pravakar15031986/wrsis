package com.csmpl.wrsis.webportal.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_mob_app_version")

/*
 * This entity class is design for Setting and Updating for Mobile App version,
 * of WRSIS .
 * 
 * @author Shaktimaan Panda
 * @version 1.0
 * @since 31-07-2020
 */

public class MobileAppVersionEntity {

	@Id
	@Column(name = "int_mob_app_ver_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int appVersionId;

	@Column(name = "int_os_type_id")
	private int osTypeId;

	@Column(name = "vch_os_type")
	private String ostype;

	@Column(name = "vch_ver_no")
	private String versionNo;

	@Column(name = "bit_mandatory_status")
	private boolean mandatorybitstatus;

	@Column(name = "bitstatus")
	private boolean bitstatus;

	@Column(name = "dtmcreatedon")
	private Timestamp createOn;

	public int getAppVersionId() {
		return appVersionId;
	}

	public void setAppVersionId(int appVersionId) {
		this.appVersionId = appVersionId;
	}

	public int getOsTypeId() {
		return osTypeId;
	}

	public void setOsTypeId(int osTypeId) {
		this.osTypeId = osTypeId;
	}

	public String getOstype() {
		return ostype;
	}

	public void setOstype(String ostype) {
		this.ostype = ostype;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public boolean isMandatorybitstatus() {
		return mandatorybitstatus;
	}

	public void setMandatorybitstatus(boolean mandatorybitstatus) {
		this.mandatorybitstatus = mandatorybitstatus;
	}

	public boolean isBitstatus() {
		return bitstatus;
	}

	public void setBitstatus(boolean bitstatus) {
		this.bitstatus = bitstatus;
	}

	public Timestamp getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Timestamp createOn) {
		this.createOn = createOn;
	}

	@Override
	public String toString() {
		return "MobileAppVersion [appVersionId=" + appVersionId + ", osTypeId=" + osTypeId + ", ostype=" + ostype
				+ ", versionNo=" + versionNo + ", mandatorybitstatus=" + mandatorybitstatus + ", bitstatus=" + bitstatus
				+ ", createOn=" + createOn + "]";
	}

}
