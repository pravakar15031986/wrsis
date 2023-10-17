package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;

public interface LoginAuditTrailService {



	List<IPTrackBean> viewSucessWebLoginDetails(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName,Integer userId,String logStatus, Integer start, Integer length);

	Integer viewSucessWebLoginDetailsCount(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName,Integer userId,String logStatus,Integer pstart, Integer plength);

	List<IPTrackBean> viewSucessMobLoginDetails(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName,Integer userId,String logStatus, Integer start, Integer length);

	Integer viewSucessMobLoginDetailsCount(Integer organizationId, Integer roleId, Integer researchCenterId,
			Integer intdesigid, String startDate, String endDate, String fullName,Integer userId,String logStatus,Integer pstart, Integer plength);

}
