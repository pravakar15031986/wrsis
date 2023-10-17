package com.csmpl.wrsis.webportal.serviceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.entity.RaceAnalysis;
import com.csmpl.wrsis.webportal.entity.RaceAnalysisInoculation;
import com.csmpl.wrsis.webportal.entity.RaceAnalysisScore;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SeasonMonth;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.excel.RaceAnalysisScoreRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisInoculationRepository;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMonthRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.util.EmailUtil;

@Service("raceAnalysisService")
public class RaceAnalysisServiceImpl implements com.csmpl.wrsis.webportal.service.RaceAnalysisService {

	public static final Logger LOG = LoggerFactory.getLogger(RaceAnalysisServiceImpl.class);

	@Autowired
	RaceAnalysisInoculationRepository raceAnalysisInoculationRepository;
	@Autowired
	RaceAnalysisScoreRepository raceAnalysisScoreRepository;
	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;
	@Autowired
	RaceAnalysisRepository raceAnalysisRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;
	@Autowired
	SeasonMonthRepository seasonMonthRepository;

	@SuppressWarnings("unused")
	@Override
	public void saveIncoulationDetails(String repeatationEncodeJson, Integer userId, Integer rustTypeId)
			throws JSONException {
		JSONObject rawJson = new JSONObject(new String(Base64.getDecoder().decode(repeatationEncodeJson.getBytes())));

		JSONArray finalResut = rawJson.getJSONArray("FinalResult");
		JSONArray data = rawJson.getJSONArray("Data");
		String inoculationDate = rawJson.getString("InoculatedDate");
		String recordingDate = rawJson.getString("RecordingDate");
		Integer inoculationId = (rawJson.has(WrsisPortalConstant.INOCULATION_ID))
				? (rawJson.getString(WrsisPortalConstant.INOCULATION_ID).trim().equals("")) ? null : rawJson.getInt(WrsisPortalConstant.INOCULATION_ID)
				: null;

		Optional<RustTypeMasterEntity> rustMaster = rustTypeRepository.findById(rustTypeId);
		boolean isYellow = rustMaster.get().getVchRustType().toLowerCase().contains("yellow");
		// delete the records
		if (inoculationId != null) {
			// remove from inoculation
			
			Optional<RaceAnalysisInoculation> inoculation = raceAnalysisInoculationRepository.findById(inoculationId);
			if (inoculation.isPresent()) {
				inoculation.get().setInoculationDate(convertStringToDate(inoculationDate));
				inoculation.get().setRecordingDate(convertStringToDate(recordingDate));
			}
			raceAnalysisScoreRepository.removeByIoculationId(inoculationId);
		}

		if (finalResut.length() > 0) {
			// result
			String result = "";
			for (int i = 0; i < finalResut.length(); i++) {
				if (isYellow) {
					result += finalResut.getString(i) + ",";
				} else {
					result += finalResut.getString(i);
				}
			}
			if (isYellow) {
				result = result.substring(0, result.length() - 1);
			}

			RaceAnalysisInoculation inoculation = new RaceAnalysisInoculation();
			if (inoculationId == null) {
				inoculation.setBitstatus(false);
				inoculation.setCreatedBy(userId);
				inoculation.setCreatedOn(new Date());
				inoculation.setInoculationStatus(0);
				inoculation.setRaceAnalysisId(Integer.valueOf(data.getJSONObject(0).getString(WrsisPortalConstant.ANALYSIS_ID)));
				inoculation.setRecordingDate(convertStringToDate(recordingDate));
				inoculation.setInoculationDate(convertStringToDate(inoculationDate));
				inoculation.setRepeatationNo(1);
				inoculation.setUpdatedBy(userId);
				inoculation.setUpdatedOn(new Date());

				if (!isYellow) {
					inoculation.setResult(result);
				} else {
					inoculation.setResult(raceAnalysisInoculationRepository.getRaceName(result.trim()));
					inoculation.setVchYellowResult(result.trim());
				}

				inoculation = raceAnalysisInoculationRepository.save(inoculation);
				Integer analysisCount = raceAnalysisInoculationRepository
						.findByAnalysisId(Integer.valueOf(data.getJSONObject(0).getString(WrsisPortalConstant.ANALYSIS_ID)));
				inoculation.setRepeatationNo(analysisCount);
				inoculation = raceAnalysisInoculationRepository.save(inoculation);
				// update analysis count
				raceAnalysisRepository.updateAnalysisCount(analysisCount,
						Integer.valueOf(data.getJSONObject(0).getString(WrsisPortalConstant.ANALYSIS_ID)));
			} else {
				Optional<RaceAnalysisInoculation> inoculation1 = raceAnalysisInoculationRepository
						.findById(inoculationId);
				if (!isYellow) {
					inoculation1.get().setResult(result);
				} else {
					inoculation1.get().setResult(raceAnalysisInoculationRepository.getRaceName(result.trim()));
					inoculation1.get().setVchYellowResult(result.trim());
				}
				raceAnalysisInoculationRepository.save(inoculation1.get());
			}

			
			
			for (int i = 0; i < data.length(); i++) {
				JSONObject json = data.getJSONObject(i);
				String analysisId = json.getString(WrsisPortalConstant.ANALYSIS_ID);
				String wheatLineId = json.getString("WheatLineId");
				String HL = json.getString("HL");
				String InfectionType = json.getString("InfectionType");
				String remark = json.getString("Remark");

				RaceAnalysisScore score = new RaceAnalysisScore();
				score.setBitstatus(false);
				score.setCreatedBy(userId);
				score.setCreatedOn(new Date());
				score.setHlValue(HL);
				score.setWheatLineId(Integer.valueOf(wheatLineId));
				score.setInoculationId((inoculationId == null) ? inoculation.getRaceIncoulationId() : inoculationId);
				score.setRaceAnalysisId(Integer.valueOf(data.getJSONObject(0).getString(WrsisPortalConstant.ANALYSIS_ID)));
				score.setRemarks(remark);
				score.setUpdatedBy(userId);
				score.setUpdatedOn(new Date());
				score.setCreatedOn(new Date());
				score.setInfType(String.valueOf(InfectionType));
				raceAnalysisScoreRepository.save(score);

			}

			// save all in analysis score db

		}

	}

	public static Date convertStringToDate(String dateString) {
		Date date = null;
		DateFormat df = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		try {
			date = df.parse(dateString);
		} catch (Exception ex) {
			LOG.error("RaceAnalysisServiceImpl::convertStringToDate():" + ex);
		}
		return date;
	}

	

	@Override
	public String updateLab(SampleLabTagBean sampleDetails) {
		String sts = "";
		try {
			SampleLabTagDetails sample = sampleLabTagDetailsRepository
					.findBySampleLabTagId(sampleDetails.getSampleLabTagId());
			if (sample != null) {
				if (sampleDetails.getStatus().equals("true")) {
					sample.setResearchCenterId(0);
				} else {
					sample.setResearchCenterId(sampleDetails.getResearchCenterId());
				}
				sample.setUpdatedBy(sampleDetails.getUpdatedBy());
				sample.setUpdatedOn(new Date());
				sample.setExternallab(Boolean.parseBoolean(sampleDetails.getStatus()));
				sampleLabTagDetailsRepository.save(sample);
				sts = WrsisPortalConstant.SUCCESS;
			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisServiceImpl::updateLab():" + e);
			sts = "fail";
		}
		return sts;
	}

	@Override
	public void publishRaceAnalysis(String fileName, Integer analysisId, String finalResult, Integer sampleId,
			Integer userId) {

		// save in sample tag details

		Optional<SampleLabTagDetails> sampleTagDtls = sampleLabTagDetailsRepository.findById(sampleId);
		sampleTagDtls.get().setUpdatedBy(userId);
		sampleTagDtls.get().setUpdatedOn(new Date());
		sampleTagDtls.get().setRaceStatus(2);
		sampleTagDtls.get().setRaceResult(finalResult);
		sampleLabTagDetailsRepository.save(sampleTagDtls.get());

		// save race table
		Optional<RaceAnalysis> raceAnalysis = raceAnalysisRepository.findById(analysisId);
		raceAnalysis.get().setRaceResult(finalResult);
		raceAnalysis.get().setRaceDoc(fileName);
		raceAnalysis.get().setUpdatedBy(userId);
		raceAnalysis.get().setUpdatedOn(new Date());
		raceAnalysis.get().setPublishStatus(true);
		raceAnalysisRepository.save(raceAnalysis.get());

		Integer surveyId = raceAnalysisRepository.fetchSurveyIdByAnalysisId(analysisId);

		MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(7);
		DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
		String newDashBoardTxt = mailsms.getNotification();
		newDashBoardTxt = newDashBoardTxt.replace("@result", finalResult);
		newDashBoardTxt = newDashBoardTxt.replace("@surveyno", "S" + surveyId);
		newDashBoardTxt = newDashBoardTxt.replace("@sampleid",
				String.valueOf(sampleTagDtls.get().getSampleIdValue()));
		dashBoard.setToUserId(Short.valueOf(Integer.toString(7)));// eiar admin id
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(Short.valueOf(Integer.toString(userId)));
		dashBoard.setCreatedOn(new Date());
		dashboardNotificationRepository.saveAndFlush(dashBoard);

		SurveyDetails sdtls = surveyDetailsRepository.findBySurveyId(surveyId);
		dashBoard = new DashboardNotificationEntity();
		dashBoard.setToUserId(Short.valueOf(Integer.toString(7)));// eiar admin id
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(Short.valueOf(Integer.toString(userId)));
		dashBoard.setCreatedOn(new Date());
		dashBoard.setToUserId(Short.valueOf(Integer.toString(sdtls.getCreatedBy())));// Surveyor

		dashboardNotificationRepository.saveAndFlush(dashBoard);
		User surveyor = userRepository.findByUserId(sdtls.getCreatedBy());
		User eiarAdmin = userRepository.findByUserId(7);// eiaradmin id
		String newEmailTxt = mailsms.getMailcontent();
		newEmailTxt = newEmailTxt.replace("@result", finalResult);
		newEmailTxt = newEmailTxt.replace("@surveyno", "S" + surveyId);
		newEmailTxt = newEmailTxt.replace("@sampleid", String.valueOf(sampleId));
		String text = newEmailTxt;

		Thread t = new Thread() {
			@Override
			public void run() {

				EmailUtil.sendAppcMail(surveyor.getEmail(), text, mailsms.getMailsubject());
				EmailUtil.sendAppcMail(eiarAdmin.getEmail(), text, mailsms.getMailsubject());
			}
		};
		t.start();

	}

	@Autowired
	UserRepository userRepository;
	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;
	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@Override
	public JSONObject saveDumpSampleTagDetails(JSONArray json, int userId) throws JSONException {
		JSONObject resp = new JSONObject();

		try {

			for (int i = 0; i < json.length(); i++) {
				JSONObject jsonObj = json.getJSONObject(i);
				Integer sampleTagId = jsonObj.getInt("SampleId");
				String remark = jsonObj.getString("Remarks");

				SampleLabTagDetails details = sampleLabTagDetailsRepository.findBySampleLabTagId(sampleTagId);
				details.setUpdatedBy(userId);
				details.setUpdatedOn(new Date());
				details.setRaceStatus(3);
				details.setRejectRemark(remark);
				sampleLabTagDetailsRepository.save(details);

			}

			resp.put("Status", 200);
			resp.put(WrsisPortalConstant.MESSAGE_JSON, "Data saved successfully.");
			return resp;
		} catch (Exception e) {
			LOG.error("RaceAnalysisServiceImpl::saveDumpsampleTagDetails():" + e);

			resp.put("Status", 400);
			resp.put(WrsisPortalConstant.MESSAGE_JSON, "Failed to save.");
			return resp;
		}
	}

	@Override
	public List<SampleTagBean> viewRaceAnalysisReport(SearchVo searchVo) {
		List<SampleTagBean> samples = new ArrayList<>();
		try {
			if (searchVo.getSurveyNo() == null)
				searchVo.setSurveyNo("");
			if (searchVo.getSampleId() == null)
				searchVo.setSampleId("");
			if (searchVo.getStartDate() == null)
				searchVo.setStartDate("");
			if (searchVo.getEndDate() == null)
				searchVo.setEndDate("");
			if (searchVo.getYear() == null)
				searchVo.setYear("");
			LOG.info("getSurveyNo : " + searchVo.getSurveyNo() + ", getSampleId : " + searchVo.getSampleId()
					+ ", getStartDate : " + searchVo.getStartDate() + ", getEndDate : " + searchVo.getEndDate()
					+ ", getRegionId : " + searchVo.getRegionId() + ", getZoneId : " + searchVo.getZoneId()
					+ ", getYear : " + searchVo.getYear() + ", getSeasonId : " + searchVo.getSeasonId()
					+ ", getRustTypeId " + searchVo.getRustTypeId());
			List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.viewRaceAnalysisReport(
					searchVo.getSurveyNo(), searchVo.getSampleId(), searchVo.getStartDate(), searchVo.getEndDate(),
					searchVo.getRegionId(), searchVo.getZoneId(), searchVo.getYear(), searchVo.getSeasonId(),
					searchVo.getRustTypeId());
			for (Object[] obj : sampleTagDetails) {
				SampleTagBean sample = new SampleTagBean();
				sample.setSurveyNo(String.valueOf(obj[0]));
				sample.setSampleValue(String.valueOf(obj[1]));
				sample.setRustType(String.valueOf(obj[2]));
				sample.setSurveyDate(String.valueOf(obj[3]));
				
				sample.setRegion(String.valueOf(obj[4]));
				sample.setZone(String.valueOf(obj[5]));
				sample.setWoreda(String.valueOf(obj[6]));
				sample.setKebel(String.valueOf(obj[7]));
				sample.setSurveyId(Integer.valueOf(String.valueOf(obj[8])));
				sample.setResearchCenter(String.valueOf(obj[12]));
				sample.setStatus(String.valueOf(obj[10]));
				sample.setRaceResult(String.valueOf(obj[13]));
				sample.setRaceflag(String.valueOf(obj[14]));
				sample.setRacePublishDate(String.valueOf(obj[15]));
				samples.add(sample);
			}
			if (searchVo.getYear() == "") {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Integer currentYear = year;
				searchVo.setYear(currentYear.toString());
			}
			if (searchVo.getSeasonId() == 0) {
				Date date = new Date();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int month = localDate.getMonthValue();
				SeasonMonth seasonId = seasonMonthRepository.findSeasonIdByMonthId(month);
				searchVo.setSeasonId(seasonId.getSeasonId());
			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisServiceImpl::viewRaceAnalysisReport():" + e);

		}

		return samples;
	}

	@Override
	public List<SampleTagBean> downloadRaceAnalysisStatusExcel(String surveyNo, String sampleId, String startDate,
			String endDate, String regionId, String rustTypeId, Integer currentUserId, Integer researchCenterId) {
		if (surveyNo == null)
			surveyNo = "";
		if (sampleId == null)
			sampleId = "";
		if (startDate == null)
			startDate = "";
		if (endDate == null)
			endDate = "";
		Integer count = 0;
		List<Object[]> sampleTagDetails = null;
		List<SampleTagBean> samples = new ArrayList<>();
		int start = 0;
		int length = 9999999;

		try {

			sampleTagDetails = sampleLabTagDetailsRepository.viewRaceAnalysis(surveyNo.toUpperCase(), sampleId,
					startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), currentUserId,
					researchCenterId, start, length);

			samples = new ArrayList<>();
			for (Object[] obj : sampleTagDetails) {
				SampleTagBean sample = new SampleTagBean();
				sample.setsNo((Integer.valueOf(start)) + (++count));
				sample.setSurveyId((Integer.parseInt(String.valueOf(obj[9]))));
				sample.setSurveyNo((obj[0] != null) ? (String) obj[0] : "");
				sample.setSampleValue((obj[1] != null) ? (String) obj[1] : "");
				sample.setRustType((obj[2] != null) ? (String) obj[2] : "");
				sample.setSurveyDate((obj[3] != null) ? (String) obj[3] : "");
				sample.setInoculationDate((obj[4] != null) ? (String) obj[4] : "");
				sample.setRegion((obj[5] != null) ? (String) obj[5] : "");
				sample.setZone((obj[6] != null) ? (String) obj[6] : "");
				sample.setWoreda((obj[7] != null) ? (String) obj[7] : "");
				sample.setKebel((obj[8] != null) ? (String) obj[8] : "");
				if (obj[13] != null) {
					sample.setResearchCenter(obj[13].toString());
				} else {
					sample.setResearchCenter("External");
				}
				sample.setStatus((obj[11] != null) ? (String) obj[11] : "");
				if (obj[14] != null) {
					sample.setRaceResult(obj[14].toString());
				} else {
					sample.setRaceResult("--");
				}
				if (obj[15].toString().equals("false")) {
					sample.setRacePublishDate("--");
				} else {
					sample.setRacePublishDate(obj[16].toString());
				}

				samples.add(sample);
			}

		} catch (Exception e) {
			LOG.error("RaceAnalysisServiceImpl::downloadRaceAnalysisStatusExcel():" + e);

		}
		return samples;
	}

}
