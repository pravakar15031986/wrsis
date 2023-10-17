package com.csmpl.wrsis.webportal.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.service.LoginService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.IVRExcelCellBean;
import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.repository.GisFileLogRepository;
import com.csmpl.wrsis.webportal.repository.GisMapRepository;
import com.csmpl.wrsis.webportal.service.GisService;
import com.csmpl.wrsis.webportal.service.ManageIVRDataService;
import com.csmpl.wrsis.webportal.service.MobileLogService;
import com.csmpl.wrsis.webportal.util.DateUtil;
import com.csmpl.wrsis.webportal.util.UnzipUtility;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping(value = "/gisapi")
@RestController
public class GisController {

	private static final Logger LOG = LoggerFactory.getLogger(GisController.class);

	static List<String> filePath = new ArrayList<>();
	@Autowired
	GisService gisService;
	
	@Autowired
	LoginService loginService;

	@Autowired
	MobileLogService mobileLogService;

	@Autowired
	GisMapRepository gisMapRepository;

	@Autowired
	GisFileLogRepository gisFileLogRepository;

	@RequestMapping(value = "/uploadGisZipFile", method = RequestMethod.POST)
	public @ResponseBody String uploadGisZipFile(Model model,HttpServletRequest request, @RequestParam("files") MultipartFile file, @RequestParam("uploadDate") String uploadDateString)
			throws JSONException { //added uploadDate by Raman Shrestha
		JSONObject response = new JSONObject();
        String authString =request.getHeader("Authorization");
		
		if(authString==null) {
			//return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			response.put(WrsisPortalConstant.STATUS, "401");
			response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			response.put("Status", WrsisPortalConstant.INVALID_AUTHENTICATION);
			return response.toString();
		}else {
			
			
			 if (!isUserAuthenticated(authString)) {
				//return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				response.put(WrsisPortalConstant.STATUS, "401");
				response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return response.toString();
			 }
		}

		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(ts);
		Calendar cal = Calendar.getInstance();
		LocalDate currentDate = LocalDate.now();

		

		Integer logStatus = 0;
		String logFileSaveStatus = "";
		String storestyledatastatus = "";
		Date currentDatee = null;
		Date uploadDate = null;

		String tdate = java.time.LocalDate.now().toString();

		Date date1 = null;
		
		try {
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD).parse(tdate);
			uploadDate = DateUtil.StringToDate(uploadDateString, WrsisPortalConstant.DATE_FORMAT_DDMMYYYY);
			currentDatee = date1;
			Date date15DayBefore = DateUtil.getBeforeDate(date1, -15);
			if(uploadDateString == null || uploadDate == null || uploadDateString.isEmpty()) {
				response.put(WrsisPortalConstant.SUCCESS_MSG,
						"Invalid upload date format, the valid format is dd-mm-yyyy.");
				response.put(WrsisPortalConstant.STATUS, 401);
				return response.toString();
			}
			
			if(uploadDate.after(currentDatee)) {
				response.put(WrsisPortalConstant.SUCCESS_MSG,
						"Upload date should not be greater than current date");
				response.put(WrsisPortalConstant.STATUS, 401);
				return response.toString();
			}
			if(uploadDate.before(date15DayBefore)) {
				response.put(WrsisPortalConstant.SUCCESS_MSG,
						"Upload date should be within 15 days from current date");
				response.put(WrsisPortalConstant.STATUS, 401);
				return response.toString();
			}
		} catch (ParseException e1) {
			LOG.error("GisController::uploadGisZipFile() date1:" + e1);
		}catch (Exception e) {
			LOG.error("GisController::uploadGisZipFile() :" + e);
		}
		
		//timeStamp = new SimpleDateFormat("ddMMyyyy").format(uploadDate);
		

		try {

//			logStatus = gisFileLogRepository.CheckLogFileExit(currentDatee);
			logStatus = gisFileLogRepository.CheckLogFileExit(uploadDate);
			if(logStatus!=null && logStatus > 0) {
				
				boolean b= gisService.isGisLayerExistDeleteByDate(uploadDate);
				LOG.info("GisController::uploadGisZipFile() : Exist Layer deleted: "+b );
			}

			String result = gisService.inactivePreviousFilesByDate(uploadDate);
//			if (logStatus == 0) {
			if ("success".equalsIgnoreCase(result)) {
				File zipfile = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_ZIP_PATH);
				if (!zipfile.exists()) {
					zipfile.mkdirs();
				}
				LOG.info("GisController::create directory for gis .zip file and get path " + zipfile.getPath());

				String zipfilepath = zipfile.getPath();
				File zipfolderCreated = new File(zipfilepath + File.separator
						+ FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase()) + "_" + timeStamp
						+ ".zip");
				String zipfoldestring = new String(zipfilepath + File.separator
						+ FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase()) + "_" + timeStamp
						+ ".zip");

				File unzipfile = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_UNZIP_PATH);
				if (!unzipfile.exists()) {
					unzipfile.mkdirs();
				}
				LOG.info("GisController::create directory for gis unzip file and get path " + unzipfile.getPath());
				String unzipdestDirectory = unzipfile.getPath();

				String yearMonthUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH
						+ File.separator + cal.get(Calendar.YEAR) + File.separator + currentDate.getMonth()
						+ File.separator + FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase()) + "_"
						+ timeStamp + ".zip"; // should be dynamic
				String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH
						+ File.separator + cal.get(Calendar.YEAR) + File.separator + currentDate.getMonth();
				File folderCreate = new File(folder);
				if (!folderCreate.exists()) {
					folderCreate.mkdir();
					Path path = Paths.get(folder);
					Files.createDirectories(path);
				}
				File filenew = new File(
						WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH);
				if (!filenew.exists()) {
					filenew.mkdir();
				}
				if (!filenew.exists()) {
					Path path = Paths
							.get(WrsisPortalConstant.wrsisPropertiesFileConstants.GIS_FILE_UPLOAD_YEAR_MONTH_PATH);
					Files.createDirectories(path);
				}
				FileCopyUtils.copy(file.getBytes(), new File(yearMonthUploadPath));
				LOG.info("GisController::create directory for gis year month file and get path " + filenew.getPath());

				if (!file.getOriginalFilename().toLowerCase().contains(".zip")) {
					response.put(WrsisPortalConstant.SUCCESS_MSG,
							"Invalid file format.File accept only .zip extension.");
					response.put(WrsisPortalConstant.STATUS, 401);
					return response.toString();
				}

				// save .zip file
				FileCopyUtils.copy(file.getBytes(), new File(zipfolderCreated.getAbsolutePath()));

				LOG.info("GisController::save .zip file to specified path " + zipfolderCreated.getAbsolutePath());

				UnzipUtility unzipper = new UnzipUtility();
				LOG.info("GisController:: Here Extracts a zip file specified by the zipFilePath to a directory ");
				unzipper.unzip(zipfoldestring, unzipdestDirectory);

				LOG.info("GisController:: Here call this saveLogfileDetails() method to store log data into DB Table");
				logFileSaveStatus = gisService
						.saveLogfileDetails(FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase())
								+ "_" + timeStamp + ".zip", yearMonthUploadPath,uploadDate);

				if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(logFileSaveStatus)) {
					LOG.info("GisController::log details saved Successfully "
							+ FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase()) + "_" + timeStamp
							+ ".zip");
				} else {
					response.put(WrsisPortalConstant.SUCCESS_MSG,
							"Something went wrong.Please try again after some times.");
					response.put(WrsisPortalConstant.STATUS, 401);
					return response.toString();
				}

				LOG.info(
						"GisController:: Here call this createStoreAndPublishStyleAndSaveData() method to create store,publisg style and store data into DB Table");
				storestyledatastatus = gisService.createStoreAndPublishStyleAndSaveData(new File(unzipdestDirectory
						+ File.separator + FilenameUtils.removeExtension(file.getOriginalFilename().toLowerCase()) + "_"
						+ timeStamp),uploadDate);

				if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(storestyledatastatus)) {
					
					try {
					if(zipfolderCreated.exists())zipfolderCreated.delete();
				    	FileUtils.cleanDirectory(new File(unzipdestDirectory));
				    	LOG.info("GisController::uploadGisZipFile():unzip file removed...");
					}catch (Exception e) {
						LOG.error("GisController::uploadGisZipFile():error on unzip file remove...");
					}

					response.put(WrsisPortalConstant.SUCCESS_MSG, "File uploaded and store created successfully.");
					response.put(WrsisPortalConstant.STATUS, 200);
					return response.toString();

				} else {
					response.put(WrsisPortalConstant.SUCCESS_MSG, "File uploaded and store created not successfully.");
					response.put(WrsisPortalConstant.STATUS, 200);
					return response.toString();
				}

			} else {
			/*	response.put(WrsisPortalConstant.SUCCESS_MSG, "Today file already uploaded");
				response.put(WrsisPortalConstant.STATUS, 401);
				return response.toString();*/
				response.put(WrsisPortalConstant.SUCCESS_MSG,
						"Unable to upload as server has down. Please try again later.");
				response.put(WrsisPortalConstant.STATUS, 500);
				return response.toString();
			}

		} catch (Exception e) {
			LOG.error("GisController::uploadGisZipFile():" + e);
			response.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to upload as server has down. Please try again later.");
			response.put(WrsisPortalConstant.STATUS, 500);
			return response.toString();
		}

	}

	@RequestMapping(value = "/getUKMetSurveyData", method = RequestMethod.POST)
	public ResponseEntity<String> getGisSurveyData(@RequestBody MobileReqBean loginReqDto,HttpServletRequest request) throws JSONException {

		JSONObject json = new JSONObject();
		
        String authString =request.getHeader("Authorization");
		
		if(authString==null) {
			
			json.put(WrsisPortalConstant.STATUS, "401");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}else {
			
			
			 if (!isUserAuthenticated(authString)) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			 }
		}

		try {

			if (loginReqDto.getFromDate() == null || "".equals(loginReqDto.getFromDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "From date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			if (loginReqDto.getToDate() == null || "".equals(loginReqDto.getToDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "To date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			// Mobile Request Log Insert
			Integer logId = mobileLogService.saveSurveyApiLog("getUKMetSurveyData", loginReqDto);

			loginReqDto.setFromDate(DateUtil.dateformatchange4(loginReqDto.getFromDate()));
			loginReqDto.setToDate(DateUtil.dateformatchange4(loginReqDto.getToDate()));

			JSONObject gisSurveyData = gisService.getGisSurveyData(loginReqDto);

			if (gisSurveyData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisSurveyData);
			}

			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("GisController::getGisSurveyData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getUKMetRawSurveyData", method = RequestMethod.POST)
	public ResponseEntity<String> getUKMetRawSurveyData(@RequestBody MobileReqBean loginReqDto,HttpServletRequest request) throws JSONException {

		JSONObject json = new JSONObject();
        String authString =request.getHeader("Authorization");
		
		if(authString==null) {
			
			json.put(WrsisPortalConstant.STATUS, "401");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}else {
			
			
			 if (!isUserAuthenticated(authString)) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			 }
		}

		try {

			if (loginReqDto.getFromDate() == null || "".equals(loginReqDto.getFromDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "From date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			if (loginReqDto.getToDate() == null || "".equals(loginReqDto.getToDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "To date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			// Mobile Request Log Insert
			Integer logId = mobileLogService.saveSurveyApiLog("getUKMetRawSurveyData", loginReqDto);

			loginReqDto.setFromDate(DateUtil.dateformatchange4(loginReqDto.getFromDate()));
			loginReqDto.setToDate(DateUtil.dateformatchange4(loginReqDto.getToDate()));

			JSONObject gisSurveyData = gisService.getRawGisSurveyData(loginReqDto);

			if (gisSurveyData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisSurveyData);
			}

			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("GisController::getGisSurveyData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/getRustSurveyDataForGlobalRust", method = RequestMethod.POST)
	public ResponseEntity<String> getRustSurveyData(@RequestBody MobileReqBean loginReqDto,HttpServletRequest request) throws JSONException {

		JSONObject json = new JSONObject();
		
		 String authString =request.getHeader("Authorization");
			
			if(authString==null) {
				
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}else {
				
				
				 if (!isUserAuthenticated(authString)) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				 }
			}

		try {

			if (loginReqDto.getFromDate() == null || "".equals(loginReqDto.getFromDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "From date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			if (loginReqDto.getToDate() == null || "".equals(loginReqDto.getToDate())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "To date required !!");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			Integer logId = mobileLogService.saveSurveyApiLog("getRustSurveyDataForGlobalRust", loginReqDto);

			loginReqDto.setFromDate(DateUtil.dateformatchange4(loginReqDto.getFromDate()));
			loginReqDto.setToDate(DateUtil.dateformatchange4(loginReqDto.getToDate()));

			JSONObject gisSurveyData = gisService.getGisSurveyData(loginReqDto);

			if (gisSurveyData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisSurveyData);
			}
			mobileLogService.updateMobileResponseDetail(logId, json);
		} catch (Exception e) {
			LOG.error("GisController::getRustSurveyData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/getLatestGISData", method = RequestMethod.POST)
	public ResponseEntity<String> getLatestGISData(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			

			JSONObject gisLatestData = gisService.getLatestGISData(loginReqDto);

			if (gisLatestData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisLatestData);
			}

		} catch (Exception e) {
			LOG.error("GisController::getLatestGISData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/getGISHistoryData", method = RequestMethod.POST)
	public ResponseEntity<String> getGISHistoryData(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {


			JSONObject gisHistoryData = gisService.getGISHistoryData(loginReqDto);

			if (gisHistoryData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisHistoryData);
			}

		} catch (Exception e) {
			LOG.error("GisController::getGisHistoryData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/getMasterDataForGISMap", method = RequestMethod.POST)
	public ResponseEntity<String> getMasterDataForGISMap() throws JSONException {

		JSONObject json = new JSONObject();

		try {

			

			JSONObject gisMasterData = gisService.getMasterDataForGISMap();

			if (gisMasterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, gisMasterData);
			}

		} catch (Exception e) {
			LOG.error("GisController::getMasterDataForGISMap():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	// Author : Raman Shrestha
	@Autowired
	ManageIVRDataService manageIVRDataService;

	@RequestMapping(value = "/uploadIvrDataAPI", method = RequestMethod.POST)
	public ResponseEntity<String> uploadIvrExcelDataApi(@RequestBody String ivrExcelData,HttpServletRequest request) throws JSONException {
		JSONObject json = null;
		ImportIVRFileBean ivrData = null;
		JSONObject response = new JSONObject();
       String authString =request.getHeader("Authorization");
		
		if(authString==null) {
			
			response.put(WrsisPortalConstant.STATUS, "401");
			response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			return new ResponseEntity<>(response.toString(), null, HttpStatus.OK);
		}else {
			
			
			 if (!isUserAuthenticated(authString)) {
				 response.put(WrsisPortalConstant.STATUS, "401");
				 response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				 return new ResponseEntity<>(response.toString(), null, HttpStatus.OK);
			 }
		}
		try {
			ivrData = new ImportIVRFileBean();
			ivrData.setCellBeanList(extractReqExcelDetailsFromJson(ivrExcelData));
			ivrData.setRecordCountBean(ivrData.getCellBeanList().size());
			String result = manageIVRDataService.saveIvrDataByApi(ivrData);
			json = new JSONObject();
			if ((WrsisPortalConstant.SAVE).equalsIgnoreCase(result)) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Data saved successfully");

			} else if ("fail".equalsIgnoreCase(result)) {
				json.put(WrsisPortalConstant.STATUS, "400");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Bad Request");
			}

		} catch (Exception e) {
			LOG.error("GisController::uploadIvrExcelDataApi():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG,
					"Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
	}

	private List<IVRExcelCellBean> extractReqExcelDetailsFromJson(String excelBeanListString) {
		ObjectMapper mapper = new ObjectMapper();
		List<IVRExcelCellBean> list = null;
		try {
			list = mapper.readValue(excelBeanListString, new TypeReference<List<IVRExcelCellBean>>() {
			});

		} catch (JsonParseException e) {
			LOG.error("GisController::extractReqExcelDetailsFromJson():" + e);
		} catch (JsonMappingException e) {
			LOG.error("GisController::extractReqExcelDetailsFromJson():" + e);
		} catch (IOException e) {
			LOG.error("GisController::extractReqExcelDetailsFromJson():" + e);
		}

		return list;
	}

	// Author : Raman Shrestha
	// API for get IVR questions
	@RequestMapping(value = "/getIVRQuestions")
	public ResponseEntity<String> getIVRQuestionData(HttpServletRequest request) throws JSONException {

		JSONObject json = new JSONObject();
		
		String authString =request.getHeader("Authorization");
		
		if(authString==null) {
			
			json.put(WrsisPortalConstant.STATUS, "401");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}else {
			
			
			 if (!isUserAuthenticated(authString)) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			 }
		}

		try {
			JSONObject masterData = gisService.getIVRQuesMasterData();

			if (masterData != null) {

				json.put("status", "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put("response", masterData);
			}

		} catch (Exception e) {
			LOG.error("GisController::getIVRQuestionData():" + e);
			json = new JSONObject();
			json.put("status", "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, "Unable to fetch data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	
	@RequestMapping(value = "/updateIVRData")
	public ResponseEntity<String> updateIVRData(HttpServletRequest request) throws JSONException {
		
	
		JSONObject json = new JSONObject();

		try {
			String reqDate=request.getParameter("ivrDateBySurvey");
			String queResList = gisService.updateQuestionOptionData();
			String status = gisService.updateIVRData(queResList,(reqDate!=null)?DateUtil.StringToDate(reqDate,WrsisPortalConstant.DATE_FORMAT_YYYYMMDD):new Date());
			JSONObject jsonResponse = new JSONObject(status);
			if (jsonResponse != null && jsonResponse.get("Status").equals("success")) {

				json.put("status", "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put("response", jsonResponse.get("Status"));
			}
			
			} catch (Exception e) {
			LOG.error("GisController::updateIVRData():" + e);
			json = new JSONObject();
			json.put("status", "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, "Unable to save data as server has down. Please try again later.");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
// Basic Authentication	
private boolean isUserAuthenticated(String authString){
        String decodedAuth = "";
        Boolean returnStatus=true;
        // Header is in the format "Basic 5tyc0uiDat4"
        // We need to extract data before decoding it back to original string
        String[] authParts = authString.split("\\s+");
        String authInfo = authParts[1];
        // Decode the data back to original string
        byte[] bytes = null;
        try {
            bytes = Base64.getDecoder().decode(authInfo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        decodedAuth = new String(bytes);
       // System.out.println(decodedAuth);
        String[] credentials = decodedAuth.split(":");
       // System.out.println(credentials[0]+"<----------->"+credentials[1]);
        String userId=credentials[0];
        String enteredPassword=credentials[1];
        
        com.csmpl.adminconsole.webportal.entity.User userDetails = loginService.findByUserName(userId.toLowerCase());
        if(userDetails == null) {
        	
        	 return false;
        }
        String existPassword = userDetails.getPassword();
    
        
        if (!new MD5PasswordEncoder().matches(enteredPassword, existPassword)) {

			
        	returnStatus=false;
		}
        
         
        return returnStatus;
    }	


@RequestMapping(value = "/getStyleDetails", method = RequestMethod.POST)
public ResponseEntity<String> getStyleDetails() throws JSONException {

	JSONObject json = new JSONObject();

	try {

		

		JSONObject gisStyleData = gisService.getStyleDetails();

		if (gisStyleData != null) {

			json.put(WrsisPortalConstant.STATUS, "200");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
			json.put(WrsisPortalConstant.RESPONSE, gisStyleData);
		}

	} catch (Exception e) {
		LOG.error("GisController::getStyleDetails():" + e);
		json = new JSONObject();
		json.put(WrsisPortalConstant.STATUS, "500");
		json.put(WrsisPortalConstant.SUCCESS_MSG,
				"Unable to fetch data as server has down. Please try again later.");
		return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

}



@RequestMapping(value = "/uploadAPKZipFile", method = RequestMethod.POST)
public @ResponseBody String uploadAPKZipFile(Model model,HttpServletRequest request, @RequestParam("files") MultipartFile file)
		throws JSONException { //added  by Debendra Nayak
	JSONObject response = new JSONObject();
    String authString =request.getHeader("Authorization");
	
	if(authString==null) {
		//return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		response.put(WrsisPortalConstant.STATUS, "401");
		response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
		response.put("Status", WrsisPortalConstant.INVALID_AUTHENTICATION);
		return response.toString();
	}else {
		
		
		 if (!isUserAuthenticated(authString)) {
			//return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			response.put(WrsisPortalConstant.STATUS, "401");
			response.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
			return response.toString();
		 }
	}

	//Date date = new Date();
	//long time = date.getTime();
	//Timestamp ts = new Timestamp(time);
	//String timeStamp = new SimpleDateFormat("ddMMyyyy").format(ts);
	//Calendar cal = Calendar.getInstance();
	//LocalDate currentDate = LocalDate.now();

	

	try {

	
			File zipfile = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.APK_FILE_UPLOAD_ZIP_PATH);
			if (!zipfile.exists()) {
				zipfile.mkdirs();
			}
			
			File destzipfile = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.APK_FILE_UPLOAD_OLD_PATH);
			if (!destzipfile.exists()) {
				destzipfile.mkdirs();
			}
			LOG.info("GisController::create directory for apk .zip file and get path " + zipfile.getPath());

			String zipfilepath = zipfile.getPath();
			File zipfolderCreated = new File(zipfilepath + File.separator
					+ FilenameUtils.removeExtension(file.getOriginalFilename()) +".zip");
		
			
			if (zipfile.isDirectory()) {
		         String[] files = zipfile.list();
		         if (files.length > 0) {
		            //System.out.println("The directory " + zipfile.getPath() + " is not empty");
		           if(destzipfile.listFiles().length > 0)
		           {
		        	   destzipfile.listFiles()[0].renameTo(new File(destzipfile.getAbsolutePath()+File.separator+(destzipfile.listFiles()[0].getName().replaceAll(".zip", "_"+System.nanoTime()+".zip"))));
		           }
		            FileUtils.copyDirectory(zipfile,destzipfile );
		         // if file copied successfully then delete the original file
		            zipfile.delete(); 
		           // System.out.println("File moved successfully");
		         } 
			}

			if (!file.getOriginalFilename().contains(".zip")) {
				response.put(WrsisPortalConstant.SUCCESS_MSG,
						"Invalid file format.File accept only .zip extension.");
				response.put(WrsisPortalConstant.STATUS, 401);
				return response.toString();
			}

			// save .zip file
			FileCopyUtils.copy(file.getBytes(), new File(zipfolderCreated.getAbsolutePath()));

			LOG.info("GisController::save .zip file to specified path " + zipfolderCreated.getAbsolutePath());
			
			response.put(WrsisPortalConstant.SUCCESS_MSG, "File uploaded successfully.");
			response.put(WrsisPortalConstant.STATUS, 200);
			return response.toString();
			
			
		

	} catch (Exception e) {
		LOG.error("GisController::uploadAPKZipFile():" + e);
		response.put(WrsisPortalConstant.SUCCESS_MSG,
				"Unable to upload as server has down. Please try again later.");
		response.put(WrsisPortalConstant.STATUS, 500);
		return response.toString();
	}
	//return response.toString();
}


@RequestMapping(value = "/getGISIVRdata", method = RequestMethod.POST)
public ResponseEntity<String> getGISIVRdata(HttpServletRequest request,
		@RequestParam(value="startDate", required = false) String startDate,
		@RequestParam(value="endDate",required = false) String endDate) throws JSONException {

	   JSONObject json = new JSONObject();

	try {

		if (!(startDate.trim().equals("null") || endDate.trim().equals("null")) &&  !(startDate.trim().equals("") || endDate.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
			SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
			endDate = smpl.format(date1);
		}else {
			
			 //If fromDate and toDate is null
			 LocalDate firstDate = LocalDate.now().withDayOfMonth(1);//get current month first date
			 startDate = firstDate.toString();
			 
			 LocalDate currentDate = LocalDate.now();//get current month current date
			 endDate = currentDate.toString();
			
		}

		JSONObject gisIVReData = gisService.getGISIVRdata(startDate,endDate);

		if (gisIVReData != null) {

			json.put(WrsisPortalConstant.STATUS, "200");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
			json.put(WrsisPortalConstant.RESPONSE, gisIVReData);
		}

	} catch (Exception e) {
		LOG.error("GisController::getGISIVRdata():" + e);
		json = new JSONObject();
		json.put(WrsisPortalConstant.STATUS, "500");
		json.put(WrsisPortalConstant.SUCCESS_MSG,
				"Unable to fetch data as server has down. Please try again later.");
		return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

}



@RequestMapping(value = "/getIVRCountDetails", method = RequestMethod.POST)
public ResponseEntity<String> getIVRCountDetails(HttpServletRequest request,
		@RequestParam(value="startDate", required = false) String startDate,
		@RequestParam(value="endDate",required = false) String endDate,@RequestParam(value="regionName",required = false) String regionName,@RequestParam(value="zoneName",required = false) String zoneName,
		@RequestParam(value="woredaName",required = false) String woredaName) throws JSONException {

	   JSONObject json = new JSONObject();

	try {

		if (!(startDate.trim().equals("") || endDate.trim().equals(""))) {
			Date date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(startDate);
			SimpleDateFormat smpl = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
			startDate = smpl.format(date1);
			date1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(endDate);
			endDate = smpl.format(date1);
		}

		JSONObject ivrCountData = gisService.getIVRCountDetails(startDate,endDate,regionName,zoneName,woredaName);

		if (ivrCountData != null) {

			json.put(WrsisPortalConstant.STATUS, "200");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
			json.put(WrsisPortalConstant.RESPONSE, ivrCountData);
		}

	} catch (Exception e) {
		LOG.error("GisController::getIVRCountDetails():" + e);
		json = new JSONObject();
		json.put(WrsisPortalConstant.STATUS, "500");
		json.put(WrsisPortalConstant.SUCCESS_MSG,
				"Unable to fetch data as server has down. Please try again later.");
		return new ResponseEntity<>(json.toString(), null, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

}

	
}
