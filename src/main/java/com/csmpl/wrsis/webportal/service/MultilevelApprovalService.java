package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.AuthorityBeans;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.ApprovalActionEntity;
import com.csmpl.wrsis.webportal.entity.ProcessEntity;

public interface MultilevelApprovalService {

	List<ProcessEntity> findAllProcess();

	List<ApprovalActionEntity> viewActionList();

	String storeMultileveApprovalAuthority(AuthorityBeans approvalObj, String jsonString);

	String viewApprovalAuthorityDetails(Integer processId);

	List<UserBean> getUsersByRoleId(Integer roleId);

}
