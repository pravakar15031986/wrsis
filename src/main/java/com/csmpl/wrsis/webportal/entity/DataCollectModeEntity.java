package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="m_wr_survey_data_collect_mode")
public class DataCollectModeEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="int_data_collect_mode_id")
	private int dataCollectModeId;
	
	@Column(name="vch_data_collect_mode")
	private String dataCollectMode;

	public int getDataCollectModeId() {
		return dataCollectModeId;
	}

	public void setDataCollectModeId(int dataCollectModeId) {
		this.dataCollectModeId = dataCollectModeId;
	}

	public String getDataCollectMode() {
		return dataCollectMode;
	}

	public void setDataCollectMode(String dataCollectMode) {
		this.dataCollectMode = dataCollectMode;
	}

	
	
	
	
}
