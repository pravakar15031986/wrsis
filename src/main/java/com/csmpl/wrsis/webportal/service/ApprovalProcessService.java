package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.ApprovalProcessBean;
import com.csmpl.wrsis.webportal.entity.ApprovalProcessEntity;



public interface ApprovalProcessService {
	
	String addApprovalProcessServices(ApprovalProcessBean approvalProcessBean);
	
	Page<ApprovalProcessEntity> getApprovalProcess(Pageable pageable);
	
	ApprovalProcessEntity editApprovalProcess(int intProcessId);
	
	String updateApprovalProcess(ApprovalProcessBean approvalProcessBean);

	List<ApprovalProcessEntity> retriveApprovalProcess();

	List<ApprovalProcessBean> viewApprovalProcess(SearchVo searchVo);



}
