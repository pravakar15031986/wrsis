package com.csmpl.adminconsole.webportal.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.csmpl.adminconsole.webportal.bean.FunctionMasterVo;
import com.csmpl.adminconsole.webportal.bean.GlobalLinkVo;
import com.csmpl.adminconsole.webportal.bean.PrimaryLinkBean;
import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.entity.GlobalLink;
import com.csmpl.adminconsole.webportal.entity.PrimaryLink;

public interface ManagePrimaryLinkService {

	List<GlobalLinkVo> getGlobalLink();

	List<FunctionMasterVo> getFunctionList();

	String addPrimaryLink(PrimaryLinkBean primarylinkVo);

	Set<PrimaryLinkBean> getPrimaryLinks(Set<Integer> set);

	List<PrimaryLinkBean> getAllPrimaryLinks();

	List<PrimaryLinkBean> viewPrimaryLinks(SearchVo searchVo);

	PrimaryLinkBean editPrimaryLink(int primaryLinkId);

	String updatePrimarylink(PrimaryLinkBean primarylinkVo);

	String deletePrimaryLink(int primaryLinkId);

	String chkDuplicatePrimaryLinkNameByName(String name);

	int getMaxSlNumber();

	Map<String, List<PrimaryLink>> findUserLeftMenuById(int userId);

	Map<GlobalLink, List<PrimaryLink>> findUserLeftMenuByUserId(int userId);

	String changePrimaryLinkStatus(Integer pl_id, Integer status);

	String chkDuplicatePrimaryLinkNameByName(int primaryLinkId, String primaryLinkName);

}
