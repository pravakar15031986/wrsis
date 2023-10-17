package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.wrsis.webportal.bean.DelegationBean;
import com.csmpl.wrsis.webportal.bean.MultilevelApprovalBean;
import com.csmpl.wrsis.webportal.bean.ProcessBean;
import com.csmpl.wrsis.webportal.entity.JobDelegationEntity;

public interface JobDelegationService {

	List<ProcessBean> findProcessByUserId(int userId);

	List<MultilevelApprovalBean> getAuthorityStatus(Integer processId,int userId);
	List<MultilevelApprovalBean> getAuthorityStatusEdit(Integer processId,int userId);

	List<User> viewUserList(int userId);

	String saveDelegation(DelegationBean delegationBean);

	List<DelegationBean> viewJobDeligation(DelegationBean delegationBean);

	DelegationBean editJobDeligation(DelegationBean delegationBean);

	List<JobDelegationEntity> getAllActiveDelegations();

	int removeJobDelegations();

	int addJobDelegations();

	String deleteJobDeligation(Integer delegationId);

	List<User> viewUserListByOrgId(int organisationId);


}
