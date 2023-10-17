package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "m_wr_demography")
public class DemographicEntity {

	@Id
	@Column(name = "int_demography_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int demographyId;

	@NotEmpty
	@NotBlank
	@NotNull
	@Column(name = "vch_demography_name")
	private String demographyName;

	@Column(name = "int_level_id")
	private int levelId;

	@Column(name = "int_parent_id")
	private int parentId;

	@Column(name = "vch_alias")
	private String alias;

	@Column(name = "bitstatus")
	private boolean status;

	@Column(name = "intcreatedby")
	private int createdBy;

	@Column(name = "dtmcreatedon")
	private Date createOn;

	@Column(name = "intupdatedby")
	private int updateBy;

	@Column(name = "dtmupdatedon")
	private Date updateOn;

	@Column(name = "vch_desc")
	private String description;

	public int getDemographyId() {
		return demographyId;
	}

	public void setDemographyId(int demographyId) {
		this.demographyId = demographyId;
	}

	public String getDemographyName() {
		return demographyName;
	}

	public void setDemographyName(String demographyName) {
		this.demographyName = demographyName;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getStatus() {
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

	public int getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(int updateBy) {
		this.updateBy = updateBy;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public Date getUpdateOn() {
		return updateOn;
	}

	public void setUpdateOn(Date updateOn) {
		this.updateOn = updateOn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ManageDemographic [demographyId=" + demographyId + ", demographyName=" + demographyName + ", levelId="
				+ levelId + ", parentId=" + parentId + ", alias=" + alias + ", status=" + status + ", createdBy="
				+ createdBy + ", createOn=" + createOn + ", updateBy=" + updateBy + ", updateOn=" + updateOn
				+ ", description=" + description + "]";
	}

}
