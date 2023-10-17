package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.entity.UserType;

public interface ManageUserTypeMasterService {

	UserType addUserType(UserType u);

	List<UserType> viewALlUserType();

	UserType getUserTypeById(Integer id);

}
