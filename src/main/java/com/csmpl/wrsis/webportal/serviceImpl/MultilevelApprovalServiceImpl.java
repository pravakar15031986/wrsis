package com.csmpl.wrsis.webportal.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.AuthorityBeans;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.ApprovalActionEntity;
import com.csmpl.wrsis.webportal.entity.MultilevelApprovalEntity;
import com.csmpl.wrsis.webportal.entity.ProcessEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalActionRepository;
import com.csmpl.wrsis.webportal.repository.MultilevelApprovalRepository;
import com.csmpl.wrsis.webportal.repository.ProcessRepository;
import com.csmpl.wrsis.webportal.service.MultilevelApprovalService;

@Component
public class MultilevelApprovalServiceImpl implements MultilevelApprovalService {

	public static final Logger LOG = LoggerFactory.getLogger(MultilevelApprovalServiceImpl.class);

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ApprovalActionRepository approvalActionRepository;

	@Autowired
	MultilevelApprovalRepository multilevelApprovalRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Override
	public List<ProcessEntity> findAllProcess() {

		return processRepository.findByBitstatusOrderByProcessNameAsc(false);
	}

	@Override
	public List<ApprovalActionEntity> viewActionList() {

		return approvalActionRepository.findAll();
	}

	@Override
	public String storeMultileveApprovalAuthority(AuthorityBeans approvalObj, String jsonString) {
		String returnType = WrsisPortalConstant.SUCCESS;
		
		MultilevelApprovalEntity multilevelObj = null;
		
		try {

			if (jsonString != null) {
				
				JSONObject jsonObj = new JSONObject(jsonString);
				String process = jsonObj.getString("Process");
				JSONArray jsa = jsonObj.getJSONArray("RowsData");
				multilevelApprovalRepository.removeByProcessId(Integer.parseInt(process));
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject innerJson = jsa.getJSONObject(i);
					String role = innerJson.getString("Role");
					String officer = innerJson.getString("Officer");
					String timeline = innerJson.getString("Timeline");
					String actions = innerJson.getString("Action");
					int stage = (i + 1);

					multilevelObj = new MultilevelApprovalEntity();
					multilevelObj.setProcessId(Integer.parseInt(process));
					multilevelObj.setStageNo(stage);
					multilevelObj.setRoleId(Integer.parseInt(role));
					multilevelObj.setAuthId(Integer.parseInt(officer));
					multilevelObj.setTimeLine(Integer.parseInt(timeline));

					multilevelObj.setActionIds(actions);
					multilevelObj.setCreatedBy(approvalObj.getUserId());
					multilevelObj.setCreatedOn(new Timestamp(new Date().getTime()));
					multilevelObj.setBitStatus(false);
					multilevelObj.setDelegateStatus(false);
					multilevelApprovalRepository.saveAndFlush(multilevelObj);

				}

				
			}

			returnType = WrsisPortalConstant.SUCCESS;
		} catch (Exception e) {
			LOG.error("MultilevelApprovalServiceImpl::storemultilevelApprovalAuthority():" + e);

		}

		return returnType;
	}

	
	@Override
	public String viewApprovalAuthorityDetails(Integer processId) {
		
		JSONArray array = new JSONArray();

		try {
			List<Object[]> jsonString = multilevelApprovalRepository.viewApprovalAuthorityDetails(processId);
			JSONObject jsonObj = null;
			for (Object[] objArr : jsonString) {

				jsonObj = new JSONObject();
				jsonObj.put("Role", String.valueOf(objArr[0]));
				jsonObj.put("Officer", String.valueOf(objArr[1]));
				jsonObj.put("Timeline", String.valueOf(objArr[2]));
				jsonObj.put("Action", objArr[3]);
				array.put(jsonObj);

			}
			
		} catch (Exception e) {
			LOG.error("MultilevelApprovalServiceImpl::viewApprovalAuthorityDetails():" + e);

		}
		return array.toString();

	}

	@Override
	public List<UserBean> getUsersByRoleId(Integer roleId) {
		List<UserBean> list = new ArrayList<>();
		UserBean vo = null;
		try {

			List<Object[]> roleIds = userRoleRepository.getUsersByRoleId(roleId);
			if (!roleIds.isEmpty()) {
				for (Object[] obj : roleIds) {
					vo = new UserBean();
					vo.setUserId((Short) (obj[0]));
					vo.setUserName(String.valueOf(obj[1]));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("MultilevelApprovalServiceImpl::getUsersByRoleId():" + e);

		}
		return list;
	}

}