package com.csmpl.wrsis.webportal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_forecasting_style_details")
public class GisMapStyleEntity {
	
	@Id
	@Column(name = "int_style_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer styleId;
	
	@Column(name = "vch_style_name")
	private String styleName;
	
	@Column(name = "vch_style_range")
	private String styleRange;
	
	@Column(name = "bitstatus")
	private boolean bitStatus;

	@Column(name = "dtm_created_on")
	public Date Createdon;

	public Integer getStyleId() {
		return styleId;
	}

	public void setStyleId(Integer styleId) {
		this.styleId = styleId;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getStyleRange() {
		return styleRange;
	}

	public void setStyleRange(String styleRange) {
		this.styleRange = styleRange;
	}

	public boolean isBitStatus() {
		return bitStatus;
	}

	public void setBitStatus(boolean bitStatus) {
		this.bitStatus = bitStatus;
	}

	public Date getCreatedon() {
		return Createdon;
	}

	public void setCreatedon(Date createdon) {
		Createdon = createdon;
	}
	
	

}
