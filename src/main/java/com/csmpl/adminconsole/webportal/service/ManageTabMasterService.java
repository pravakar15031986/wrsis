package com.csmpl.adminconsole.webportal.service;

import java.util.List;

import com.csmpl.adminconsole.webportal.bean.ButtonMasterVo;
import com.csmpl.adminconsole.webportal.bean.FunctionMasterVo;
import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.bean.TabMasterVo;

public interface ManageTabMasterService {

	List<FunctionMasterVo> getFunctionList();

	String addTabMaster(TabMasterVo tabMasterVo);

	List<ButtonMasterVo> getButtonList(int funId);

	List<TabMasterVo> viewTabMasterList(SearchVo searchVo);

	String updateTabMaster(TabMasterVo tabMasterVo);

	String deleteTabMaster(int tabId);

	TabMasterVo getTabMasterDataById(int tabId);

	String chkTabMasterNameByName(String tabName);

	String chkTabFileNameByFileName(String tab_file_name);

	int getMaxSlNo();

}
