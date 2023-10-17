package com.csmpl.wrsis.webportal.service;

import java.util.List;

import com.csmpl.wrsis.webportal.bean.QueryLogViewBean;
import com.csmpl.wrsis.webportal.entity.QueryBuilderLogEntity;

public interface QueryBuilderLogService {

	QueryBuilderLogEntity saveQueryBuilder(QueryBuilderLogEntity qblEntity);

	String addQueryBuilderLog(String query, String ipaddress, int userId);

	String getQueryResponse(Integer logId);

	List<QueryLogViewBean> getQueryList(String startDate, String endDate, Integer start, Integer length);

	Integer getQueryListCount(String startDate, String endDate, Integer pstart, Integer plength);

}
