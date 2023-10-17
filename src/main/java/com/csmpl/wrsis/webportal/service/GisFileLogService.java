package com.csmpl.wrsis.webportal.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import com.csmpl.wrsis.webportal.bean.GisFileLogBean;
import com.csmpl.wrsis.webportal.bean.LogBean;

public interface GisFileLogService {

	List<GisFileLogBean> gisFileLogMethod(Date uploadstartdate, Date uploadenddate);

	List<GisFileLogBean> gisFileLogMethod();

	void downloadGisFile(HttpServletResponse response, Integer fileId, ModelMap model);

	String gisFileExits(Integer fileId);

	List<LogBean> viewLogFile(String directory, Date uploadstartdate, Date uploadenddate);

}
