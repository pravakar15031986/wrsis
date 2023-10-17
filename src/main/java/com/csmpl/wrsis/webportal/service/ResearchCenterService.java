package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.csmpl.wrsis.webportal.bean.ResearchCenterHelperBean;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;

public interface ResearchCenterService {

	String saveAndUpdate(ResearchCenterHelperBean rCenterHelperBean);

	ResearchCenterHelperBean getByResearchCenterId(Integer researchCenterBeanId);

	String viewResearchCenterByPage(Integer pageSize, Integer pageNumber, Pageable pageable);

	String searchResearchCenterByPage(Integer pageSize, Integer pageNumber, Pageable pageable, String name,
			String status, Integer regionId, Integer zoneId);

	String searchResearchCenterRegionAndZoneByPage(Integer pageSize, Integer pageNumber, Pageable pageable,
			String regionId, String zoneId);

	List<ResearchCenter> getResearchCenterList();

	List<ResearchCenter> getResearchCenterListByRustType(Integer rustTypeId);

	boolean checkDuplicateRCName(Integer researchCenterBeanId, String rcName);

	List<ResearchCenter> getResearchCenterListOrder();
}
