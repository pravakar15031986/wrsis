package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.OrganizationMasterBean;

public interface OrganizationMasterService {

	String saveAndUpdate(OrganizationMasterBean orgDetails);

	List<OrganizationMasterBean> viewAllOrganisation();

	OrganizationMasterBean editOrganization(OrganizationMasterBean organization);

}
