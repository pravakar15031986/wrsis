package com.csmpl.wrsis.webportal.service;

import java.util.List;

import org.json.JSONObject;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.MobileLogViewBean;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;

public interface MobileLogService {

	Integer insertMobileRequestDetail(String serviceName, MobileReqBean reqObj);

	void updateMobileResponseDetail(Integer logId, JSONObject json);
	// method for view

	List<MobileLogViewBean> getDataList(String startDate, String endDate, Integer start, Integer length);

	Integer getDataListCount(String startDate, String endDate, Integer pstart, Integer plength);

	List<MobileLogViewBean> viewMobileLogDeatiles(SearchVo searchVo);

	String getRequestResponse(Integer logId);

	String getResponse(Integer logId);

	Integer saveSurveyApiLog(String string, MobileReqBean loginReqDto);

	List<MobileLogViewBean> getUKMetAndGlobalRustAPILog(String startDate, String endDate);

}
