package com.csmpl.wrsis.webportal.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.MobileLogViewBean;
import com.csmpl.wrsis.webportal.bean.MobileReqBean;
import com.csmpl.wrsis.webportal.entity.MobileLogEntity;
import com.csmpl.wrsis.webportal.repository.MobileLogRepository;
import com.csmpl.wrsis.webportal.service.MobileLogService;
import com.csmpl.wrsis.webportal.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("mobileLogService")

public class MobileLogServiceImpl implements MobileLogService {

	public static final Logger LOG = LoggerFactory.getLogger(MobileLogServiceImpl.class);

	@Autowired
	MobileLogRepository mobileLogRepository;

	List<String> modalist = new ArrayList<>();
	int totalCount = 0;

	@Override
	public Integer insertMobileRequestDetail(String requestService, MobileReqBean reqObj) {
		MobileLogEntity logObj = new MobileLogEntity();
		try {
			logObj.setServiceName(requestService);
			logObj.setReqDetails(new ObjectMapper().writeValueAsString(reqObj));
			logObj.setReqTime(new Date());
			logObj.setUserId(reqObj.getUserId());
			logObj = mobileLogRepository.save(logObj);

		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::insertMobileRequestDetail():" + e);
		}
		return logObj.getIntMobSerLogId();
	}

	@Override
	public void updateMobileResponseDetail(Integer logId, JSONObject json) {

		try {
			MobileLogEntity logObj = mobileLogRepository.getOne(logId);
			if (logObj != null) {
				logObj.setResDetails(json.toString());
				logObj.setResTime(new Date());
				mobileLogRepository.save(logObj);
			}

		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::updateMobileResponseDetail():" + e);

		}

	}

	public List<MobileLogViewBean> getDataList(String startDate, String endDate, Integer start, Integer length) {

		List<MobileLogViewBean> list = new ArrayList<>();
		MobileLogViewBean vo = null;
		Integer count = 0;

		try {
			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);
				startDate = tdate;
			} else {
				startDate = DateUtil.StringMMMToDate(startDate).toString();
			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);
				endDate = tdate;
			} else {
				endDate = DateUtil.StringMMMToDate(endDate).toString();
			}

		} catch (Exception ee) {
			LOG.error("MobileLogServiceImpl::getDataList():" + ee);

		}

		try {
			List<Object[]> objList = mobileLogRepository.getDataList(startDate, endDate, start, length, -1);

			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new MobileLogViewBean();
					vo.setsNo(start + (++count));
					vo.setServiceName(String.valueOf(obj[0]));
					vo.setReqDetails(String.valueOf(obj[1]));//
					vo.setModalLink("<button class=\"btn btn-primary\"	onclick=\"userReqInfo(" + (String) obj[10]
							+ ")\"><i class=\"fa fa-info\" aria-hidden=\"true\"></i></button>");

					vo.setModalLinkRes("<button class=\"btn btn-primary\" onclick=\"userResInfo(" + (String) obj[10]
							+ ")\"><i class=\"fa fa-info\" aria-hidden=\"true\"></i></button>");
					vo.setReqTime(String.valueOf(obj[2]));

					if ((obj[3]) != null) {

						vo.setResDetails(String.valueOf(obj[3]));

					} else {
						vo.setResDetails(" ");
					}

					if ((obj[4]) != null) {
						vo.setResTime(String.valueOf(obj[4]));
					} else {
						vo.setResTime("");
					}

					vo.setFullName(String.valueOf(obj[5]));
					vo.setUserName(String.valueOf((obj[6])));
					vo.setDesignation(String.valueOf(obj[7]));
					vo.setRoll(String.valueOf(obj[8]));
					vo.setOrganisation(String.valueOf(obj[9]));

					list.add(vo);
					modalist.add(String.valueOf(obj[1]));

				}
			}
		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::getDataList():" + e);

		}

		return list;

	}

	@Override
	public Integer getDataListCount(String startDate, String endDate, Integer pstart, Integer plength) {

		Integer totalCount = 0;

		try {
			if (startDate == null || startDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				startDate = tdate;

			} else {

				startDate = DateUtil.StringMMMToDate(startDate).toString();

			}

			if (endDate == null || endDate.equalsIgnoreCase("")) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				LocalDateTime now = LocalDateTime.now();
				String tdate = dtf.format(now);

				endDate = tdate;

			} else {

				endDate = DateUtil.StringMMMToDate(endDate).toString();

			}
			totalCount = mobileLogRepository.getDataListCount(startDate, endDate, pstart, plength, -1);
		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::getDataListCount():" + e);

		}

		return totalCount;

	}

	@Override
	public List<MobileLogViewBean> viewMobileLogDeatiles(SearchVo searchVo) {

		return Collections.emptyList();
	}

	@Override
	public String getRequestResponse(Integer logId) {

		JSONObject jobj = null;
		try {

			String startDate = "";
			String endDate = "";
			Integer start = -1;
			Integer length = -1;

			List<Object[]> reqresList = mobileLogRepository.getDataList(startDate, endDate, start, length, logId);
			{
				jobj = new JSONObject();
				String val = null;
				for (Object[] dObject : reqresList) {
					val = (String) dObject[1];

					

					jobj.put("requestVal", val);
				}
			}

		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::viewMobileLogDeatiles():" + e);

		}
		return jobj.toString();
	}

	@Override
	public String getResponse(Integer logId) {

		JSONObject jobjj = null;
		try {

			String startDate = "";
			String endDate = "";
			Integer start = -1;
			Integer length = -1;

			List<Object[]> resList = mobileLogRepository.getDataList(startDate, endDate, start, length, logId);
			
				jobjj = new JSONObject();
				String val = null;
				for (Object[] dObject : resList) {
					val = (String) dObject[3];

					jobjj.put("responseVal", val);
				}
			

		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::getResponse():" + e);

		}
		return jobjj.toString();
	}

//insert
	@Override
	public Integer saveSurveyApiLog(String requestService, MobileReqBean loginReqDto) {

		MobileLogEntity logObj = new MobileLogEntity();
		try {
			logObj.setServiceName(requestService);
			logObj.setReqDetails(
					"{\"fromDate\":" + loginReqDto.getFromDate() + ",\"toDate\":" + loginReqDto.getToDate() + "}");

			logObj.setReqTime(new Date());
			logObj.setUserId(0);
			logObj = mobileLogRepository.save(logObj);

		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::saveSurveyApiLog():" + e);

		}
		return logObj.getIntMobSerLogId();

	}

	@Override
	public List<MobileLogViewBean> getUKMetAndGlobalRustAPILog(String startDate, String endDate) {
		List<MobileLogViewBean> list = new ArrayList<>();
		MobileLogViewBean vo = null;
		try {

			List<Object[]> objList = mobileLogRepository.uKMetAndSurvey(DateUtil.StringMMMToDate(startDate),
					DateUtil.StringMMMToDate(endDate));
			if (!objList.isEmpty()) {
				for (Object[] obj : objList) {
					vo = new MobileLogViewBean();

					vo.setServiceName(String.valueOf(obj[1]));//

					vo.setReqTime(String.valueOf(obj[3]));//

					vo.setResTime(String.valueOf(obj[5]));//
					vo.setModalLink("<button class=\"btn btn-primary\"	onclick=\"userReqInfo(" + obj[0]
							+ ")\"><i class=\"fa fa-info\" aria-hidden=\"true\"></i></button>");

					vo.setModalLinkRes("<button class=\"btn btn-primary\" onclick=\"userResInfo(" + obj[0]
							+ ")\"><i class=\"fa fa-info\" aria-hidden=\"true\"></i></button>");

					if ((obj[2]) != null) {

						vo.setReqDetails(String.valueOf(obj[2]));
					} else {
						vo.setResDetails(" ");
					}

					if ((obj[4]) != null) {
						vo.setResDetails(String.valueOf(obj[4]));
					} else {
						vo.setResTime("");
					}

					list.add(vo);
				}
			}
		} catch (Exception e) {
			LOG.error("MobileLogServiceImpl::getUKMetAndGlobalRustAPILog():" + e);

		}
		return list;

	}

}
