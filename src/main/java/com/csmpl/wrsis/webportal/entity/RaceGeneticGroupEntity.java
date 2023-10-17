package com.csmpl.wrsis.webportal.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_race_genetic_group")
public class RaceGeneticGroupEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "int_genetic_group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer geneticGroupId;

	@Column(name = "vch_group_name")
	private String groupName;

	@Column(name = "vch_desc")
	private String descr;

	@Column(name = "bitstatus")
	private Boolean status;

	public Integer getGeneticGroupId() {
		return geneticGroupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getDescr() {
		return descr;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setGeneticGroupId(Integer geneticGroupId) {
		this.geneticGroupId = geneticGroupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

}
