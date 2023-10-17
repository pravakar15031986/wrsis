package com.csmpl.wrsis.webportal.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.AdvisoryBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.AdvisoryEntiry;

public interface AdvisoryService {

	String addAdvisoryDetails(AdvisoryBean advDetails);

	List<AdvisoryBean> viewAdvisory(SearchVo searchvo);

	List<AdvisoryEntiry> viewAllPublishedAdvisory();

	List<AdvisoryEntiry> viewAllPublishedAdvisory(Date startDate, Date endDate, String advNo);

	String advisoryFileExits(Integer fileId);

	void downloadAdvisoryFile(HttpServletResponse response, Integer fileId);

	String deleteAdvisory(HttpServletRequest request, int advisoryId);

	String publishAdvisory(HttpServletRequest request, int advisoryId);

	AdvisoryBean advDetails(int advisoryId);

	List<AdvisoryBean> viewPublishedAdvisory(SearchVo searchVo);

	String advisoryFileExists(Integer downId);

	List<AdvisoryBean> viewAdvisoryPageWise(String startDate, String endDate, String advisoryNo, Integer pStart,
			Integer pLength);

	List<AdvisoryBean> viewAdvisoryPageWiseApp(String startDate, String endDate, String advisoryNo,
			Integer actionStatus, Integer pStart, Integer pLength);

	List<AdvisoryBean> viewPublishedAdvisoryPage(String startDate, String endDate, String advisoryNo, Integer pStart,
			Integer pLength);

	Integer viewPublishedAdvisoryPageCount(String startDate, String endDate, String advisoryNo, Integer pStart,
			Integer pLength);

	AdvisoryEntiry findByAdvisoryId(int advisoryId);

	List<UserBean> getUserDetails(int userId);
}
