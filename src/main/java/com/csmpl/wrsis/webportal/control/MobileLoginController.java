package com.csmpl.wrsis.webportal.control;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.ws.WebServiceContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csmpl.adminconsole.webportal.bean.LoginReqDto;
import com.csmpl.adminconsole.webportal.config.MD5PasswordEncoder;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.service.LoginService;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.entity.MobileLoginTracking;
import com.csmpl.wrsis.webportal.entity.MonitorDetailsEntity;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.MobileLogService;
import com.csmpl.wrsis.webportal.service.MobileService;
import com.csmpl.wrsis.webportal.util.DateUtil;
import com.csmpl.wrsis.webportal.util.LatLongUtill;

import nl.bitwalker.useragentutils.UserAgent;

@RequestMapping(value = "/api")
@RestController
public class MobileLoginController {

	private static final Logger LOG = LoggerFactory.getLogger(MobileLoginController.class);

	@Autowired
	LoginService loginService;

	@Autowired
	MobileService mobileService;

	@Autowired
	UserService userService;

	//@Resource
	//private WebServiceContext wsc;

	@Autowired
	CommonService commonService;

	@Autowired
	MobileLogService mobileLogService;
	
	@Autowired
	LatLongUtill latlongAPI ;
	
	@Autowired
	DashboardService dashboardService;


	// API for mobile app login
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public ResponseEntity<String> mobileUserLogin(@RequestBody LoginReqDto loginReqDto, HttpServletRequest request)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			// Mobile Tracking Info Start

			String ipaddress = null;
			ipaddress = loginReqDto.getIpaddress();
			loginReqDto.setIpaddress(ipaddress);

			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
			loginReqDto.setOsName(userAgent.getOperatingSystem().getName());
			
			LOG.info("MobileLoginController:mobileUserLogin():DeviceType-"+userAgent.getOperatingSystem().getDeviceType().getName());
			LOG.info("MobileLoginController:mobileUserLogin():Manufactur-"+userAgent.getOperatingSystem().getManufacturer().getName());
			
			
			MobileLoginTracking ipinfo = new MobileLoginTracking();
			ipinfo.setIpaddress(loginReqDto.getIpaddress());
			ipinfo.setOsName(loginReqDto.getOsName());
			if (loginReqDto.getDeviceId() == null || "".equals(loginReqDto.getDeviceId())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_AFTER_SOME_TIME);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			} else {
				ipinfo.setDeviceName(loginReqDto.getDeviceId());
			}
			if (loginReqDto.getSuscriberId() == null || "".equals(loginReqDto.getSuscriberId())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_AFTER_SOME_TIME);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			} else {
				ipinfo.setSubscriberName(loginReqDto.getSuscriberId());
			}
			if (loginReqDto.getImeiId() == null || "".equals(loginReqDto.getImeiId())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_AFTER_SOME_TIME);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			} else {
				ipinfo.setImeiNo(loginReqDto.getImeiId());
			}
			if (loginReqDto.getFcmId() == null || "".equals(loginReqDto.getFcmId())) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_AFTER_SOME_TIME);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			} else {
				ipinfo.setFcmName(loginReqDto.getFcmId());
			}

			ipinfo.setCreatedOn(new Timestamp(new Date().getTime()));
			ipinfo.setBitStatus(false);
			ipinfo = mobileService.saveMobileTrackInfo(ipinfo);
			// Mobile Tracking Info End

			User userDetails = loginService.findByUserName(loginReqDto.getUsername());
			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			
			// valid password check
			String existPassword = userDetails.getPassword();
			String enteredPassword = loginReqDto.getPassword();
			if (!new MD5PasswordEncoder().matches(enteredPassword, existPassword)) {

				
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			

			JSONObject mUserDetails = loginService.mUserDetails(loginReqDto.getUsername());

			if(mUserDetails.getJSONObject("userProfileData").getString("firstlogin").equalsIgnoreCase("y")) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Please change your password before logging on the first time.");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

			}else{
				
				JSONObject surveyMasterData=null;
				JSONObject implementationMasterData=null;
				JSONObject incidentMasterData=null;
				JSONObject recomListForSurvey=null;
				try {
					JSONArray roleArray=mUserDetails.getJSONObject("userProfileData").getJSONArray("roleData");
					String roleName=null;
					
					MobileReqBean reqObj=new MobileReqBean();
					reqObj.setUserId(userDetails.getUserId());
					boolean devFlag=false;
					boolean suvFlag=false;
					
					//Retrieve master data as per the role
					for (int i = 0, size = roleArray.length(); i < size; i++)
					 {
						JSONObject objectInArray = roleArray.getJSONObject(i);
						roleName = objectInArray.getString("rolName");
						
						if("WOREDA_EXPERTS".equalsIgnoreCase(roleName)) {
							implementationMasterData = mobileService.getMasterforSurveyImplementation(reqObj);
							recomListForSurvey = mobileService.viewRecommendationData(reqObj);

						}else if("DEVELOPMENT_AGENTS".equalsIgnoreCase(roleName) || "TRAINED_FARMERS".equalsIgnoreCase(roleName)) {
							if(!devFlag) {
								incidentMasterData = mobileService.getMasterforRustIncident(reqObj);
								devFlag=true;
							}
						}else {
							if(!suvFlag) {
								surveyMasterData = mobileService.getSurveyMasterData(userDetails.getUserId());
								suvFlag=true;
							}
						}
						
					 }//end of for loop
				}catch (Exception e) {
					LOG.error("MobileLoginController::mobileUserLogin():Exception on master data retrive" + e);
				}
				
				mUserDetails.put("surveyMasterData", (surveyMasterData!=null) ? surveyMasterData : "");
				mUserDetails.put("incidentMasterData", (incidentMasterData!=null) ? incidentMasterData : "");
				mUserDetails.put("implementationMasterData", (implementationMasterData!=null) ? implementationMasterData : "");
				mUserDetails.put("recommendationListForSurvey", (recomListForSurvey!=null) ? recomListForSurvey : "");
				
				
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, mUserDetails);
				ipinfo.setCreatedBy(userDetails.getUserId());
				ipinfo.setLoginStatus(true);
				ipinfo = mobileService.saveMobileTrackInfo(ipinfo);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::mobileUserLogin():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	//API for view user profile
	@RequestMapping(value = "/userProfile", method = RequestMethod.POST)
	public ResponseEntity<String> userProfile(@RequestBody MobileReqBean loginReqDto, HttpServletRequest request)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject mUserDetails = mobileService.mUserProfileDetails(loginReqDto.getUserId());
			if (mUserDetails.length() != 0) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, mUserDetails);
			} else {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Please change your password before logging on the first time.");
				json.put(WrsisPortalConstant.RESPONSE, mUserDetails);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::userProfile():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	//API for profile Image
	@RequestMapping(value = "/public/profile_image", method = RequestMethod.GET)
	void loadProfileImageInformation(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "imagePath", required = false) String path) throws IOException {
		BufferedInputStream inputStream = null;
		File file = null;
		if (path == null || path.isEmpty()) {
			try {
				inputStream = new BufferedInputStream(
						new FileInputStream(new File(request.getServletContext().getRealPath("/") + "wrsis"
								+ File.separator + "images" + File.separator + "blank_mobile_profile_image.png")));
				OutputStream outputStream = response.getOutputStream();
				byte[] bytes = new byte[1024 * 2];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, bytesRead);
				}
				outputStream.flush();

			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		} else {

			path = new String(Base64.getDecoder().decode(path));
			try {
				file = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.USER_PROFILE_IMG_UPLOAD_PATH
						+ File.separator + path);
				if (!file.exists()) {
					inputStream = new BufferedInputStream(
							new FileInputStream(new File(request.getServletContext().getRealPath("/") + "wrsis"
									+ File.separator + "images" + File.separator + "blank_mobile_profile_image.png")));
					OutputStream outputStream = response.getOutputStream();
					byte[] bytes = new byte[1024 * 2];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, bytesRead);
					}
					outputStream.flush();
				} else {
					inputStream = new BufferedInputStream(new FileInputStream(
							new File(WrsisPortalConstant.wrsisPropertiesFileConstants.USER_PROFILE_IMG_UPLOAD_PATH
									+ File.separatorChar + path)));
					OutputStream outputStream = response.getOutputStream();
					byte[] bytes = new byte[1024 * 2];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, bytesRead);
					}
					outputStream.flush();
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
	}
	
	//API for mobile logout
	@RequestMapping(value = "/mobileUserLogout", method = RequestMethod.POST)
	public ResponseEntity<String> mobileUserLogout(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			String lououtStatus = mobileService.mobileUserLogout(loginReqDto);

			JSONObject wjson = new JSONObject(lououtStatus);

			if (wjson.has(WrsisPortalConstant.STATUS_CODE) && wjson.getInt(WrsisPortalConstant.STATUS_CODE) == 200) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Logout successfully");
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::mobileUserLogout():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}


	public String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || WrsisPortalConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	
	// API for get dashboard count
	
		@RequestMapping(value = "/viewDashBoardCount", method = RequestMethod.POST)
		public ResponseEntity<String> viewDashBoardCount(@RequestBody MobileReqBean loginReqDto) throws JSONException {

			JSONObject json = new JSONObject();

			try {

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONObject dashBoardCount = dashboardService.getDashboardContent(loginReqDto.getUserId());

				if (dashBoardCount != null) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
					json.put(WrsisPortalConstant.RESPONSE, dashBoardCount);
				}

			} catch (Exception e) {
				LOG.error("MobileLoginController::viewDashBoardCount():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
		
		//API for get all  master data 
		@RequestMapping(value = "/getAllMasterData", method = RequestMethod.POST)
		public ResponseEntity<String> getAllMasterData(@RequestBody MobileReqBean loginReqDto)
				throws JSONException {

			JSONObject json = new JSONObject();
			JSONObject response = new JSONObject();
			try {
				// Mobile Request Log Insert
				Integer logId = mobileLogService.insertMobileRequestDetail("getAllMasterData", loginReqDto);

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONArray roleArray=userService.getUserRolesByUserId(loginReqDto.getUserId());
				
				JSONObject surveyMasterData=null;
				JSONObject implementationMasterData=null;
				JSONObject incidentMasterData=null;
				JSONObject recomListForSurvey=null;
				
				String roleName=null;
				
				boolean devFlag=false;
				boolean suvFlag=false;
				
				//Retrieve master data as per the role
				for (int i = 0, size = roleArray.length(); i < size; i++)
				 {
					JSONObject objectInArray = roleArray.getJSONObject(i);
					roleName = objectInArray.getString("rolName");
					
					if("WOREDA_EXPERTS".equalsIgnoreCase(roleName)) {
						implementationMasterData = mobileService.getMasterforSurveyImplementation(loginReqDto);
						recomListForSurvey = mobileService.viewRecommendationData(loginReqDto);
					}else if("DEVELOPMENT_AGENTS".equalsIgnoreCase(roleName) || "TRAINED_FARMERS".equalsIgnoreCase(roleName)) {
						if(!devFlag) {
							incidentMasterData = mobileService.getMasterforRustIncident(loginReqDto);
							devFlag=true;
						}
					}else {
						if(!suvFlag) {
							surveyMasterData = mobileService.getSurveyMasterData(loginReqDto.getUserId());
							suvFlag=true;
						}
					}
				}
				
				response.put("surveyMasterData", (surveyMasterData!=null) ? surveyMasterData : "");
				response.put("incidentMasterData", (incidentMasterData!=null) ? incidentMasterData : "");
				response.put("implementationMasterData", (implementationMasterData!=null) ? implementationMasterData : "");
				response.put("recommendationListForSurvey", (recomListForSurvey!=null) ? recomListForSurvey : "");
			
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, response);
				
				// Mobile Response Log Update.
				mobileLogService.updateMobileResponseDetail(logId, json);

			} catch (Exception e) {
				LOG.error("MobileLoginController::getAllMasterDataForSurvey():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
	
	//API for get survey master data
	@RequestMapping(value = "/getSurveyMasterData", method = RequestMethod.POST)
	public ResponseEntity<String> getSurveyMasterData(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {
			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("getSurveyMasterData", loginReqDto);

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject masterData = mobileService.getSurveyMasterData(loginReqDto.getUserId());

			if (masterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, masterData);
			}
			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("MobileLoginController::getAllMasterDataForSurvey():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	//API for save survey data
	@RequestMapping(value = "/saveWheatRustSurvey", method = RequestMethod.POST)
	public ResponseEntity<String> saveSurveyDetailsFromMobileSide(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {
			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("saveWheatRustSurve", loginReqDto);
			
			Integer userId = new JSONObject(new String(Base64.getDecoder().decode(loginReqDto.getSurveyData())))
					.getInt("LoginUserId");

			User userDetails = userService.getUserById(userId);

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			
			
			
			JSONObject rawDataLatitude = new JSONObject(
				new String(Base64.getDecoder().decode(loginReqDto.getSurveyData())));
			
			//API call for to get Altitude
			String latitude = rawDataLatitude.getString("latitudeId");
			String longitude = rawDataLatitude.getString("longitudeId");
			String season = rawDataLatitude.getString(WrsisPortalConstant.SEASON_ID);
			LOG.info("MObileLoginController::saveSurveyDetailsFromMobileSide():Latidue,Longitude,Season value :-"+latitude+" "+longitude+" "+season);
			if(latitude!=null && longitude !=null) {
				if(season!=null)
				{
					Boolean checkSeasonLatLongDuplicate=mobileService.checkSeasonLatLongDuplicate(Integer.valueOf(season),
							latitude,longitude);
					if(checkSeasonLatLongDuplicate)
					{
						LOG.info("MObileLoginController::saveSurveyDetailsFromMobileSide():Duplicate LAT-LONG Found");
						json.put(WrsisPortalConstant.STATUS, "409");
						json.put(WrsisPortalConstant.SUCCESS_MSG, "Duplicate entry is not allowed.");
						return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
					}
				}
				String altitude=latlongAPI.getAltitudeFromAPI(latitude, longitude);
				LOG.info("MObileLoginController::saveSurveyDetailsFromMobileSide():Altitude value from service -"+altitude);
				if(altitude!=null) {
					rawDataLatitude.remove("altitudeId");
					rawDataLatitude.put("altitudeId", altitude);
				}
			}
			
			
			JSONArray base64Image = rawDataLatitude.getJSONArray(WrsisPortalConstant.IMAGES);
		    Date sDate = DateUtil.convertStringToDate(rawDataLatitude.getString("surveyDateId"));
			
			JSONArray jsa = new JSONArray();
			int counter = (base64Image.length() == 0) ? 1 : base64Image.length();
			for (int i = 0; i < base64Image.length(); i++) {
				LocalDate localDate = LocalDate.now();
				
				SecureRandom random = new SecureRandom();
				
				String date = String.valueOf(localDate.getDayOfMonth());
				String month =String.valueOf(localDate.getMonth().getValue());
				String year = String.valueOf(localDate.getYear());
				LOG.info("MobileLoginController::saveSurveyDetailsFromMobileSide():Current date:-"+localDate);
				//LOG.info("MobileLoginController::saveSurveyDetailsFromMobileSide():Day-"+date+" Month-"+month+" Year-"+year);
				int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

				String extensionName = "jpg";
				String fileName = "SURVEY_IMAGE_" + randomNo + date + month + year + "_" + counter + "."
						+ extensionName;
				counter++;

				Calendar cal = Calendar.getInstance();
				cal.setTime(sDate); // don't forget this if date is arbitrary
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
				String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
						+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate)
						+ File.separator + fileName; // should be dynamic
				String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH + File.separator
						+ cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate);
				File folderCreate = new File(folder);
				if (!folderCreate.exists()) {
					folderCreate.mkdir();
					Path path = Paths.get(folder);
					Files.createDirectories(path);
				}
				File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
				if (!file1.exists()) {
					file1.mkdir();
				}
				if (!file1.exists()) {
					Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
					Files.createDirectories(path);
				}

				byte[] imageBytes = Base64.getDecoder().decode(base64Image.getString(i));
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
				File outputfile = new File(fileUploadPath);
				ImageIO.write(img, "jpg", outputfile);

				jsa.put(cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate) + File.separator
						+ fileName);

			}
			rawDataLatitude.remove(WrsisPortalConstant.IMAGES);
			rawDataLatitude.put(WrsisPortalConstant.IMAGES, jsa);

			loginReqDto.setSurveyData(new String(Base64.getEncoder().encode(rawDataLatitude.toString().getBytes())));

			
			String surveyData = loginReqDto.getSurveyData();
			String wheatSurveystatus = commonService.saveSurveyDetails(surveyData, null, userId, 2, null, userId);
			JSONObject wjson = new JSONObject(wheatSurveystatus);

			if (wjson.has(WrsisPortalConstant.STATUS_CODE) && wjson.getInt(WrsisPortalConstant.STATUS_CODE) == 200) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Record submitted successfully");
			} else {
				json.put(WrsisPortalConstant.STATUS, "400");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Please try again, after sometime.");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("MobileLoginController::saveSurveyDetailsFromMobileSide():Error while saving mobile survey data" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	
    //API for view survey records
	@RequestMapping(value = "/viewWheatRustSurvey", method = RequestMethod.POST)
	public ResponseEntity<String> viewWheatRustSurvey(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("viewWheatRustSurvey", loginReqDto);

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewSurveyData = mobileService.viewWheatRustSurvey(loginReqDto);

			if (viewSurveyData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewSurveyData);
			}

			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);
		} catch (Exception e) {
			LOG.error("MobileLoginController::viewWheatRustSurvey():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	//API for view survey info detail
	@RequestMapping(value = "/viewWheatRustSurveyDetail", method = RequestMethod.POST)
	public ResponseEntity<String> viewWheatRustSurveyDetails(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject wheatRustSurveyDetail = commonService.getFormmatedJsonBySurveyId(loginReqDto.getSurveyId());

			if (wheatRustSurveyDetail.isNull(WrsisPortalConstant.FUNGISIDE_ID)) {

				wheatRustSurveyDetail.put(WrsisPortalConstant.FUNGISIDE_ID, "");
			}

			if (wheatRustSurveyDetail != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, wheatRustSurveyDetail);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewWheatRustSurveyDetails():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

   //API for view survey images
	@RequestMapping(value = "public/survey_image", method = RequestMethod.GET)
	void loadSurveyImageInformation(HttpServletResponse response,
			@RequestParam(value = "imagePath", required = false) String path) throws IOException {
		path = new String(Base64.getDecoder().decode(path));
		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(
					new FileInputStream(new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
							+ File.separatorChar + path)));
			OutputStream outputStream = response.getOutputStream();
			byte[] bytes = new byte[1024 * 2];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, bytesRead);
			}
			outputStream.flush();

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	//API for view race analysis
	@RequestMapping(value = "/viewRaceAnalysis", method = RequestMethod.POST)
	public ResponseEntity<String> viewRaceAnalysis(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewRaceAnalysisData = mobileService.viewRaceAnalysis(loginReqDto);

			if (viewRaceAnalysisData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewRaceAnalysisData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewRaceAnalysis():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	//API for  view race analysis details
	@RequestMapping(value = "/viewRaceAnalysisDetails", method = RequestMethod.POST)
	public ResponseEntity<String> viewRaceAnalysisDetails(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewRaceAnalysisDetails = mobileService.viewRaceAnalysisDetails(loginReqDto);

			if (viewRaceAnalysisDetails != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewRaceAnalysisDetails);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewRaceAnalysisDeatils():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	//API for download race analysis document
	@RequestMapping(value = "/public/viewDocument", method = RequestMethod.GET)
	void downloadRaceAnalysisfile(HttpServletResponse response,
			@RequestParam(value = "path", required = false) String path) throws IOException {
		path = new String(Base64.getDecoder().decode(path));

		Path file = Paths.get(
				WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH + File.separatorChar + path);
		if (Files.exists(file)) {
			if ((path.substring(path.lastIndexOf(".") + 1, path.length()).trim().toLowerCase()
					.equalsIgnoreCase("pdf"))) {
				response.setContentType("application/pdf");
			} else {
				response.setContentType("application/jpeg");
			}
			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + path);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				LOG.error("MobileLoginController::downloadRaceAnalysisfile():" + ex);
			}
		}

	}

	
	//API for get rust incident master data
	@RequestMapping(value = "/getMasterforRustIncident", method = RequestMethod.POST)
	public ResponseEntity<String> getMasterforRustIncident(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {
			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("getMasterforRustIncident", loginReqDto);

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject forIncidentMasterData = mobileService.getMasterforRustIncident(loginReqDto);

			if (forIncidentMasterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, forIncidentMasterData);
			}

			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);
		}

		catch (Exception e) {
			LOG.error("MobileLoginController::getMasterForRustIncident():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	//API for get rust incident questions
	@RequestMapping(value = "/incidentQuestions", method = RequestMethod.POST)
	public ResponseEntity<String> getAllQuestionData(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			if (loginReqDto.getUserId() == 0) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject masterData = mobileService.getQuestionMasterData();

			if (masterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, masterData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::getAllQuestionData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	//API for save rust incident data
		@RequestMapping(value = "/rustIncident", method = RequestMethod.POST)
		public ResponseEntity<String> saveRustIncident(@RequestBody MobileReqBean loginReqDto) throws JSONException {

			JSONObject json = new JSONObject();

			try {
				// Mobile Request Log Insert
				Integer logId = mobileLogService.insertMobileRequestDetail("rustIncident", loginReqDto);

				User userDetails = userService.getUserById(loginReqDto.getUserId());
				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				String rustIncidentStatus = mobileService.saveRustIncident1(loginReqDto);

				if (WrsisPortalConstant.SAVE.equalsIgnoreCase(rustIncidentStatus)) {
					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Record submitted successfully.");
				} else if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(rustIncidentStatus)) {
					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Record updated successfully.");
				} else {
					json.put(WrsisPortalConstant.STATUS, "400");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Please try again after sometime");
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				// Mobile Response Log Update.
				mobileLogService.updateMobileResponseDetail(logId, json);
			} catch (Exception e) {
				LOG.error("MobileLoginController::saveRustIncident():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
	
		//API for view rust incident details
		@RequestMapping(value = "/viewRustIncident", method = RequestMethod.POST)
		public ResponseEntity<String> viewRustIncident(@RequestBody MobileReqBean loginReqDto) throws JSONException {

			JSONObject json = new JSONObject();

			try {

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONObject viewRustIncidentData = mobileService.viewRustIncident(loginReqDto);

				if (viewRustIncidentData != null) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
					json.put(WrsisPortalConstant.RESPONSE, viewRustIncidentData);
				}

			} catch (Exception e) {
				LOG.error("MobileLoginController::viewRustIncident():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
	//API for view rust incident list 
		@RequestMapping(value = "/viewRustIncidentByList", method = RequestMethod.POST)
		public ResponseEntity<String> viewRustIncidentByUser(@RequestBody MobileReqBean loginReqDto) throws JSONException {

			JSONObject json = new JSONObject();

			try {

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONObject viewRustIncidentData = mobileService.viewRustIncidentByUser(loginReqDto);

				if (viewRustIncidentData != null) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
					json.put(WrsisPortalConstant.RESPONSE, viewRustIncidentData);
				}

			} catch (Exception e) {
				LOG.error("MobileLoginController::viewRustIncidentByUser():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
		//API for view rust incident details
		@RequestMapping(value = "/viewIncidentDetailsByIncidentid", method = RequestMethod.POST)
		public ResponseEntity<String> viewRustIncidentByRustIncident(@RequestBody MobileReqBean loginReqDto)
				throws JSONException {

			JSONObject json = new JSONObject();

			try {

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONObject viewRustIncidentData = mobileService.viewRustIncidentByRustIncident(loginReqDto);

				if (viewRustIncidentData != null) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
					json.put(WrsisPortalConstant.RESPONSE, viewRustIncidentData);
				}

			} catch (Exception e) {
				LOG.error("MobileLoginController::viewRustIncidentByRustIncident():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
		
		
		
	//API for get master data of Survey Implementation	
		@RequestMapping(value = "/getMasterforSurveyImplementation", method = RequestMethod.POST)
		public ResponseEntity<String> getMasterforSurveyImplementation(@RequestBody MobileReqBean loginReqDto)
				throws JSONException {

			JSONObject json = new JSONObject();

			try {

				User userDetails = userService.getUserById(loginReqDto.getUserId());

				if (userDetails == null) {
					json.put(WrsisPortalConstant.STATUS, "401");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

				JSONObject forImplementationMasterData = mobileService.getMasterforSurveyImplementation(loginReqDto);

				if (forImplementationMasterData != null) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
					json.put(WrsisPortalConstant.RESPONSE, forImplementationMasterData);
				}

			} catch (Exception e) {
				LOG.error("MobileLoginController::getMasterforSurveyImplementation():" + e);
				json = new JSONObject();
				json.put(WrsisPortalConstant.STATUS, "500");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

		}
		
	@RequestMapping(value = "/viewRecommendationAndAdvisory", method = RequestMethod.POST)
	public ResponseEntity<String> viewRecommendationAndAdvisory(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewRecommendationAndAdvisory = mobileService.viewRecommendationAndAdvisory(loginReqDto);

			if (viewRecommendationAndAdvisory != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewRecommendationAndAdvisory);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewRecomendationAndAdvisory():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	
	
	

	@RequestMapping(value = "/public/downloadRecommendationfile", method = RequestMethod.GET)
	void downloadExcel(HttpServletResponse response, @RequestParam(value = "path", required = false) String path)
			throws IOException {
		path = new String(Base64.getDecoder().decode(path));


		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					new File(WrsisPortalConstant.wrsisPropertiesFileConstants.RECOMMENDATION_FILE_UPLOAD_PATH
							+ File.separatorChar + path)));
			OutputStream outputStream = response.getOutputStream();
			byte[] bytes = new byte[1024 * 2];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, bytesRead);
			}
			outputStream.flush();

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}

	@RequestMapping(value = "/public/downloadAdvisoryfile", method = RequestMethod.GET)
	void downloadAdvisoryfile(HttpServletResponse response, @RequestParam(value = "path", required = false) String path)
			throws IOException {
		path = new String(Base64.getDecoder().decode(path));


		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					new File(WrsisPortalConstant.wrsisPropertiesFileConstants.ADVISORY_FILE_UPLOAD_PATH
							+ File.separatorChar + path)));
			OutputStream outputStream = response.getOutputStream();
			byte[] bytes = new byte[1024 * 2];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, bytesRead);
			}
			outputStream.flush();

		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

	}

	


	@RequestMapping(value = "/saveSurveyImplementation", method = RequestMethod.POST)
	public ResponseEntity<String> saveSurveyImplementation(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {
		
		JSONArray kebeleDetailsjsa=null;
		Integer monitorId=null;
		Integer recId=null;
		Integer woredaId=null;
		JSONObject json = new JSONObject();
		
		try {
			String decodeJSON = new String(Base64.getDecoder().decode(loginReqDto.getImplementationData().getBytes()));
			JSONObject implementationJson = new JSONObject(decodeJSON);
			Integer userId = implementationJson.getInt("loginUserId");
			
			

			User userDetails = userService.getUserById(userId);

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			if (implementationJson.has("kebelejsa")) {
				kebeleDetailsjsa = implementationJson.getJSONArray("kebelejsa");
				JSONObject jsonObj = kebeleDetailsjsa.getJSONObject(0);
				recId = Integer.valueOf(jsonObj.getString("recomId"));
				woredaId = Integer.valueOf(jsonObj.getString("woredaId"));
				List<MonitorDetailsEntity> monitorDetails = mobileService.getMonitorDetailsByRecIdAndWoredaId(recId,
						woredaId);
				// Check if recommendation Survey is present in table or not
				if (monitorDetails != null && !monitorDetails.isEmpty()) {
					// Check if recommendation Survey is published or discared or pending
					// respectively
					if (monitorDetails.get(0).getMonitorStatus() == 1) {
						json.put(WrsisPortalConstant.STATUS, "200");
						json.put(WrsisPortalConstant.SUCCESS_MSG, "Recommendation survey has already published.");
						return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
					}
					if (monitorDetails.get(0).getMonitorStatus() == 2) {
						json.put(WrsisPortalConstant.STATUS, "200");
						json.put(WrsisPortalConstant.SUCCESS_MSG, "Recommendation survey has discarded.");
						return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
					}
					if (monitorDetails.get(0).getMonitorStatus() == 0) {
						monitorId = monitorDetails.get(0).getMonitorid();
					}
				}

				String implementationStatus  = mobileService.saveSurveyImplementation(loginReqDto.getImplementationData(), 2,
						monitorId);

				if (WrsisPortalConstant.SAVE.equalsIgnoreCase(implementationStatus)) {
					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Record submitted successfully");
				}else if (WrsisPortalConstant.UPDATE.equalsIgnoreCase(implementationStatus)) {
					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Record updated successfully");
				} else {
					json.put(WrsisPortalConstant.STATUS, "400");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Please try again after sometime");
					return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
				}

			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::saveSurveyImplementation():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, "Exception occured !!");
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	

	

	@RequestMapping(value = "/viewRecommendationData", method = RequestMethod.POST)
	public ResponseEntity<String> viewRecommendationData(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewRecommendation = mobileService.viewRecommendationData(loginReqDto);

			if (viewRecommendation != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewRecommendation);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewRecomendationData():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	

	@RequestMapping(value = "/viewImplementation", method = RequestMethod.POST)
	public ResponseEntity<String> viewImplementation(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewImplemmentaionData = mobileService.viewImplementation(loginReqDto);

			if (viewImplemmentaionData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewImplemmentaionData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewImplementation():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/viewDetailsImplementation", method = RequestMethod.POST)
	public ResponseEntity<String> viewDetailsImplementation(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject viewDetailsImplemmentaion = mobileService.viewDetailsImplementation(loginReqDto);

			if (viewDetailsImplemmentaion != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewDetailsImplemmentaion);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewDetailsImplementation():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	
	
	
	
	
	
	@RequestMapping(value = "/viewNotificationByUser", method = RequestMethod.POST)
	public ResponseEntity<String> viewNotificationByUser(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			JSONObject notificationData = mobileService.viewNotificationByUser(loginReqDto);

			if (notificationData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, notificationData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewNotificationByUser():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/updateNotificationStatus", method = RequestMethod.POST)
	public ResponseEntity<String> updateNotificationStatus(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			User userDetails = userService.getUserById(loginReqDto.getUserId());

			if (userDetails == null) {
				
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			String notificationStatus = mobileService.updateNotificationStatus(loginReqDto);

			if ( WrsisPortalConstant.SUCCESS.equalsIgnoreCase(notificationStatus)) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "status updated successfully.");
				
			} else {
				json.put(WrsisPortalConstant.STATUS, "400");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Something went wrong. Please try again.");
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::updateNotificationStatus():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@RequestMapping(value = "/getMasterforRustIncidentMap", method = RequestMethod.POST)
	public ResponseEntity<String> getMasterforRustIncidentMap() throws JSONException {

		JSONObject json = new JSONObject();

		try {

			
			JSONObject forIncidentMapMasterData = mobileService.getMasterforRustIncidentMap();

			if (forIncidentMapMasterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, forIncidentMapMasterData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::getMasterforRustIncidentMap():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/getMasterDataforGIS", method = RequestMethod.POST)
	public ResponseEntity<String> getAllMasterDataForSurveyMap() throws JSONException {

		JSONObject json = new JSONObject();

		try {

			JSONObject forGISMasterData = mobileService.getforGISMasterData();

			if (forGISMasterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, forGISMasterData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::getAllMasterDataForSurveyMap():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	
	@RequestMapping(value = "/getSurveyDataforGIS", method = RequestMethod.POST)
	public ResponseEntity<String> getSurveyDataForSurveyMap(@RequestBody MobileReqBean loginReqDto)
			throws JSONException {

		JSONObject json = new JSONObject();

		try {

			JSONObject forGISMasterData = mobileService.getSurveyDataforGIS(loginReqDto);

			if (forGISMasterData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, forGISMasterData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::geySurveyDataForSurveyMap():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	@RequestMapping(value = "/viewRustIncidentForGis", method = RequestMethod.POST)
	public ResponseEntity<String> viewRustIncidentForGis(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();

		try {

			
			JSONObject viewRustIncidentData = mobileService.viewRustIncidentForGis(loginReqDto);

			if (viewRustIncidentData != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, viewRustIncidentData);
			}

		} catch (Exception e) {
			LOG.error("MobileLoginController::viewRustIncidentForGis():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	

	

	
	// Authentication Token Generation

	@RequestMapping(value = "/getMobileToken", method = RequestMethod.POST)
	public ResponseEntity<String> getMobileToken(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();
		try {
			User user = userService.getUserById(loginReqDto.getUserId());

			if (user == null) {

				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			String token = mobileService.generateMobileToken(loginReqDto.getUserId());

			

			if (token != null) {

				if ("token_exist".equalsIgnoreCase(token)) {

					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Token already generated");

				} else {
					json.put("jtoken", token);
					json.put(WrsisPortalConstant.STATUS, "200");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Token generate sucessfully");

				}

			} else {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Token Generate Failed .Please try after sometime !");

			}

		} catch (Exception e) {

			LOG.error("MobileLoginController::getMobileToken():" + e);

			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}

		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
	}

	// Token Authentication

	public boolean isTokenAuthenticate(Integer userId, String token) {
		return mobileService.authenticateToken(userId, token);

	}

	// Update auth token after used
	public boolean updateAuthTokenAfterUse(Integer userId, String token) {
		return mobileService.updateAuthTokenAfterUse(userId, token);

	}

	// mobile API
	// set Version

	@RequestMapping(value = "/setMobileVersion", method = RequestMethod.POST)
	public ResponseEntity<String> setVersion(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();
		try {

			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("setMobileVersion", loginReqDto);

			if (loginReqDto.getAuthToken() == null || loginReqDto.getAuthToken().isEmpty()) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Auth Token value missing !");

				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			if (loginReqDto.getVersionval() == null) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "App version value required !");

				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			if (loginReqDto.getOsTypeId() == null) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "OS type Id value required !");

				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			// pass token for validation
			if (!isTokenAuthenticate(loginReqDto.getUserId(), loginReqDto.getAuthToken())) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Auth Token value not valid !");

				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}

			String status = mobileService.updateAppVersion(loginReqDto);

			// token value used updated
			updateAuthTokenAfterUse(loginReqDto.getUserId(), loginReqDto.getAuthToken());

			if ("success".equalsIgnoreCase(status)) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "App version updated sucessfully");
			} else {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "App version not updated ");
			}

			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("MobileLoginController::setVersion():" + e);
			json.put(WrsisPortalConstant.STATUS, "200");
			json.put(WrsisPortalConstant.SUCCESS_MSG, "Something Went Wrong .Please try after sometime !");

			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}

		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}

	// Get version
	@RequestMapping(value = "/getMobileVersion", method = RequestMethod.POST)
	public ResponseEntity<String> getVersion(@RequestBody MobileReqBean loginReqDto) throws JSONException {

		JSONObject json = new JSONObject();
		try {
			
		  // Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("getMobileVersion", loginReqDto);

			JSONObject version = mobileService.getAppVersionByOstype(loginReqDto.getOsTypeId());
			if (version != null) {

				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SUCCESSFUL);
				json.put(WrsisPortalConstant.RESPONSE, version);
			}

			// Mobile Response Log Update.
			mobileLogService.updateMobileResponseDetail(logId, json);

		} catch (Exception e) {
			LOG.error("MobileLoginController::getVersion():" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.UNABLE_TO_FETCH_DATA_AS_SERVER_HAS_DOWN_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
	}
	
	//added By Shaktimaan Panda
	//added on 09-09-2021
	//For multi survey save by API
	
	@RequestMapping(value = "/saveMultiSurveyData", method = RequestMethod.POST)
	public ResponseEntity<String> saveMultiSurveyData(@RequestBody MobileReqBean loginReqDto) throws JSONException {
		
		
		JSONObject json = new JSONObject();
		JSONObject statusJson = null;
		JSONArray statusJsonArr = new JSONArray();
		Integer userId = null;
		User userDetails =null;
		try {
			// Mobile Request Log Insert
			Integer logId = mobileLogService.insertMobileRequestDetail("saveMultiSurveyData", loginReqDto);
			
			
			JSONArray jsonArrTemp = new JSONArray(
					new String(Base64.getDecoder().decode(loginReqDto.getSurveyData())));
			
			if(jsonArrTemp!=null && jsonArrTemp.length()>0) {
				for(int index=0;index<jsonArrTemp.length();index++){
					if(!"".equals(jsonArrTemp.opt(index))) {
						JSONObject surveyObj = jsonArrTemp.getJSONObject(index);
						JSONObject surveyObj1=(JSONObject) surveyObj.get("dataWithID");
						userId =  surveyObj1.getInt("LoginUserId");
						break;
					}
				}
			}
			
			if(userId!=null)
				 userDetails = userService.getUserById(userId);

			if (userDetails == null) {
				json.put(WrsisPortalConstant.STATUS, "401");
				json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.INVALID_AUTHENTICATION);
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			
			
			
			JSONArray jsonArr = new JSONArray(
				new String(Base64.getDecoder().decode(loginReqDto.getSurveyData())));
			int count=0;
			
			for(int index=0;index<jsonArr.length();index++){
				
				if(!"".equals(jsonArr.opt(index))) {
					
					
				
				JSONObject surveyObj1 = jsonArr.getJSONObject(index);
				//if(surveyObj1.has("dataWithID")  && surveyObj1.get("dataWithID")!=null){
					JSONObject rawDataLatitude=(JSONObject) surveyObj1.get("dataWithID");
					Integer ItemId =	0;
					try{ ItemId =	surveyObj1.getInt("itemId"); }catch (Exception e) {LOG.error("MObileLoginController::saveMultiSurveyDataFromAPI():Error in getting  the itemId "+e);}
						
					
					
			//API call for to get Altitude
			String latitude = rawDataLatitude.getString("latitudeId");
			String longitude = rawDataLatitude.getString("longitudeId");
			String season = rawDataLatitude.getString(WrsisPortalConstant.SEASON_ID);
			LOG.info("MObileLoginController::saveMultiSurveyDataFromAPI():Latidue,Longitude,Season value :-"+latitude+" "+longitude+" "+season);
			if(latitude!=null && longitude !=null) {
				if(season!=null)
				{
					Boolean checkSeasonLatLongDuplicate=mobileService.checkSeasonLatLongDuplicate(Integer.valueOf(season),
							latitude,longitude);
					if(checkSeasonLatLongDuplicate)
					{
						LOG.info("MObileLoginController::saveSurveyDetailsFromMobileSide():Duplicate LAT-LONG Found");
						statusJson = new JSONObject();
						statusJson.put("ItemId", ItemId);
						statusJson.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.FAILURE);
						statusJsonArr.put(statusJson);
						
						json.put(WrsisPortalConstant.SURVEY_DATA, statusJsonArr);
						json.put(WrsisPortalConstant.STATUS, "409");
						json.put(WrsisPortalConstant.SUCCESS_MSG, "Duplicate LAT-LONG Found");
						return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
					}
				}
				String altitude=latlongAPI.getAltitudeFromAPI(latitude, longitude);
				LOG.info("MObileLoginController::saveMultiSurveyDataFromAPI():Altitude value from service -"+altitude);
				if(altitude!=null) {
					rawDataLatitude.remove("altitudeId");
					rawDataLatitude.put("altitudeId", altitude);
				}
			}
			
			
			JSONArray base64Image = rawDataLatitude.getJSONArray(WrsisPortalConstant.IMAGES);
		    Date sDate = DateUtil.convertStringToDate(rawDataLatitude.getString("surveyDateId"));
			
			JSONArray jsa = new JSONArray();
			int counter = (base64Image.length() == 0) ? 1 : base64Image.length();
			for (int i = 0; i < base64Image.length(); i++) {
				LocalDate localDate = LocalDate.now();
				
				SecureRandom random = new SecureRandom();
				
				String date = String.valueOf(localDate.getDayOfMonth());
				String month =String.valueOf(localDate.getMonth().getValue());
				String year = String.valueOf(localDate.getYear());
				LOG.info("MobileLoginController::saveMultiSurveyDataFromAPI():Current date:-"+localDate);
				
				int	randomNo = random.ints(1001, 9999).findFirst().getAsInt(); //Security hotspot

				String extensionName = "jpg";
				String fileName = "SURVEY_IMAGE_" + randomNo + date + month + year + "_" + counter + "."
						+ extensionName;
				counter++;

				Calendar cal = Calendar.getInstance();
				cal.setTime(sDate); // don't forget this if date is arbitrary
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM");
				String fileUploadPath = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH
						+ File.separator + cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate)
						+ File.separator + fileName; // should be dynamic
				String folder = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH + File.separator
						+ cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate);
				File folderCreate = new File(folder);
				if (!folderCreate.exists()) {
					folderCreate.mkdir();
					Path path = Paths.get(folder);
					Files.createDirectories(path);
				}
				File file1 = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
				if (!file1.exists()) {
					file1.mkdir();
				}
				if (!file1.exists()) {
					Path path = Paths.get(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_IMAGES_PATH);
					Files.createDirectories(path);
				}

				byte[] imageBytes = Base64.getDecoder().decode(base64Image.getString(i));
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
				File outputfile = new File(fileUploadPath);
				ImageIO.write(img, "jpg", outputfile);

				jsa.put(cal.get(Calendar.YEAR) + File.separator + simpleDateFormat.format(sDate) + File.separator
						+ fileName);

			}
			rawDataLatitude.remove(WrsisPortalConstant.IMAGES);
			rawDataLatitude.put(WrsisPortalConstant.IMAGES, jsa);

			loginReqDto.setSurveyData(new String(Base64.getEncoder().encode(rawDataLatitude.toString().getBytes())));

			
			String surveyData = loginReqDto.getSurveyData();
			String wheatSurveystatus = commonService.saveSurveyDetails(surveyData, null, userId, 2, null, userId); 
			JSONObject wjson = new JSONObject(wheatSurveystatus);

			if (wjson.has(WrsisPortalConstant.STATUS_CODE) && wjson.getInt(WrsisPortalConstant.STATUS_CODE) == 200) {
				statusJson = new JSONObject();
				statusJson.put("ItemId", ItemId);
				statusJson.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.SUCCESS);
				statusJsonArr.put(statusJson);
				
				count++;  ///record saved
		//		json.put(WrsisPortalConstant.STATUS, "200");
	//		json.put(WrsisPortalConstant.SUCCESS_MSG, "Record submitted successfully");
			} else {
				statusJson = new JSONObject();
				statusJson.put("ItemId", ItemId);
				statusJson.put(WrsisPortalConstant.STATUS, WrsisPortalConstant.FAILURE);
				statusJsonArr.put(statusJson);
				json.put(WrsisPortalConstant.SURVEY_DATA, statusJsonArr);
				json.put(WrsisPortalConstant.STATUS, "400");
				json.put(WrsisPortalConstant.SUCCESS_MSG, "Please try again, after sometime.");
				
				return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
			}
			// Mobile Response Log Update.
			
		  }//end of Json Array blank checking
		} //end of saving loop
			if(count>0) {
				json.put(WrsisPortalConstant.STATUS, "200");
				json.put(WrsisPortalConstant.SUCCESS_MSG, count+" Record(s) submitted successfully");
				json.put(WrsisPortalConstant.SURVEY_DATA, statusJsonArr);
				LOG.info("MobileLoginController::saveMultiSurveyDataFromAPI():survey Record requst info:-"+jsonArr.length());
				LOG.info("MobileLoginController::saveMultiSurveyDataFromAPI():survey Record saved info:-"+count);
			}else {
					json.put(WrsisPortalConstant.STATUS, "400");
					json.put(WrsisPortalConstant.SUCCESS_MSG, "Please try again, after sometime.");
					json.put(WrsisPortalConstant.SURVEY_DATA, statusJsonArr);
			}
			
			mobileLogService.updateMobileResponseDetail(logId, json);
			
			
			
		} catch (Exception e) {
			LOG.error("MobileLoginController::saveMultiSurveyDataFromAPI():Error while saving mobile survey data" + e);
			json = new JSONObject();
			json.put(WrsisPortalConstant.SURVEY_DATA, statusJsonArr);
			json.put(WrsisPortalConstant.STATUS, "500");
			json.put(WrsisPortalConstant.SUCCESS_MSG, WrsisPortalConstant.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN_LATER);
			return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);
		}
		return new ResponseEntity<>(json.toString(), null, HttpStatus.OK);

	}
	

}
