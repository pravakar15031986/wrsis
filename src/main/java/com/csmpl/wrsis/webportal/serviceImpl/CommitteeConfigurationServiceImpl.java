package com.csmpl.wrsis.webportal.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.repository.AdminLevelRepository;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.CommitteeConfigurationBean;
import com.csmpl.wrsis.webportal.entity.CommitteeConfigurationEntity;
import com.csmpl.wrsis.webportal.entity.CommitteeMemberEntity;
import com.csmpl.wrsis.webportal.repository.CommitteeConfigurationRepository;
import com.csmpl.wrsis.webportal.repository.CommitteeMemberRepository;
import com.csmpl.wrsis.webportal.service.CommitteeConfigurationService;

@Service
public class CommitteeConfigurationServiceImpl implements CommitteeConfigurationService {

	public static final Logger LOG = LoggerFactory.getLogger(CommitteeConfigurationServiceImpl.class);

	@Autowired
	AdminLevelRepository adminLevelRepository;

	@Autowired
	CommitteeConfigurationRepository committeeConfigurationRepository;

	@Autowired
	CommitteeMemberRepository committeeMemberRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public String addCommittee(CommitteeConfigurationBean committeeBean) {

		String sts = WrsisPortalConstant.FAILURE;
		CommitteeConfigurationEntity committeeDetails = new CommitteeConfigurationEntity();
		CommitteeMemberEntity committeeMembers = null;
		try {
			List<Integer> users = committeeBean.getSelectedUsers();
			if (users != null) {
				committeeDetails.setCommitteName(committeeBean.getCommitteeName());
				committeeDetails.setRoleId(committeeBean.getCommitteeRoleId());
				committeeDetails.setDesc(committeeBean.getCommitteeDesc());
				committeeDetails.setStatus(false);
				committeeDetails.setCreatedBy(committeeBean.getUserId());
				committeeDetails.setCreatedOn(new Date());

				CommitteeConfigurationEntity x = committeeConfigurationRepository.save(committeeDetails);

				for (int i = 0; i < users.size(); i++) {
					committeeMembers = new CommitteeMemberEntity();
					committeeMembers.setCommitteeId(x.getCommitteeId());
					committeeMembers.setUserId(users.get(i));
					committeeMembers.setCreatedBy(committeeBean.getUserId());
					committeeMembers.setCreatedOn(new Date());
					committeeMembers.setStatus(false);
					committeeMemberRepository.saveAndFlush(committeeMembers);
				}
				sts = WrsisPortalConstant.SUCCESS;
			} else if (users == null) {
				sts = "nousers";
				return sts;
			}
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::addCommitee():" + e);

		}

		return sts;
	}

	@Override
	public List<CommitteeConfigurationEntity> viewCommittee() {
		List<CommitteeConfigurationEntity> committeeList = null;
		try {
			committeeList = committeeConfigurationRepository.findAll();
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::viewCommitee():" + e);
		}
		return committeeList;
	}

	@Override
	public List<CommitteeConfigurationBean> getCommitteeDetails() {
		List<CommitteeConfigurationBean> list = new ArrayList<>();

		CommitteeConfigurationBean cobj = null;

		try {
			List<Object[]> committeeList = committeeConfigurationRepository.getCommitteeDetails();
			for (Object[] obj : committeeList) {
				cobj = new CommitteeConfigurationBean();
				cobj.setCommitteeId((Short) obj[0]);
				cobj.setCommitteeName(String.valueOf(obj[1]));
				cobj.setRoleId((Short) obj[2]);
				cobj.setCommitteeRole(String.valueOf(obj[3]));
				cobj.setCommitteeDesc(String.valueOf(obj[4]));
				list.add(cobj);
			}
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::getCommiteeDetails():" + e);
		}

		return list;
	}

	@Override
	public String getMemberDetailsByComId(int committeeId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		try {
			List<Object[]> memberList = committeeConfigurationRepository.getMemberDetailsByComId(committeeId);

			for (Object[] dObject : memberList) {
				jobj = new JSONObject();
				jobj.put("organization", String.valueOf(dObject[0]));
				jobj.put("fullName", String.valueOf(dObject[1]));
				jobj.put("role", String.valueOf(dObject[2]));
				jarr.put(jobj);
			}
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::getMemberDetailsByComId():" + e);
		}
		return jarr.toString();
	}

	@Override
	public CommitteeConfigurationBean editCommittee(int committeeId) {
		CommitteeConfigurationBean cObj = null;
		try {
			List<Object[]> olist = committeeConfigurationRepository.getCommitteeDetailsByComId(committeeId);
			for (Object[] obj : olist) {
				cObj = new CommitteeConfigurationBean();
				cObj.setCommitteeName(String.valueOf(obj[0]));
				cObj.setCommitteeRoleId((Short) obj[1]);
				cObj.setCommitteeRole(String.valueOf(obj[2]));
				cObj.setCommitteeDesc(String.valueOf(obj[3]));
				cObj.setCommitteeId((Short) obj[4]);
			}
		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::editCommittee():" + e);
		}
		return cObj;
	}

	@Override
	public List<CommitteeConfigurationBean> getMembersByComId(int committeeId) {

		List<CommitteeConfigurationBean> list = new ArrayList<>();
		CommitteeConfigurationBean vo = null;
		try {
			List<Object[]> members = committeeConfigurationRepository.getMembersByComId(committeeId);
			if (!members.isEmpty()) {
				for (Object[] obj : members) {
					vo = new CommitteeConfigurationBean();
					vo.setUserId((Integer) obj[0]);
					vo.setFullName(String.valueOf(obj[1]));
					vo.setUserName(String.valueOf(obj[2]));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::getMembersByComId():" + e);
		}
		return list;
	}

	@Override
	public String updateCommitteeDetails(CommitteeConfigurationBean committeeDetails) {
		String retStatus = WrsisPortalConstant.FAILURE;
		
		try {
			List<Object[]> commlist = committeeMemberRepository
					.doesCommitteeNameDataExist(committeeDetails.getCommitteeId(), committeeDetails.getCommitteeName());
			if (!commlist.isEmpty()) {
				return "committeeexists";
			}
			List<Integer> users = committeeDetails.getSelectedUsers();

			if (users == null) {
				return "nousers";
			} else if (users != null) {

				// member mapping start
				List<Integer> newUsers = committeeDetails.getSelectedUsers();
				List<CommitteeMemberEntity> existingUsers = committeeMemberRepository
						.findByCommitteeId(committeeDetails.getCommitteeId());

				boolean flag = false;
				for (CommitteeMemberEntity existuser : existingUsers) {
					flag = false;
					for (Integer newUserId : newUsers) {
						if (existuser.getUserId() == newUserId) {
							if (existuser.isStatus()) {
								List<CommitteeMemberEntity> comUsr = committeeMemberRepository
										.findByUserIdAndCommitteeId(existuser.getUserId(),
												committeeDetails.getCommitteeId());
								if (!comUsr.isEmpty()) {
									CommitteeMemberEntity usr = comUsr.get(0);
									usr.setStatus(false);
									committeeMemberRepository.saveAndFlush(usr);
								}
							}

							flag = true;
						}
					}
					if (!flag) {
						// delete existMemberId
						List<CommitteeMemberEntity> comUsr = committeeMemberRepository
								.findByUserIdAndCommitteeId(existuser.getUserId(), committeeDetails.getCommitteeId());
						if (!comUsr.isEmpty()) {
							CommitteeMemberEntity usr = comUsr.get(0);
							usr.setStatus(true);
							usr.setUpdatedBy(committeeDetails.getUserId());
							usr.setUpdatedOn(new Date());
							committeeMemberRepository.saveAndFlush(usr);
						}
					}
				}

				for (Integer newUserId : newUsers) {
					flag = false;
					for (CommitteeMemberEntity existuser : existingUsers) {
						if (existuser.getUserId() == newUserId) {
							flag = true;
						}
					}
					if (!flag) {
						// insert newMemberId
						CommitteeMemberEntity comUsr = new CommitteeMemberEntity();
						comUsr.setCommitteeId(committeeDetails.getCommitteeId());
						comUsr.setUserId(newUserId);
						comUsr.setCreatedBy(committeeDetails.getUserId());
						comUsr.setCreatedOn(new Date());
						committeeMemberRepository.saveAndFlush(comUsr);
					}
				}
				// Member mapping end
				CommitteeConfigurationEntity dto = committeeConfigurationRepository
						.getOne(committeeDetails.getCommitteeId());
				dto.setCommitteName(committeeDetails.getCommitteeName());
				dto.setRoleId(committeeDetails.getCommitteeRoleId());
				dto.setDesc(committeeDetails.getCommitteeDesc());
				dto.setStatus(false);
				dto.setUpdatedBy(committeeDetails.getUserId());
				dto.setUpdatedOn(new Date());
				committeeConfigurationRepository.save(dto);
			}
			retStatus = WrsisPortalConstant.SUCCESS;

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::updateCommitteeDetails():" + e);
			retStatus = WrsisPortalConstant.FAILURE;
		}
		return retStatus;
	}

	@Override
	public List<CommitteeConfigurationBean> viewAllMembers(int committeeId) {
		List<CommitteeConfigurationBean> list = new ArrayList<>();
		CommitteeConfigurationBean vo = null;
		try {

			List<Object[]> membersIds = committeeMemberRepository.getAllMemberIds(committeeId);
			if (!membersIds.isEmpty()) {
				for (Object[] obj : membersIds) {
					vo = new CommitteeConfigurationBean();
					vo.setUserId((Short) (obj[0]));
					vo.setFullName(String.valueOf(obj[1]));
					vo.setUserName(String.valueOf(obj[2]));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::viewallMembers():" + e);
		}
		return list;
	}

	@Override
	public List<CommitteeConfigurationBean> getAllMemberIds(CommitteeConfigurationBean cdto) {
		List<CommitteeConfigurationBean> list = new ArrayList<>();
		CommitteeConfigurationBean vo = null;
		try {

			List<Object[]> membersIds = committeeMemberRepository.getAllMemberIds(cdto.getCommitteeId());
			if (!membersIds.isEmpty()) {
				for (Object[] obj : membersIds) {
					vo = new CommitteeConfigurationBean();
					vo.setUserId((Integer) (obj[0]));
					vo.setFullName(String.valueOf(obj[1]));
					vo.setUserName(String.valueOf(obj[2]));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("CommitteeConfigurationServiceImpl::getAllMemberIds():" + e);
		}
		return list;
	}
}
