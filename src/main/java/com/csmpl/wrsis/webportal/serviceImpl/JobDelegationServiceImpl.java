package com.csmpl.wrsis.webportal.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.DelegationBean;
import com.csmpl.wrsis.webportal.bean.MultilevelApprovalBean;
import com.csmpl.wrsis.webportal.bean.ProcessBean;
import com.csmpl.wrsis.webportal.entity.JobDelegationEntity;
import com.csmpl.wrsis.webportal.entity.MultilevelApprovalEntity;
import com.csmpl.wrsis.webportal.repository.JobDelegationRepository;
import com.csmpl.wrsis.webportal.repository.MultilevelApprovalRepository;
import com.csmpl.wrsis.webportal.service.JobDelegationService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Component
public class JobDelegationServiceImpl implements JobDelegationService {

	public static final Logger LOG = LoggerFactory.getLogger(JobDelegationServiceImpl.class);

	@Autowired
	JobDelegationRepository jobDelegationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MultilevelApprovalRepository multilevelApprovalRepository;

	@Override
	public List<ProcessBean> findProcessByUserId(int userId) {
		List<ProcessBean> list = new ArrayList<>();
		ProcessBean vo = null;
		try {
			List<Object[]> objList = jobDelegationRepository.findProcessByUserId(userId);
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new ProcessBean();
					vo.setProcessId((Short) (obj[0]));
					vo.setProcessName(String.valueOf(obj[1]));
					list.add(vo);
				}

			}

		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::findProcessByUserId():" + e);

		}
		return list;
	}

	@Override
	public List<MultilevelApprovalBean> getAuthorityStatus(Integer processId, int userId) {
		List<MultilevelApprovalBean> list = new ArrayList<>();
		MultilevelApprovalBean vo = null;
		try {
			List<Object[]> objList = jobDelegationRepository.getAuthorityStatus(processId, userId);
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new MultilevelApprovalBean();
					vo.setMultiapprovalId(Integer.valueOf((Short) (obj[0])));
					vo.setStageNo((Integer.valueOf((Short) (obj[1]))));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::getAuthorityStatus():" + e);

		}
		return list;

	}

	@Override
	public List<MultilevelApprovalBean> getAuthorityStatusEdit(Integer processId, int userId) {
		List<MultilevelApprovalBean> list = new ArrayList<>();
		MultilevelApprovalBean vo = null;
		try {
			List<Object[]> objList = jobDelegationRepository.getAuthorityStatusEdit(processId, userId);
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new MultilevelApprovalBean();
					vo.setMultiapprovalId(Integer.valueOf((Short) (obj[0])));
					vo.setStageNo((Integer.valueOf((Short) (obj[1]))));
					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::getAuthorityStatusEdit():" + e);

		}
		return list;

	}

	@Override
	public List<User> viewUserList(int userId) {

		return userRepository.viewUserList(userId);
	}

	@Override
	public String saveDelegation(DelegationBean delegationBean) {

		String result = "";
		JobDelegationEntity jobDelegation = null;
	

		try {

			if (delegationBean.getDelegationId() == 0) {
				jobDelegation = new JobDelegationEntity();
				jobDelegation.setMultiapprovalId(delegationBean.getMultiapprovalId());
				jobDelegation.setDelegateTo(delegationBean.getApprovalTo());
				jobDelegation.setDatefrom(DateUtil.StringMMMToDate(delegationBean.getDatefrom()));
				jobDelegation.setDateto(DateUtil.StringMMMToDate(delegationBean.getDateto()));
				jobDelegation.setDescription(delegationBean.getDescription());
				jobDelegation.setBitStatus(false);
				jobDelegation.setCreatedBy(delegationBean.getUserId());
				jobDelegation.setCreatedOn(new Timestamp(new Date().getTime()));
				jobDelegationRepository.saveAndFlush(jobDelegation);

				
				return WrsisPortalConstant.SUCCESS;
			} else {
				jobDelegation = jobDelegationRepository.findByDelegationId(delegationBean.getDelegationId());
				
				if (jobDelegation != null) {
					if(jobDelegation.getMultiapprovalId()!=delegationBean.getMultiapprovalId()) {
						
						List<Object[]> objList = jobDelegationRepository.isJobDelegationForProcess(delegationBean.getApprovalProcessId(), delegationBean.getUserId());
						if(objList!=null && !objList.isEmpty()) {
							
							return "update_exist";
						}
					}
					jobDelegation.setMultiapprovalId(delegationBean.getMultiapprovalId());
					jobDelegation.setDelegateTo(delegationBean.getApprovalTo());
					jobDelegation.setDatefrom(DateUtil.StringMMMToDate(delegationBean.getDatefrom()));
					jobDelegation.setDateto(DateUtil.StringMMMToDate(delegationBean.getDateto()));
					jobDelegation.setDescription(delegationBean.getDescription());
					jobDelegation.setBitStatus(false);
					jobDelegation.setUpdatedBy(delegationBean.getUserId());
					jobDelegation.setUpdatedOn(new Timestamp(new Date().getTime()));

					jobDelegationRepository.saveAndFlush(jobDelegation);

					return WrsisPortalConstant.UPDATE;
				}
			}

		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::saveDeligation():" + e);

		}

		return result;
	}

	@Override
	public List<DelegationBean> viewJobDeligation(DelegationBean delegationBean) {
		List<DelegationBean> list = new ArrayList<>();
		DelegationBean vo = null;
		try {
			List<Object[]> objList = jobDelegationRepository.viewJobDeligation(delegationBean.getUserId());
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new DelegationBean();
					vo.setProcessName(String.valueOf(obj[0]));
					vo.setStageNo(Integer.valueOf((Short) (obj[1])));
					vo.setDatefrom(DateUtil.DateToString((Date) (obj[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					vo.setDateto(DateUtil.DateToString((Date) (obj[3]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					vo.setApproveBy(String.valueOf(obj[4]));
					vo.setApproveTo(String.valueOf(obj[5]));
					vo.setRemarks(String.valueOf(obj[6]));
					vo.setBitStatus(String.valueOf(obj[7]));
					vo.setDelegationId((Integer) (obj[8]));
					vo.setMultiapprovalId(Integer.valueOf((Short) (obj[9])));
					if (obj[10] != null)
						vo.setActiveStatus(String.valueOf(obj[10]));
					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::viewJobDeligation():" + e);

		}
		return list;

	}

	

	@Override
	public DelegationBean editJobDeligation(DelegationBean delegationBean) {
		DelegationBean vo = null;
		try {
			List<Object[]> objList = jobDelegationRepository.editJobDeligation(delegationBean.getDelegationId(),
					delegationBean.getMultiapprovalId());
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new DelegationBean();
					vo.setDelegationId((Integer) (obj[0]));
					vo.setStageNo((Integer.valueOf((Short) (obj[1]))));
					vo.setDatefrom(DateUtil.DateToString((Date) (obj[2]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					vo.setDateto(DateUtil.DateToString((Date) (obj[3]), WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY));
					vo.setApprovalTo((Integer) (obj[4]));
					vo.setDescription(String.valueOf(obj[5]));
					vo.setBitStatus(String.valueOf(obj[6]));
					vo.setApprovalProcessId((Integer.valueOf((Short) (obj[7]))));
					vo.setOrganisationId(OrganisationId((Integer) (obj[4])));

				}
			}
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::editJobDeligation():" + e);
		}
		return vo;
	}

	private int OrganisationId(Integer approvalTo) {
		int orgId = 0;
		try {
			List<User> list = userRepository.getOrganizationIdByUserId(approvalTo);
			orgId = list.get(0).getIntLevelDetailId();
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::OrganisationId():" + e);

		}

		return orgId;
	}

	@Override
	public List<JobDelegationEntity> getAllActiveDelegations() {
		List<JobDelegationEntity> list = null;
		try {
			list = jobDelegationRepository.findByActiveStatus(true);
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::getallActivedeligations():" + e);
		}

		return list;
	}

	@Transactional
	@Override
	public int removeJobDelegations() {
		int retVal = 0;
		Date today = new Date();
		Timestamp date = new Timestamp(today.getTime());
		try {
			List<JobDelegationEntity> list = jobDelegationRepository.findByActiveStatus(true);
			for (JobDelegationEntity obj : list) {
				if (DateUtil.isDateLessThenCurrentDate(obj.getDateto())) {

					MultilevelApprovalEntity authObj = multilevelApprovalRepository
							.findByMultiapprovalIdAndBitStatus(obj.getMultiapprovalId(), false);
					authObj.setDelegateStatus(false);
					authObj.setUpdatedOn(date);
					multilevelApprovalRepository.save(authObj);

					obj.setActiveStatus(false);
					obj.setUpdatedOn(date);
					jobDelegationRepository.save(obj);

					retVal++;
				}
			}
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::removeJobDeligations():" + e);
		}
		return retVal;
	}

	@Override
	public int addJobDelegations() {
		int retVal = 0;
		Date today = new Date();
		Timestamp date = new Timestamp(today.getTime());
		List<JobDelegationEntity> list = jobDelegationRepository.findByActiveStatusAndBitStatus(null, false);
		for (JobDelegationEntity obj : list) {
			if (DateUtil.isDateEqualsToCurrentDateUsingFormat(obj.getDatefrom(),
					WrsisPortalConstant.DATE_FORMAT_YYYYMMDD)) {

				MultilevelApprovalEntity authObj = multilevelApprovalRepository
						.findByMultiapprovalIdAndBitStatus(obj.getMultiapprovalId(), false);
				authObj.setDelegateStatus(true);
				authObj.setUpdatedOn(date);
				multilevelApprovalRepository.save(authObj);

				obj.setActiveStatus(true);
				obj.setUpdatedOn(date);
				jobDelegationRepository.save(obj);

				retVal++;
			}
		}

		return retVal;
	}

	@Override
	public String deleteJobDeligation(Integer delegationId) {
		String result = "";
		JobDelegationEntity jobDelegation = null;
	

		try {
			jobDelegation = jobDelegationRepository.findByDelegationId(delegationId);
			if (jobDelegation != null) {
				jobDelegation.setBitStatus(true);
				jobDelegationRepository.saveAndFlush(jobDelegation);

				return WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("JobDelegationServiceImpl::deleteJobDeligation():" + e);

			return WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public List<User> viewUserListByOrgId(int organisationId) {
		return userRepository.viewUserListByOrgId(organisationId);
	}

}
