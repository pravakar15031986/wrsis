package com.csmpl.wrsis.webportal.service;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.csmpl.wrsis.webportal.bean.GisBean;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.entity.MapEntity;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;

public interface GisService {

	JSONObject getGisSurveyData(MobileReqBean loginReqDto);

	JSONObject getLatestGISData(MobileReqBean loginReqDto);

	JSONObject getGISHistoryData(MobileReqBean loginReqDto);

	JSONObject getMasterDataForGISMap();

	String saveLogfileDetails(String fileName, String filePath, Date uploadDate);

	String createStoreAndPublishStyleAndSaveData(File file, Date uploadDate);

	// before search
	List<GisBean> gisForcastingFile(String tdate,String edate);

	// after search
	List<MapEntity> viewAllActiveMap();

	List<RustTypeMasterEntity> viewAllActiveRust();

	List<GisBean> gisForcastingFile(String uploadstartdate, String uploadenddate, String rustTypeId, String mapTypeId,
			String status);

	JSONObject getIVRQuesMasterData();

	int deleteForcastingFile(Date date);

	JSONObject getRawGisSurveyData(MobileReqBean loginReqDto);

	String inactivePreviousFilesByDate(Date uploadDate);

	//String saveIVRQuestionAndOptions(String jsonData) throws JSONException;

	String updateQuestionOptionData();

	String updateIVRData(String queResList, Date date);

	JSONObject getStyleDetails();

	boolean isGisLayerExistDeleteByDate(Date uploadDate);

	JSONObject getGISIVRdata(String startDate, String endDate);

	JSONObject getIVRCountDetails(String startDate, String endDate,String regionName,String zoneName,String woredaName);

}
