package com.csmpl.wrsis.webportal.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.wrsis.webportal.bean.RecommendationBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;

public interface RecomendationService {

	String addRecommmendation(RecommendationBean recom);

	List<RecommendationBean> viewAllRecommendation();

	String recommendFileExits(Integer fileId);

	void downloadRecommendFile(HttpServletResponse response, Integer fileId);

	String publishRecommendtion(Integer recommendId, Integer userId);

	String deleteRecommendtion(Integer recommendId, Integer userId);

	List<RecommendationBean> searchRecommendation(String sDate, String eDate, String recpmmendNummber,
			Integer recomType);

	RecommendationBean findByRecommendId(Integer recommendId);

	List<RecommendationBean> getRecommendation(SearchVo searchVo);

	String recommendationFileExists(Integer downId);

	void downloadRecommendationFile(HttpServletResponse response, Integer downId);

	RecommendationBean recDetails(int recomendId);

	String updateRecForwardByBoa(RecommendationBean recDetails);

	List<RecommendationBean> getRecommendationsForwardedByBoa(SearchVo searchVo);

	List<RecommendationBean> getMoaRecommendations(SearchVo searchVo);

	String getFungicideDetails();

	List<RecommendationBean> getMoaRecommendationsforWoredaExperts(SearchVo searchVo);

	List<RecommendationBean> getRecLocationDetailsByRecId(int recomendId, int userId);

	List<RecommendationBean> searchRecommendationForRegionSpesific(String sDate, String eDate, Integer regionId,
			Integer zoneId, Integer woredaId, Integer kebeleId, String recpmmendNummber);

	Object getMoaRecommendationsforWoredaExpertsImplementation(SearchVo searchVo);

	List<DemographicEntity> fetchAllUserWiseImplementionRegion(int userId);

	List<RecommendationBean> viewAllRecommendationByPage(Integer recommendType, String recommendNo, String sDate,
			String eDate, Integer userId, Integer processId, Integer actionStartus, Integer pstart, Integer pLength);

	List<RecommendationBean> viewAllRecommendationPage(Integer recommendType, String recommendNo, String sDate,
			String eDate, Integer pstart, Integer pLength);

	List<RecommendationBean> viewAddMonitorRecommendation(String recDtFrom, String recDtTo, String recNo,
			Integer userId, Integer pstart, Integer pLength);

	Integer viewAddMonitorRecommendationCount(String recDtFrom, String recDtTo, String recNo, Integer userId,
			Integer pstart, Integer pLength);

	List<RecommendationBean> viewRecommendationByBOA(Integer recomType, String recommendNo,
			String startDate, String endDate, Integer pstart, Integer pLength, Integer userId);

	Integer viewRecommendationByBOACount(Integer recomType, String recommendNo, String startDate,
			String endDate, Integer pstart, Integer pLength, Integer userId);

	List<RecommendationBean> viewForwardedByRecommendationByBOA(Integer recomType, String recommendNo,
			String startDate, String endDate, Integer pstart, Integer pLength, Integer userId);

	Integer viewForwardedByRecommendationByBOACount(Integer recomType, String recommendNo,
			String startDate, String endDate, Integer pstart, Integer pLength, Integer userId);

	List<RecommendationBean> viewMOARecommendationPage(String sDate, String eDate, String recommendNo, Integer pstart,
			Integer pLength);

	Integer viewMOARecommendationPageCount(String sDate, String eDate, String recommendNo, Integer pstart,
			Integer pLength);

	

	/**
	 * @param recommendId
	 * @return
	 * List<UserBean>
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  29-10-2020
	 */
	List<UserBean> getBoaDetails(Integer recommendId);

	/**
	 * @param recommendId
	 * void
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  11-02-2020
	 */
	String getRecommendNoById(Integer recommendId);
	
	/**
	 * @param recomendId
	 * void
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  11-02-2020
	 */
	List<UserBean> getWoredaDetailsByRecommendationId(int recomendId);

	/**
	 * @param recomendId
	 * @return
	 * String
	 * @author  Shaktimaan Panda
	 * @version 1.0
	 * @since  00-00-2020
	 */
	String getSmsContentByById(int recomendId);

	String getRecLocDetailsByUserIdAndRecId(int userId, int recId);

	String getRecLocDetailsByRecId(int recId);

	String getForwardedRecLocDetailsByRecId(int recId);

	
}
