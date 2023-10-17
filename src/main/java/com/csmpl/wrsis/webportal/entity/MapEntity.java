package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_wr_map_type")
public class MapEntity {

	@Id
	@Column(name = "int_map_type_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer mapTypeId;

	@Column(name = "vch_map_type")
	private String mapName;

	@Column(name = "bitstatus")
	private Boolean status;

	public Integer getMapTypeId() {
		return mapTypeId;
	}

	public void setMapTypeId(Integer mapTypeId) {
		this.mapTypeId = mapTypeId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

}
