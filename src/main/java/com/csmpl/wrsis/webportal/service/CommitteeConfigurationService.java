package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.CommitteeConfigurationBean;
import com.csmpl.wrsis.webportal.entity.CommitteeConfigurationEntity;

public interface CommitteeConfigurationService {

	String addCommittee(CommitteeConfigurationBean committeeBean);

	List<CommitteeConfigurationEntity> viewCommittee();

	List<CommitteeConfigurationBean> getCommitteeDetails();

	String getMemberDetailsByComId(int committeeId);

	CommitteeConfigurationBean editCommittee(int committeeId);

	List<CommitteeConfigurationBean> getMembersByComId(int committeeId);

	String updateCommitteeDetails(CommitteeConfigurationBean committeeDetails);

	List<CommitteeConfigurationBean> viewAllMembers(int committeeId);

	List<CommitteeConfigurationBean> getAllMemberIds(CommitteeConfigurationBean cdto);

}
