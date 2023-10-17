package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.ApprovalProcessBean;
import com.csmpl.wrsis.webportal.entity.ApprovalProcessEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalProcessRepository;

@Service("ApprovalProcessService")
public class ApprovalProcessServiceImpl implements com.csmpl.wrsis.webportal.service.ApprovalProcessService {

	public static final Logger LOG = LoggerFactory.getLogger(ApprovalProcessServiceImpl.class);

	@Autowired
	@Qualifier("ApprovalProcessRepository")
	ApprovalProcessRepository ApprovalProcessRepository;

	@Override
	public String addApprovalProcessServices(ApprovalProcessBean approvalProcessBean) {
		String returnType = WrsisPortalConstant.SUCCESS;

		ApprovalProcessEntity approvalObj = new ApprovalProcessEntity();
		try {
			List<Object[]> datalist = ApprovalProcessRepository
					.isProcessNameDataExist(approvalProcessBean.getTxtProcessName().toUpperCase());
			if (!datalist.isEmpty()) {
				return "processexist";
			}
			approvalObj.setVchProcessName(approvalProcessBean.getTxtProcessName().toUpperCase());
			approvalObj.setVchDescription(approvalProcessBean.getDesc());
			approvalObj.setDtmCreatedOn(new Date());
			approvalObj.setDtmUpdatedOn(new Date());
			approvalObj.setIntCreatedBy(approvalProcessBean.getUserId());
			approvalObj.setIntUpdatedBy(approvalProcessBean.getUserId());
			approvalObj.setIntDeletedFlag(approvalProcessBean.getIntDeletedFlag());
			

			ApprovalProcessRepository.saveAndFlush(approvalObj);
			returnType = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			/*			
			*/ returnType = WrsisPortalConstant.FAILURE;
			LOG.error("ApprovalProcessServiceImpl::addApprovalProcessServices():" + e);
		}

		return returnType;

	}

	@Override
	public Page<ApprovalProcessEntity> getApprovalProcess(Pageable pageable) {
		Page<ApprovalProcessEntity> list = null;
		try {
			list = ApprovalProcessRepository.findAll(pageable);

		} catch (Exception e) {
			LOG.error("ApprovalProcessServiceImpl::getApprovalProcess():" + e);
		}
		return list;
	}

	@Override
	public ApprovalProcessEntity editApprovalProcess(int intProcessId) {
		ApprovalProcessEntity vo = null;
		try {
			vo = ApprovalProcessRepository.editApproval(intProcessId);
		} catch (Exception e) {
			LOG.error("ApprovalProcessServiceImpl::editApprovalProcess():" + e);
		}
		return vo;

	}

	@Override
	public String updateApprovalProcess(ApprovalProcessBean approvalProcessBean) {
		String retStatus = null;

		try {
			List<Object[]> datalist = ApprovalProcessRepository.isProcessNameDataExistOnupdate(
					approvalProcessBean.getIntProcessId(), approvalProcessBean.getTxtProcessName().toUpperCase());
			if (!datalist.isEmpty()) {
				return "processexist";
			}
			if (approvalProcessBean.getIntDeletedFlag()) {

				Integer count = ApprovalProcessRepository
						.isProcessNameConfigureToUser(approvalProcessBean.getIntProcessId());
				if (count != 0) {
					return "configureprocess";
				}
			}

			ApprovalProcessEntity dto = ApprovalProcessRepository.getOne(approvalProcessBean.getIntProcessId());
			dto.setVchProcessName(approvalProcessBean.getTxtProcessName().toUpperCase());
			dto.setVchDescription(approvalProcessBean.getDesc());
			dto.setIntDeletedFlag(approvalProcessBean.getIntDeletedFlag());
			dto.setIntUpdatedBy(approvalProcessBean.getUserId());
			dto.setDtmUpdatedOn(new Date());

			ApprovalProcessRepository.save(dto);
			retStatus = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			LOG.error("ApprovalProcessServiceImpl::updateApprovalProcess():" + e);
			retStatus = WrsisPortalConstant.FAILURE;
		}
		return retStatus;
	}

	@Override
	public List<ApprovalProcessEntity> retriveApprovalProcess() {
		List<ApprovalProcessEntity> list = null;
		try {
			list = ApprovalProcessRepository.findAll();

		} catch (Exception e) {
			LOG.error("ApprovalProcessServiceImpl::retriveApprovalProcess():" + e);
		}
		return list;
	}

	@Override
	public List<ApprovalProcessBean> viewApprovalProcess(SearchVo searchVo) {
		List<ApprovalProcessBean> approvalProcessList = new ArrayList<>();
		ApprovalProcessBean vo = null;
		boolean condition = false;
		String appProcessName = searchVo.getApprovalProcessName();
		if (appProcessName == null) {
			appProcessName = "";
		}
		List<Object[]> obj = null;
		try {

			if (searchVo.getDataId() != 0) {
				if (searchVo.getDataId() == 1) {
					condition = false;
				} else if (searchVo.getDataId() == 2) {
					condition = true;
				}
				obj = ApprovalProcessRepository.viewApprovalProcessList(condition, appProcessName.toUpperCase());
			} else {
				obj = ApprovalProcessRepository.viewApprovalProcessList(appProcessName.toUpperCase());
			}
			if (!obj.isEmpty()) {
				for (Object[] dto : obj) {
					vo = new ApprovalProcessBean();
					vo.setIntProcessId((Short) dto[0]);
					vo.setTxtProcessName(String.valueOf(dto[1]));
					vo.setDesc(String.valueOf(dto[2]));
					vo.setIntDeletedFlag((Boolean) dto[3]);
					approvalProcessList.add(vo);
				}
				LOG.info("view List size of Variety " + approvalProcessList.size());
			}
		} catch (Exception e) {
			LOG.error("ApprovalProcessServiceImpl::viewApprovalProcess():" + e);
		}
		return approvalProcessList;
	}

}
