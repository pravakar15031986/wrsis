package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.csmpl.adminconsole.responsedto.UpdatePermissionResponseDto;
import com.csmpl.adminconsole.webportal.entity.AdminRole;
import com.csmpl.adminconsole.webportal.entity.GlobalLink;
import com.csmpl.adminconsole.webportal.entity.User;

public interface ManageUserMasterService {

	List<User> getUserList();

	List<GlobalLink> getglobalList();

	List<User> getUnassignedPrimaryLinkUserList();

	List<AdminRole> viewPermissions();

	Page<User> getPermissionAssignedUserList(Pageable pageable);

	Page<User> getSearchUsers(User usr, Pageable pageable);

	List<UpdatePermissionResponseDto> getUserLinksAccessByUserId(int userId);

	String getUserPrimaryLnkActionByUsrIdAndPlnkId(int userId, int getpLinkId);

}
