package com.csmpl.adminconsole.webportal.serviceImpl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.csmpl.adminconsole.webportal.bean.UserRoleDto;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.entity.AdmLevelDetails;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.entity.UserRole;
import com.csmpl.adminconsole.webportal.repository.AdminLevelRepository;
import com.csmpl.adminconsole.webportal.repository.AdminRoleRepository;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.repository.UserResearchCenterMapRepository;
import com.csmpl.adminconsole.webportal.repository.UserRoleRepository;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Value("${default.login.password}")
	private String defaultLoginPassword;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	DiseaseMasterRepository DiseaseMasterRepository;

	@Autowired
	AdminRoleRepository adminRoleRepository;

	@Autowired
	AdminLevelRepository adminLevelRepository;

	@Autowired
	UserResearchCenterMapRepository userResearchCenterMapRepository;

	@Override
	public User saveUser(User u) {
		User user = null;
		try {
			user = userRepository.save(u);
		} catch (Exception e) {
			LOG.error("UserServiceImpl::saveUser():" + e);
		}
		return user;
	}

	@Override
	public User getUserById(Integer userId) {
		
		return userRepository.findByUserId(userId);
	}

	@Override
	public List<User> viewUserList() {
		return userRepository.findAll();
	}

	@Override
	public boolean isEmailExist(String email) {

		List<User> list = userRepository.findByEmail(email);


		
		if (list != null && !list.isEmpty()) {
			return true;
		} else {

			return false;
		}

	}

	@Override
	public List<UserBean> viewAllUserByPage(String mobileNo, String email, String name, String oname, String role,
			String center, String desig, String status) {
		List<UserBean> userList = new ArrayList<>();
		try {

			List<Object[]> page = userRepository.viewAllPage(mobileNo, email, name, oname, role, center, desig, status);

			UserBean userBean = null;
			for (Object[] user : page) {
				for (int i = 0; i < user.length; i++) {
					userBean = new UserBean();
					userBean.setUserId(
							(user[0] instanceof Short) ? (Short) user[0] : Short.parseShort(String.valueOf(user[0])));
					userBean.setFullName((String) user[1]);
					userBean.setDesignation((String) user[2]);
					userBean.setRoleName((String) user[3]);
					userBean.setMobile((String) user[5]);
					userBean.setEmail((String) user[6]);
					userBean.setMapStatus((user[7] instanceof Boolean) ? (Boolean) user[7]
							: Boolean.parseBoolean(String.valueOf(user[7])));

					userBean.setOrganizationName((String) user[8]);
					if ((user[7] instanceof Boolean) ? (Boolean) user[7]
							: Boolean.parseBoolean(String.valueOf(user[7])) == false) {
						userBean.setBitStatus("Active");
					} else {
						userBean.setBitStatus("Inactive");
					}

					if (user[9] != null) {
						userBean.setRcName((String) user[9]);
					}
					userBean.setPasswordUnlock(
							(user[10] instanceof Character) ? Character.toString((Character) user[10])
									: (String) user[10]);
					if (userBean.getPasswordUnlock().equals("Y")) {
						String s1 = "<a class=\"btn btn-danger\" data-toggle=\"tooltip\" title=\"Unlock\" onclick=\"unlock('"
								+ userBean.getUserId()
								+ "')\"> <i id=\"actionId\" class=\"fa fa-share\" aria-hidden=\"true\"></i></a>";
						userBean.setPasswordUnlock(s1);
					} else {
						String s1 = "";
						userBean.setPasswordUnlock(s1);
					}
					String s = "<a class=\"btn btn-primary\" data-toggle=\"tooltip\" title=\"Edit\" onclick=\"edit('"
							+ userBean.getUserId()
							+ "')\"> <i id=\"actionId\" class=\"fa fa-share\" aria-hidden=\"true\"></i></a>";
					userBean.setEdit(s);

					userBean.setUserName((String) user[12]);
					userBean.setGender((user[11] instanceof Short) ? Integer.valueOf(Short.toString((Short) user[11]))
							: Integer.valueOf((String) user[11]));

				}

				userList.add(userBean);

			}

		} catch (Exception e) {
			LOG.error("UserServiceImpl::viewAllUserByPage():" + e);
		}

		return userList;
	}

	

	@Transactional
	@Override
	public String saveUserMaster(UserBean userBean) {
		String result = null;
		User user = new User();
		try {

			if (userBean.getUserId() == 0) {

				user.setUserName(userBean.getUserName().toLowerCase());
				user.setPassword(userBean.getPassword());

				if (userBean.getMapStatus()) { // Is research center map to user
					user.setMemUserTypeId(1);
				} else {
					user.setMemUserTypeId(0);
				}
				user.setFullName(userBean.getFullName());
				user.setMobile(userBean.getMobile());
				user.setEmail(userBean.getEmail());
				user.setIntdesigid(userBean.getIntdesigid());

				user.setIntLevelDetailId(userBean.getIntLevelDetailId());

				user.setFirstLogin('Y');
				user.setLoginFailAttempt('N');

				user.setBitStatus(Boolean.valueOf(userBean.getBitStatus()));

				user.setCreatedBy(userBean.getCreatedBy());
				user.setCreatedOn(new Timestamp(new Date().getTime()));

				user.setGroupId(0);
				user.setUpdatedBy(0);
				user.setGender(userBean.getGender());// 1 for male and 2 for female

				User newUser = userRepository.saveAndFlush(user);

				for (int roleId : userBean.getSelectedRoleList()) {
					UserRole usrRol = new UserRole();
					usrRol.setUserId(newUser.getUserId());
					usrRol.setRoleId(roleId);
					userRoleRepository.saveAndFlush(usrRol);
				}

				if (newUser.getMemUserTypeId() == 1) {
					UserResearchCenterMapping urcObj = new UserResearchCenterMapping();
					urcObj.setUserId(newUser.getUserId());
					urcObj.setRecearchCenterId(userBean.getResearchCenterId());
					userResearchCenterMapRepository.save(urcObj);
				}

				result = "successAdd";

			} else {

				user = userRepository.findByUserId(userBean.getUserId());
				if (user != null) {

					user.setFullName(userBean.getFullName());
					user.setIntdesigid(userBean.getIntdesigid());
					user.setMobile(userBean.getMobile());
					user.setEmail(userBean.getEmail());

					if (userBean.getMapStatus()) { // Is research center map to user
						user.setMemUserTypeId(1);
					} else {
						user.setMemUserTypeId(0);
					}

					user.setGender(userBean.getGender());

					user.setIntLevelDetailId(userBean.getIntLevelDetailId());

					user.setBitStatus(Boolean.valueOf(userBean.getBitStatus()));
					user.setUpdatedOn(new Timestamp(new Date().getTime()));
					user.setUpdatedBy(userBean.getCreatedBy());

					userRepository.saveAndFlush(user);

					// Role mapping start
					List<Integer> newRoles = userBean.getSelectedRoleList();
					
					List<UserRole> exisingRoles = userRoleRepository.findByUserId(userBean.getUserId());

					boolean flag = false;
					for (UserRole existRole : exisingRoles) {
						flag = false;
						for (Integer newRoleId : newRoles) {
							if (existRole.getRoleId() == newRoleId) {
								if (existRole.isBitstatus()) {
									List<UserRole> usrRoles = userRoleRepository
											.findByRoleIdAndUserId(existRole.getRoleId(), user.getUserId());
									if (!usrRoles.isEmpty()) {
										UserRole usrRol = usrRoles.get(0);
										usrRol.setBitstatus(false);
										userRoleRepository.saveAndFlush(usrRol);
									}
								}

								flag = true;
							}
						}
						if (!flag) {
							// delete existRoleId
							List<UserRole> usrRoles = userRoleRepository.findByRoleIdAndUserId(existRole.getRoleId(),
									user.getUserId());
							if (!usrRoles.isEmpty()) {
								UserRole usrRol = usrRoles.get(0);
								usrRol.setBitstatus(true);
								userRoleRepository.saveAndFlush(usrRol);
							}
						}
					}

					for (Integer newRoleId : newRoles) {
						flag = false;
						for (UserRole existRole : exisingRoles) {
							if (existRole.getRoleId() == newRoleId) {
								flag = true;
							}
						}
						if (!flag) {
							// insert newRoleId
							UserRole usrRol = new UserRole();
							usrRol.setUserId(user.getUserId());
							usrRol.setRoleId(newRoleId);
							userRoleRepository.saveAndFlush(usrRol);
						}
					}
					// Role mapping end

					// Research center mapping start
					if (user.getMemUserTypeId() == 1) {
						UserResearchCenterMapping urcObj = userResearchCenterMapRepository
								.findByUserId(user.getUserId());
						if (urcObj != null) {
							urcObj.setRecearchCenterId(userBean.getResearchCenterId());
							urcObj.setBitStatus(false);
							userResearchCenterMapRepository.saveAndFlush(urcObj);
						} else {
							UserResearchCenterMapping nurcObj = new UserResearchCenterMapping();
							nurcObj.setUserId(user.getUserId());
							nurcObj.setRecearchCenterId(userBean.getResearchCenterId());
							userResearchCenterMapRepository.saveAndFlush(nurcObj);
						}
					} else {
						UserResearchCenterMapping urcObj = userResearchCenterMapRepository
								.findByUserId(user.getUserId());
						if (urcObj != null) {
							urcObj.setRecearchCenterId(userBean.getResearchCenterId());
							urcObj.setBitStatus(true);
							userResearchCenterMapRepository.saveAndFlush(urcObj);
						}
					}
					// Research center mapping end
				}

				result = "successUpdate";

			}

		} catch (Exception e) {
			result = WrsisPortalConstant.FAILURE;
			LOG.error("UserServiceImpl::saveUserMaster():" + e);
		}

		return result;
	}

	@Override
	public List<UserRoleDto> getAllRoleIds(UserRoleDto udto) {
		List<UserRoleDto> list = new ArrayList<>();
		UserRoleDto vo = null;
		try {

			List<Object[]> roleIds = userRoleRepository.getAllRoleIds(udto.getUserId());
			if (!roleIds.isEmpty()) {
				for (Object[] obj : roleIds) {
					vo = new UserRoleDto();
					vo.setRoleId((Short) (obj[0]));
					vo.setRoleName(String.valueOf(obj[1]));
					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("UserServiceImpl::getAllRoleIds():" + e);
		}
		return list;

	}

	@Override
	public boolean isMobileNoExist(String MobileNo) {

		List<User> list = userRepository.findByMobile(MobileNo);


		if (list != null && !list.isEmpty()) {
			return true;
		} else {

			return false;
		}

	}

	@Override
	public Integer existEmail(String email, int userId) {

		return userRepository.findByExitEmail(email, userId);
		

	}

	@Override
	public Integer existMobileNo(String mobile, int userId) {

		return  userRepository.findByExitMobile(mobile, userId);
		 

	}

	@Override
	public String searchUserByAjax(Integer pageSize, Integer pageNumber, Pageable pageable, String fullName,
			Integer orgName, String mobile, String email, Boolean status, Integer intdesigid) { // ,Integer roleVal
		JSONObject jobjb = new JSONObject();
		JSONArray array = new JSONArray();
		String cond = "";
		try {

			List<Object[]> page = null;
			if (mobile != null && !"".equalsIgnoreCase(mobile) || fullName != null && !"".equalsIgnoreCase(fullName)
					|| email != null && !"".equalsIgnoreCase(email) || status != null) {
				page = userRepository.viewSearchPage(email, fullName, mobile, new PageRequest(0, 10)); // roleVal,
			}
			

			if (page != null && !page.isEmpty()) {
				int x = page.size();

				int currentPage = 0;
				int beginPage = Math.max(1, currentPage - 5);
				int endPage = Math.min(beginPage + pageSize, x);
				jobjb.put("startPage", beginPage);
				jobjb.put("endPage", endPage);
				jobjb.put("currentPage", currentPage);
				jobjb.put("pageNo", 0);
				jobjb.put("showRowNo", 10);
				jobjb.put("totalRowNo", x);

				JSONObject jsonObject = null;

				UserBean userBean = null;

				for (int i = 0; i < page.size(); i++) {
					Object[] user = page.get(i);
					userBean = new UserBean();
					userBean.setUserId((Short) user[0]);
					userBean.setFullName((String) user[1]);
					userBean.setDesignation((String) user[2]);
					
					userBean.setRoleName((String) user[3]);
					userBean.setCreatedOn((Date) user[4]);
					userBean.setMobile((String) user[5]);
					userBean.setEmail((String) user[6]);
					userBean.setMapStatus((Boolean) user[7]);

					userBean.setOrganizationName((String) user[8]);

					if (user[9] != null)
						userBean.setRcName((String) user[9]);

					if (user != null) {
						jsonObject = new JSONObject();
						jsonObject.put("userId", userBean.getUserId() != 0 ? userBean.getUserId() : "NA");
						jsonObject.put("name", userBean.getFullName() != null ? userBean.getFullName() : "NA");
						jsonObject.put("userType",
								userBean.getMemUserTypeId() != 0 ? userBean.getMemUserTypeId() : "NA");
						jsonObject.put("groupId", userBean.getRoleName() != null ? userBean.getRoleName() : "NA");
						
						jsonObject.put("creationDate",
								userBean.getCreatedOn() != null
										? new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(userBean.getCreatedOn())
										: "NA");
						jsonObject.put("designationName",
								userBean.getDesignation() != null ? userBean.getDesignation() : "NA");
						jsonObject.put("mobile", userBean.getMobile() != null ? userBean.getMobile() : "NA");
						jsonObject.put("email", userBean.getEmail() != null ? userBean.getEmail() : "NA");
						if (Boolean.valueOf(userBean.getMapStatus()) == false) {
							jsonObject.put("status", "Active");
						} else {
							jsonObject.put("status", "Inactive");
						}
						jsonObject.put("orgName",
								userBean.getOrganizationName() != null ? userBean.getOrganizationName() : "-NA-");
						jsonObject.put("rcName", userBean.getRcName() != null ? userBean.getRcName() : "NA");
					}

					array.put(jsonObject);

				}
			} else {
				jobjb.put("totalRowNo", "0");
			}
			jobjb.put("userList", array);
		} catch (Exception e) {
			LOG.error("UserServiceImpl::searchUserByAjax():" + e);
		}

		return jobjb.toString();
	}

	@Override
	public List<AdmLevelDetails> getOrganizationList() {
		List<AdmLevelDetails> list = adminLevelRepository.findByLevelIdAndBitStatus(3, false);
		list.sort(Comparator.comparing(AdmLevelDetails::getLevelName));
		return list;
	}

	@Override
	public UserResearchCenterMapping getUserResearchCenterMapping(Integer userId) {
		List<UserResearchCenterMapping> list = userResearchCenterMapRepository.findByUserIdAndBitStatus(userId, false);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<User> getUsersByOrganizationId(Integer userId, Integer orgId) {

		
		return userRepository.retriveUsersByLevelId(userId, orgId);
	}

	@Override
	public String getUserByRoleAndOrgId(int roleId, int intLevelDetailId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		JSONArray sortedJsonArray = new JSONArray();
		try {
			List<Object[]> userList = userRepository.getUserByRoleAndOrgId(roleId, intLevelDetailId);

			for (Object[] dObject : userList) {
				jobj = new JSONObject();
				jobj.put("userId", (Short) dObject[0]);
				jobj.put("username", String.valueOf(dObject[1]));
				jobj.put("name", String.valueOf(dObject[2]));
				jarr.put(jobj);
			}
			

			List<JSONObject> jsonValues = new ArrayList<>();
			for (int i = 0; i < jarr.length(); i++) {
				jsonValues.add(jarr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				// You can change "Name" with "ID" if you want to sort by ID
				private static final String KEY_NAME = "name";

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = new String();
					String valB = new String();

					try {
						valA = (String) a.get(KEY_NAME);
						valB = (String) b.get(KEY_NAME);
					} catch (JSONException e) {
						LOG.error("UserServiceImpl::getUserByRoleAndOrgId():" + e);
					}

					return valA.compareToIgnoreCase(valB);
					// if you want to change the sort order, simply use the following:
					
				}
			});

			for (int i = 0; i < jarr.length(); i++) {
				sortedJsonArray.put(jsonValues.get(i));
			}
			

		} catch (Exception e) {
			LOG.error("UserServiceImpl::getUserByRoleAndOrgId():" + e);
		}
		return sortedJsonArray.toString();
	}

	@Override
	public List<UserBean> viewUserDetails(int start, int len) {
		List<UserBean> list = new ArrayList<>();
		UserBean vo = null;
		try {

			List<Object[]> userList = userRepository.viewUserDetails(start, len);
			if (!userList.isEmpty()) {
				for (Object[] obj : userList) {
					vo = new UserBean();
					vo.setUserId((Short) obj[0]);
					vo.setFullName((String) obj[1]);
					vo.setDesignation((String) obj[2]);
					vo.setRoleName((String) obj[3]);
					vo.setCreatedOn((Date) obj[4]);
					vo.setMobile((String) obj[5]);
					vo.setEmail((String) obj[6]);
					if ((Boolean) obj[7] == false) {
						vo.setBitStatus("Active");
					} else {
						vo.setBitStatus("Inactive");
					}
					vo.setOrganizationName((String) obj[8]);
					if (obj[9] == null) {
						vo.setRcName("NA");
					} else {
						vo.setRcName((String) obj[9]);
					}
					vo.setPasswordUnlock(String.valueOf((Character) obj[10]));

					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("UserServiceImpl::viewUserDetails():" + e);
		}
		return list;

	}

	@Override
	public Integer viewUserDetailsCount() {

		return userRepository.viewUserDetailsCount();
	}

	@Override
	public List<UserBean> viewUserSearch(int start, int len, String email) {
		List<UserBean> list = new ArrayList<>();
		UserBean vo = null;
		try {

			List<Object[]> userList = userRepository.viewUserDetails(start, len);
			if (!userList.isEmpty()) {
				for (Object[] obj : userList) {
					vo = new UserBean();
					vo.setUserId((Short) obj[0]);
					vo.setFullName((String) obj[1]);
					vo.setDesignation((String) obj[2]);
					vo.setRoleName((String) obj[3]);
					vo.setCreatedOn((Date) obj[4]);
					vo.setMobile((String) obj[5]);
					vo.setEmail((String) obj[6]);
					if ((Boolean) obj[7] == false) {
						vo.setBitStatus("Active");
					} else {
						vo.setBitStatus("Inactive");
					}
					vo.setOrganizationName((String) obj[8]);
					if (obj[9] == null) {
						vo.setRcName("NA");
					} else {
						vo.setRcName((String) obj[9]);
					}

					list.add(vo);
				}
			}

		} catch (Exception e) {
			LOG.error("UserServiceImpl::viewUserSearch():" + e);
		}
		return list;
	}

	// User Unlock Status

	@Override
	public String unlockUserMaster(UserBean userBean) {
		String result = "";
		User user = null;
		try {
			user = userRepository.findByUserId(userBean.getUnlockUserId());
			if (user != null) {
				user.setLoginFailAttempt('N');
				user.setUpdatedOn(new Timestamp(new Date().getTime()));
				user.setUpdatedBy(userBean.getUserId());
				userRepository.saveAndFlush(user);
				userRepository.updateTrack(user.getUserId());

				return WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("UserServiceImpl::unlockUserMaster():" + e);
			return WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public String getNotificationUserByRoleAndOrgId(int roleId, int intLevelDetailId, int rcId, int userId) {
		JSONArray jarr = new JSONArray();
		JSONObject jobj = null;
		JSONArray sortedJsonArray = new JSONArray();
		try {
			List<Object[]> userList = userRepository.getNotificationUserByRoleRCAndOrgId(roleId, intLevelDetailId,
					rcId);

			for (Object[] dObject : userList) {
				if (Integer.parseInt(String.valueOf(dObject[0])) != userId) {
					jobj = new JSONObject();
					jobj.put("userId", String.valueOf(dObject[0]));
					jobj.put("username", String.valueOf(dObject[1]));
					jobj.put("name", String.valueOf(dObject[2]));
					jarr.put(jobj);
				}
			}
			//

			List<JSONObject> jsonValues = new ArrayList<>();
			for (int i = 0; i < jarr.length(); i++) {
				jsonValues.add(jarr.getJSONObject(i));
			}
			Collections.sort(jsonValues, new Comparator<JSONObject>() {
				// You can change "Name" with "ID" if you want to sort by ID
				private static final String KEY_NAME = "name";

				@Override
				public int compare(JSONObject a, JSONObject b) {
					String valA = new String();
					String valB = new String();

					try {
						valA = (String) a.get(KEY_NAME);
						valB = (String) b.get(KEY_NAME);
					} catch (JSONException e) {
						LOG.error("UserServiceImpl::getNotificationUserByRoleAndOrgId():" + e);
					}

					return valA.compareToIgnoreCase(valB);
					// if you want to change the sort order, simply use the following:
					
				}
			});

			for (int i = 0; i < jarr.length(); i++) {
				sortedJsonArray.put(jsonValues.get(i));
			}
			//

		} catch (Exception e) {
			LOG.error("UserServiceImpl::getNotificationUserByRoleAndOrgId():" + e);
		}
		return sortedJsonArray.toString();
	}

	@Override
	public List<UserBean> viewUserDataPagination(String mobile, String email, String fullName, String organizationId,
			String roleId, String researchCenterId, String intdesigid, String status, Integer pstart, Integer plength) {
		List<UserBean> userList = new ArrayList<>();
		Integer count = 0;
		try {

			List<Object[]> page = userRepository.viewUserDataPagination(mobile, email, fullName, organizationId, roleId,
					researchCenterId, intdesigid, status, pstart, plength);

			UserBean userBean = null;
			if (!page.isEmpty()) {
				for (Object[] user : page) {
					
					userBean = new UserBean();
					userBean.setsNo(pstart + (++count));
					userBean.setUserId(
							(user[0] instanceof Short) ? (Short) user[0] : Short.parseShort(String.valueOf(user[0])));
					userBean.setFullName((String) user[1]);
					userBean.setDesignation((String) user[2]);
					userBean.setRoleName((String) user[3]);
					userBean.setMobile((String) user[5]);
					userBean.setEmail((String) user[6]);
					userBean.setMapStatus((user[7] instanceof Boolean) ? (Boolean) user[7]
							: Boolean.parseBoolean(String.valueOf(user[7])));

					userBean.setOrganizationName((String) user[8]);
					if ((user[7] instanceof Boolean) ? (Boolean) user[7]
							: Boolean.parseBoolean(String.valueOf(user[7])) == false) {
						userBean.setBitStatus("Active");
					} else {
						userBean.setBitStatus("Inactive");
					}

					if (user[9] != null) {
						userBean.setRcName((String) user[9]);
					}
					userBean.setPasswordUnlock(
							(user[10] instanceof Character) ? Character.toString((Character) user[10])
									: (String) user[10]);
					if (userBean.getPasswordUnlock().equals("Y")) {
						String s1 = "<a  data-toggle=\"tooltip\" title=\"Unlock User\" onclick=\"unlock('"
								+ userBean.getUserId() + "')\">"
								+ "<button class=\"btn btn-danger btn-xs\" data-title=\"Unlock Use\" data-toggle=\"modal\" data-target=\"#edit\">\r\n"
								+ "<i class=\"fa fa-lock\"></i></button></a>";
						userBean.setPasswordUnlock(s1);
					} else {
						String s1 = "";
						userBean.setPasswordUnlock(s1);
					}
					String s = "<a data-placement=\"top\" data-original-title=\"Edit User\" data-toggle=\"tooltip\" title=\"Edit User\" onclick=\"edit('"
							+ userBean.getUserId() + "')\">"
							+ " <button class=\"btn btn-primary btn-xs\" data-title=\"Edit\" data-toggle=\"modal\" data-target=\"#edit\">\r\n"
							+ "<i class=\"icon-edit1\"></i></button></a>";
					userBean.setEdit(s);

					userBean.setUserName((String) user[12]);
					if (Integer.valueOf(user[11].toString()) == 1) {
						userBean.setGenderName(WrsisPortalConstant.MALE);

					} else if (Integer.valueOf(user[11].toString()) == 2) {
						userBean.setGenderName(WrsisPortalConstant.FEMALE);
					} else {
						userBean.setGenderName("NA");
					}
					if("Active".equalsIgnoreCase(userBean.getBitStatus())){
					userBean.setResetPassword("<button class=\"btn btn-primary\"	onclick=\"resetPassword('"
							+ userBean.getUserId() +"')\"><i class=\"fa fa-key\" aria-hidden=\"true\"></i></button>");
					}else {
						
						userBean.setResetPassword("");	
					}

					userList.add(userBean);
				}

			}

		} catch (Exception e) {
			LOG.error("UserServiceImpl::viewUserDataPagination():" + e);
		}

		return userList;
	}

	@Override
	public Integer viewUserDataPaginationCount(String mobile, String email, String fullName, String organizationId,
			String roleId, String researchCenterId, String intdesigid, String status, Integer pstart, Integer plength) {
		Integer totalCount = 0;
		try {
			totalCount = userRepository.viewUserDataPaginationCount(mobile, email, fullName, organizationId, roleId,
					researchCenterId, intdesigid, status, pstart, plength);
		} catch (Exception e) {
			LOG.error("UserServiceImpl::viewUserDataPaginationCount():" + e);
		}
		return totalCount;
	}

	//Reset to default password
	
	@Override
	public String resetPassword(UserBean userBean) {
		String result = null;
		User user = null;
		try {
			user = userRepository.findByUserId(userBean.getUnlockUserId());
			if (user != null) {
				user.setPassword(new MD5PasswordEncoder().encode(defaultLoginPassword));  //Encoded in MD5 format
				user.setUpdatedOn(new Timestamp(new Date().getTime()));
				user.setUpdatedBy(userBean.getUserId());
				userRepository.saveAndFlush(user);
				LOG.info("UserServiceImpl::resetPassword(): Password changed to default" );

				result  = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("UserServiceImpl::resetPassword():" + e);
			result = WrsisPortalConstant.FAILURE;
		}
		return result;
	}

	@Override
	public JSONArray getUserRolesByUserId(int userId) {
		JSONObject roleObj=null;
		JSONArray roleArray=new JSONArray();
		try {
		List<Object[]> roleList = userRepository.getRoleByUserId(userId);
		if (!roleList.isEmpty()) {
			for (Object[] optobj : roleList) {
				roleObj = new JSONObject();
				roleObj.put("rolId", String.valueOf(optobj[0]));
				roleObj.put("rolName", String.valueOf(optobj[1]));
				roleArray.put(roleObj);

			
			}
		}
		}catch (Exception e) {
			
		}
		
		return roleArray;
	}

}
