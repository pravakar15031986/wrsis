package com.csmpl.wrsis.webportal.service;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.DemographicLocationTagBean;
public interface DemographicTaggingService {

	String getUserNameByRoleIdAndOrganizationId(Integer roleId,Integer orgId);
	String serchLocationDetailsByUserId(Integer userId);
	String viewDemographyLocationTagByPage(Integer pageSize,Integer pageNumber,Pageable pageable);
	String saveAndUpdateTaggingLocationWithLevelId(DemographicLocationTagBean tag);
	DemographicLocationTagBean getTaggingDetailsById(Integer locationTagId);
	String searchDemographyLocationTagByPage(Integer pageSize,Integer pageNumber,Pageable pageable,Integer orgName,String Status);
	String getUserNameByRoleIdAndOrganizationIdSearch(Integer roleId,Integer orgId);
}
