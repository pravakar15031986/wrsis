package com.csmpl.wrsis.webportal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_wr_user_location_details")
public class UserLocationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "int_loc_id")
	private int userLocationId;
	
	@ManyToOne
    @JoinColumn(name = "int_user_loc_tag_id")
    private DemographicLocationTagEntity locationTagId;
   
	@ManyToOne
	@JoinColumn(name = "int_demography_id")
	 private DemographicEntity demographyId;
    
	
	@Column(name = "bitstatus")
	private boolean status;


	public int getUserLocationId() {
		return userLocationId;
	}


	public void setUserLocationId(int userLocationId) {
		this.userLocationId = userLocationId;
	}


	public DemographicLocationTagEntity getLocationTagId() {
		return locationTagId;
	}


	public void setLocationTagId(DemographicLocationTagEntity locationTagId) {
		this.locationTagId = locationTagId;
	}


	public DemographicEntity getDemographyId() {
		return demographyId;
	}


	public void setDemographyId(DemographicEntity demographyId) {
		this.demographyId = demographyId;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
}
