package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.IpTrack;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.entity.UserPermission;
import com.csmpl.adminconsole.webportal.entity.UserType;
import com.csmpl.wrsis.webportal.bean.PasswordBean;
import com.csmpl.wrsis.webportal.bean.ProfileBean;

public interface LoginService {

	public User findByUserName(String userName);

	public List<UserPermission> findByUserId(int userId);

	public AdminRole getRolByUserId(int userId);

	IpTrack saveUserTrackInfo(IpTrack ipinfo);

	int countFailAttempt(int userId);

	public UserType getUserTypeById(int userId);

	public String updatePassword(PasswordBean loginReqDto);

	public String updateProfile(ProfileBean profileDetails);

	public String updateforgottenPassword(PasswordBean paswdResetDetails);

	public JSONObject mUserDetails(String userName);

	public String validateMobileNumberByUserId(String mobile, Integer userId);

}