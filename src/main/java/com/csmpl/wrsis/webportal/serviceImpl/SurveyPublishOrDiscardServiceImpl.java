package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.entity.User;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.repository.UserRepository;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityEntity;
import com.csmpl.wrsis.webportal.entity.ApprovalAuthorityHistoryEntity;
import com.csmpl.wrsis.webportal.entity.DashboardNotificationEntity;
import com.csmpl.wrsis.webportal.entity.MailSmsContentEntity;
import com.csmpl.wrsis.webportal.entity.SurveyDetails;
import com.csmpl.wrsis.webportal.entity.SurveyDiscardLogEntity;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityEntityRepository;
import com.csmpl.wrsis.webportal.repository.ApprovalAuthorityHistoryEntityRepository;
import com.csmpl.wrsis.webportal.repository.DashboardNotificationRepository;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.FungiEffectTypeRepository;
import com.csmpl.wrsis.webportal.repository.FungicideDetailsRepository;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;
import com.csmpl.wrsis.webportal.repository.GrowthRepository;
import com.csmpl.wrsis.webportal.repository.LevelDetailsRepository;
import com.csmpl.wrsis.webportal.repository.MailSmsContentRepository;
import com.csmpl.wrsis.webportal.repository.MoistureRepository;
import com.csmpl.wrsis.webportal.repository.ReactionTypeRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleSurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeverityRepository;
import com.csmpl.wrsis.webportal.repository.SiteTypeRepository;
import com.csmpl.wrsis.webportal.repository.SoilColorRepository;
import com.csmpl.wrsis.webportal.repository.SubReportRepository;
import com.csmpl.wrsis.webportal.repository.SurveyCapturedImageRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyExcelFilesRepository;
import com.csmpl.wrsis.webportal.repository.SurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.repository.SurveyOtherRepository;
import com.csmpl.wrsis.webportal.repository.SurveyRustDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveySiteRepository;
import com.csmpl.wrsis.webportal.repository.SurveyorDetailsRepository;
import com.csmpl.wrsis.webportal.repository.UserResearchCenterMappingRepository;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;
import com.csmpl.wrsis.webportal.repository.WRSurveyOtherDiseaseRepository;
import com.csmpl.wrsis.webportal.repository.WheatTypeRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.SurveyDiscardLogService;
import com.csmpl.wrsis.webportal.service.SurveyPublishOrDiscardService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.EmailUtil;

@Service
public class SurveyPublishOrDiscardServiceImpl implements SurveyPublishOrDiscardService {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyPublishOrDiscardServiceImpl.class);
	@Autowired
	SurveyExcelFilesRepository surveyExcelFilesRepository;

	@Autowired
	SoilColorRepository soilColorRepository;

	@Autowired
	MoistureRepository moistureRepository;

	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;

	@Autowired
	SiteTypeRepository siteTypeRepository;

	@Autowired
	WheatTypeRepository wheatTypeRepository;

	@Autowired
	GrowthRepository growthRepository;

	@Autowired
	RustTypeRepository rustTypeRepository;

	@Autowired
	ReactionTypeRepository reactionTypeRepository;

	@Autowired
	CommonService commonService;

	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	SampleTypeRepository sampleTypeRepository;

	@Autowired
	FungicideMasterRepository fungicideMasterRepository;

	@Autowired
	FungiEffectTypeRepository fungiEffectTypeRepository;

	@Autowired
	VarietyTypeRepository varietyTypeRepository;

	@Autowired
	SurveyorDetailsRepository surveyorDetailsRepository;

	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	TypeOfRustService typeOfRustService;
	@Autowired
	UserResearchCenterMappingRepository userResearchCenterMappingRepository;

	@Value("${mail.indicator}")
	private String mailFlag;

	@Value("${sms.indicator}")
	private String smsFlag;

	@Autowired
	MailSmsContentRepository mailSmsContentRepository;

	@Autowired
	LevelDetailsRepository levelDetailsRepository;
	@Autowired
	SurveySiteRepository surveySiteRepository;

	@Autowired
	SurveyOtherRepository surveyOtherRepository;

	@Autowired
	SurveyOtherDiseaseRepository surveyOtherDiseaseRepository;

	@Autowired
	WRSurveyOtherDiseaseRepository WRSurveyOtherDiseaseRepository;

	@Autowired
	SurveyRustDetailsRepository surveyRustDetailsRepository;

	@Autowired
	FungicideDetailsRepository fungicideDetailsRepository;

	@Autowired
	SampleDetailsRepository sampleDetailsRepository;

	@Autowired
	SampleSurveyDetailsRepository sampleSurveyDetailsRepository;

	@Autowired
	SurveyCapturedImageRepository surveyCapturedImageRepository;

	@Autowired
	SeverityRepository severityRepository;
	@Autowired
	SurveyDiscardLogService surveyDiscardLogService;
	@Autowired
	ApprovalAuthorityEntityRepository approvalAuthorityEntityRepository;
	@Autowired
	ApprovalAuthorityHistoryEntityRepository approvalAuthorityHistoryEntityRepository;

	@Autowired
	DashboardNotificationRepository dashboardNotificationRepository;
	@Autowired
	UserRepository userRepository;

	@Override
	public List<SurveyDetailsBean> pendingSurveyView() {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(0, 0, 0, 0, "",
					"", 0, "", 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("0")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::pendingSurveyView():" + e);
		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> searchPendingSurvey(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			// here User Id is default as zero because Its Admin so its can see all records
			// of Survey Details with survey status zero
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, 0);

			for (Object[] ar : details) {

				if (ar[9].toString().equals("0")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::searchPendingSurvey():" + e);
		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> publishedSurveyView() {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(0, 0, 0, 0, "",
					"", 0, "", 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::publishedSurveyView():" + e);
		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> searchPublishedSurvey(String zoneId, String regionId, String woredaId,
			String kebeleId, String startDate, String endDate, String surveyNo, String rustTypeId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::searchPublishedSurvey():" + e);
		}
		return beans;
	}

	// of Survey Details with survey status two
	@Override
	public List<SurveyDetailsBean> discardSurveyView() {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(0, 0, 0, 0, "",
					"", 0, "", 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("2")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setDiscardDte((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::discardSurveyView():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> searchDiscardSurvey(String zoneId, String regionId, String woredaId, String kebeleId,
			String startDate, String endDate, String surveyNo, String rustTypeId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			// of Survey Details with survey status Two

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, sDate, Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("2")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setDiscardDte((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::searchDiscardSurvey():" + e);
		}
		return beans;
	}

	// Show published data by User Id
	@Override
	public List<SurveyDetailsBean> surveyPublishedReport(Integer userId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByUser(0, 0, 0,
					0, "", "", 0, "", 0, userId, 1);
			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedReport():" + e);
		}
		return beans;
	}

	// Search published data by User Id
	@Override
	public List<SurveyDetailsBean> searchSurveyPublisheReport(String zoneId, String regionId, String woredaId,
			String kebeleId, String startDate, String endDate, String surveyNo, String rustTypeId, Integer userId) {

		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByUser(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, Integer.valueOf(rustTypeId), surveyNo.toUpperCase().trim(),
					0, userId, 1);
			

			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);

			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveysurveyPublishReport():" + e);

		}
		return beans;
	}

	// Show Discarded data by User Id
	@Override
	public List<SurveyDetailsBean> surveyDiscardedReport(Integer userId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByUser(0, 0, 0,
					0, "", "", 0, "", 0, userId, 2);

			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);

			}

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveyDiscardedReport():" + e);

		}
		return beans;
	}

	// Search Discarded data by User Id
	@Override
	public List<SurveyDetailsBean> searchSurveyDiscardedReport(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, String surveyNo, String rustTypeId, Integer userId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByUser(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, Integer.valueOf(rustTypeId), surveyNo.toUpperCase(), 0,
					userId, 2);

			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);

			}

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::searchSurveyDiscardedReport():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> rustSurveyDataReport() {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(0, 0, 0, 0, "",
					"", 0, "", 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyDataReport():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> rustSurveyDataSearchReport(Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo) {

		List<SurveyDetailsBean> beans = new ArrayList<>();
		List<Object[]> details = null;
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else {
				sDate = startDate;
			}
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else {
				eDate = endDate;
			}

			details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(regionId, zoneId, woredaId,
					kebeleId, sDate, eDate, rustTypeId, surveyNo.toUpperCase().trim(), 0, 0);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyDataSearchReport():" + e);

		}
		return beans;
	}

	@Override
	public void surveyPublishedExcel(HttpServletResponse response, Integer regionId, Integer zoneId, Integer woredaId,
			Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo) {

		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			List<Object[]> details = null;
			details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetails(regionId, zoneId, woredaId,
					kebeleId, sDate, eDate, rustTypeId, surveyNo.toUpperCase().trim(), 0, 0);

			// Excel Code Start
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Survey Publish Data");

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

			Font newFont = workbook.createFont();
			newFont.setBold(true);
			newFont.setColor(IndexedColors.WHITE.getIndex());
			newFont.setFontHeightInPoints((short) 13);
			newFont.setItalic(false);

			headerCellStyle.setFont(newFont);

			Row headRow = sheet.createRow(0);
			String[] columns = { "Sl No.", WrsisPortalConstant.SURVEY_NUMBER, WrsisPortalConstant.SURVEY_DATE, "Published Date", WrsisPortalConstant.REGION, WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA,
					WrsisPortalConstant.KEBELE, "Instituition Name", "Type of Rust", "Mode Of Data Collection" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}
			int rowNo = 1;
			int count = 1;
			for (Object[] ar : details) // Bean Object
			{
				if (ar[9].toString().equals("1")) {
					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(2).setCellValue((ar[2] != null) ? (String) ar[2] : "-NA-");
					row.createCell(3).setCellValue((ar[12] != null) ? (String) ar[12] : "-NA-");
					row.createCell(4).setCellValue((ar[4] != null) ? (String) ar[4] : "-NA-");
					row.createCell(5).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(6).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(7).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(8).setCellValue((ar[11] != null) ? (String) ar[11] : "-NA-");
					row.createCell(9).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(10).setCellValue((ar[10] != null) ? (String) ar[10] : "-NA-");
				}
			}
			
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
			String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
			String FILENAME = "Survey-Published-Report_" + formattedDate + ".xlsx";
			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			try {
				workbook.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				workbook.close();
			} catch (IOException e) {

				LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedExcel():" + e);

			}
			// Excel Code End

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedExcel():" + e);

		}

	}

	@Override
	public List<SurveyDetailsBean> rustSurveyDataReportByRCTagging(Integer userId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = surveyDetailsRepository
					.searchSurveyPendingPublishedDiscardDetailsByResearchCenterTag(0, 0, 0, 0, "", "", 0, "", 0,
							userId);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRegion((ar[3] != null) ? (String) ar[3] : "");
					sdb.setZone((ar[4] != null) ? (String) ar[4] : "");
					sdb.setWoreda((ar[5] != null) ? (String) ar[5] : "");
					sdb.setKebele((ar[6] != null) ? (String) ar[6] : "");
					sdb.setInstitutionName((ar[7] != null) ? (String) ar[7] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyDataReportByRCTagging():" + e);
		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> rustSurveyDataSearchReportByRCTagging(Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo,
			Integer userId) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository
					.searchSurveyPendingPublishedDiscardDetailsByResearchCenterTag(regionId, zoneId, woredaId, kebeleId,
							sDate, eDate, rustTypeId, surveyNo.toUpperCase().trim(), 0, userId);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setPublishedDate((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRegion((ar[3] != null) ? (String) ar[3] : "");
					sdb.setZone((ar[4] != null) ? (String) ar[4] : "");
					sdb.setWoreda((ar[5] != null) ? (String) ar[5] : "");
					sdb.setKebele((ar[6] != null) ? (String) ar[6] : "");
					sdb.setInstitutionName((ar[7] != null) ? (String) ar[7] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
					sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");

					beans.add(sdb);
				}
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyDataSearchReportByRCTagging():" + e);

		}
		return beans;
	}

	@Override
	public void surveyPublishedExcelByRCTagging(HttpServletResponse response, Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo,
			Integer userId) {
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			List<Object[]> details = null;
			details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByResearchCenterTag(regionId,
					zoneId, woredaId, kebeleId, sDate, eDate, rustTypeId, surveyNo.toUpperCase().trim(), 0, userId);

			// Excel Code Start
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Survey Publish Data");

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
			headerCellStyle.setFillPattern(FillPatternType.BIG_SPOTS);

			Font newFont = workbook.createFont();
			newFont.setBold(true);
			newFont.setColor(IndexedColors.WHITE.getIndex());
			newFont.setFontHeightInPoints((short) 13);
			newFont.setItalic(false);

			headerCellStyle.setFont(newFont);

			Row headRow = sheet.createRow(0);
			String[] columns = { "Sl No.", WrsisPortalConstant.SURVEY_NUMBER, WrsisPortalConstant.SURVEY_DATE, "Published Date", WrsisPortalConstant.REGION, WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA,
					WrsisPortalConstant.KEBELE, "Instituition Name", "Type of Rust", "Mode Of Data Collection" };
			for (int i = 0; i < columns.length; i++) {
				Cell cell = headRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}
			int rowNo = 1;
			int count = 1;
			for (Object[] ar : details) // Bean Object
			{
				if (ar[9].toString().equals("1")) {
					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(2).setCellValue((ar[2] != null) ? (String) ar[2] : "-NA-");
					row.createCell(3).setCellValue((ar[11] != null) ? (String) ar[11] : "-NA-");
					row.createCell(4).setCellValue((ar[3] != null) ? (String) ar[3] : "-NA-");
					row.createCell(5).setCellValue((ar[4] != null) ? (String) ar[4] : "-NA-");
					row.createCell(6).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(7).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(8).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(9).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(10).setCellValue((ar[10] != null) ? (String) ar[10] : "-NA-");
				}
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(WrsisPortalConstant.DATE_FORMAT_DDMMYYYHHMMSS);
			String formattedDate = myDateObj.format(myFormatObj).replace(" ", "").replace(":", "");
			String FILENAME = "Survey-Published-Report_" + formattedDate + ".xlsx";

			response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			try {
				workbook.write(response.getOutputStream());
				response.getOutputStream().flush();
				response.getOutputStream().close();
				workbook.close();
			} catch (IOException e) {

				LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedExcelByRCTagging():" + e);
			}
			// Excel Code End

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedExcelByRCTagging():" + e);

		}

	}

	@Override
	public UserResearchCenterMapping checkUsertaggingResearchCenter(Integer userId) {
		UserResearchCenterMapping rc = null;
		try {
			rc = userResearchCenterMappingRepository.findByUserId(userId);
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::checkUsertaggingResearchCenter():" + e);

		}
		return rc;
	}

	@Override
	public List<SurveyDetailsBean> discardSurveyViewPage(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0, 2, pstart,
					pLength);
			Integer count = 0;
			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\"" + sdb.getSurveyId()
						+ "\">" + sdb.getSurveyNo() + "</a>");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setDiscardDte((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
				beans.add(sdb);

			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::discardSurveyviewPage():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> publishedSurveyViewPage(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0,
					actionStatus, pstart, pLength);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\"" + sdb.getSurveyId()
						+ "\">" + sdb.getSurveyNo() + "</a>");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::publishedSurveyviewPage():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> pendingSurveyViewPage(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		return null;

	}

	@Override
	public List<SurveyDetailsBean> pendingSurveyViewPageByUser(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer processId, Integer pstart, Integer pLength) {

		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			
			List<Object[]> details = surveyDetailsRepository
					.searchSurveyPendingPublishedDiscardDetailsByActionStatusByUser(Integer.valueOf(regionId),
							Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), sDate, eDate,
							rustTypeId, surveyNo.trim(), 0, userId, actionStatus, processId, pstart,
							pLength);

			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);

				sdb.setSurveyCheckBox("<input id='demo-form-inline-checkbox" + sdb.getSurveyId()
						+ "' class='magic-checkbox' type='checkbox' name='surveyDetailsId' value=" + sdb.getSurveyId()
						+ ">" + "<label for='demo-form-inline-checkbox" + sdb.getSurveyId() + "'></label>");
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\"" + sdb.getSurveyId()
						+ "\">" + sdb.getSurveyNo() + "</a>");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				sdb.setEditLink(
						"<a class=\"btn btn-info btn-sm editClass\" data-toggle=\"tooltip editClass\" title=\"\" id=\" \" data-original-title=\"Edit\" survey-id=\""
								+ sdb.getSurveyId() + "\"><i class=\"icon-edit1\"></i></a>");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::pendingSurveyviewPageByUser():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> pendingSurveyViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0,
					actionStatus, pstart, pLength);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::pendingSurveyviewPageExcel():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> publishedSurveyViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0,
					actionStatus, -1, -1);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::publishedSurveyviewPageExcel():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> discardSurveyViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0, 2, pstart,
					pLength);
			Integer count = 0;
			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
				beans.add(sdb);

			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::discardSurveyviewPageExcel():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> rustSurveyPublishedDataViewPage(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0,
					actionStatus, pstart, pLength);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\"" + sdb.getSurveyId()
						+ "\">" + sdb.getSurveyNo() + "</a>");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyPublishedDataViewPage():" + e);

		}
		return beans;
	}

	@Override
	public List<SurveyDetailsBean> rustSurveyPublishedDataViewPageExcel(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {
		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			List<Object[]> details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByActionStatus(
					Integer.valueOf(regionId), Integer.valueOf(zoneId), Integer.valueOf(woredaId),
					Integer.valueOf(kebeleId), sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), 0, 0,
					actionStatus, pstart, pLength);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::rustSurveyPublishedDataViewPageExcel():" + e);

		}
		return beans;

	}

	@Autowired
	SubReportRepository subReportRepository;

	@Override
	public List<RustIncidentEntityBean> getDashBoardRustIncidentExcel(Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, Integer yearId, Integer seasonId) {

		List<RustIncidentEntityBean> beans = new ArrayList<>();
		try {

			List<Object[]> details = subReportRepository.getIncidendReportListDetails(regionId, zoneId, woredaId,
					kebeleId, yearId, seasonId);

		
			for (Object[] obj : details) {
				RustIncidentEntityBean objReport = new RustIncidentEntityBean();

				objReport.setIncidendDate(String.valueOf(obj[0]));
				objReport.setRegionName(String.valueOf(obj[1]));
				objReport.setZoneName(String.valueOf(obj[2]));
				objReport.setWoredaName(String.valueOf(obj[3]));
				objReport.setKebeleName(String.valueOf(obj[4]));
				objReport.setYear(String.valueOf(obj[5]));
				objReport.setSeasonName(String.valueOf(obj[6]));
				objReport.setLongitude(String.valueOf(obj[7]));
				objReport.setLatitude(String.valueOf(obj[8]));
				objReport.setQueostions(String.valueOf(obj[9]));
				objReport.setUserFullName(String.valueOf(obj[10]));
				beans.add(objReport);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::getdashBoardRustIncidentExcel():" + e);

		}
		return beans;

	}

	@Override
	public List<SurveyDetailsBean> surveyPublishedExcelByRCTaggingData(Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo,
			Integer userId) {
		List<SurveyDetailsBean> beanList = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			List<Object[]> details = null;
			details = surveyDetailsRepository.searchSurveyPendingPublishedDiscardDetailsByResearchCenterTag(regionId,
					zoneId, woredaId, kebeleId, sDate, eDate, rustTypeId, surveyNo.toUpperCase().trim(), 0, userId);

			for (Object[] ar : details) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean bean = new SurveyDetailsBean();
					bean.setSurveyNo((ar[1] != null) ? (String) ar[1] : "-NA-");
					bean.setSurveyDate((ar[2] != null) ? (String) ar[2] : "-NA-");
					bean.setPublishedDate((ar[11] != null) ? (String) ar[11] : "-NA-");
					bean.setRegion((ar[3] != null) ? (String) ar[3] : "-NA-");
					bean.setZone((ar[4] != null) ? (String) ar[4] : "-NA-");
					bean.setWoreda((ar[5] != null) ? (String) ar[5] : "-NA-");
					bean.setKebele((ar[6] != null) ? (String) ar[6] : "-NA-");
					bean.setInstitutionName((ar[7] != null) ? (String) ar[7] : "-NA-");
					bean.setRust((ar[8] != null) ? (String) ar[8] : "-NA-");
					bean.setMode((ar[10] != null) ? (String) ar[10] : "-NA-");
					bean.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
					beanList.add(bean);
				}
			}

		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::surveyPublishedExcelByRCTaggingData():" + e);

		}

		return beanList;
	}

	@Override
	public List<SurveyDetailsBean> pendingSurveyViewPageExcelByUser(String regionId, String zoneId, String woredaId,
			String kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo, Integer fileId,
			Integer userId, Integer actionStatus, Integer processId, Integer pstart, Integer pLength) {

		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {

			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;

			

			List<Object[]> details = surveyDetailsRepository
					.searchSurveyPendingPublishedDiscardDetailsByActionStatusByUser(Integer.valueOf(regionId),
							Integer.valueOf(zoneId), Integer.valueOf(woredaId), Integer.valueOf(kebeleId), sDate, eDate,
							rustTypeId, surveyNo.trim().toUpperCase(), 0, userId, actionStatus, processId, pstart,
							pLength);
			Integer count = 0;
			for (Object[] ar : details) {
				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo((ar[1] != null) ? (String) ar[1] : "");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				sdb.setLocation((ar[5] != null) ? (String) ar[5] : "");
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				beans.add(sdb);
			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::pendingSurveyViewPageExcelByUser():" + e);

		}
		return beans;

	}

	@Transactional
	@Override
	public String publishOrDiscardSurveyDataSave(String[] surveyFinalArrayId, String surveyFinalOptionId,
			String surveyFinalReasonId, Integer userId, String processId, String sendMailStatus, String sendSmsStatus) {

		String msg = "";
		try {

			String[] arr = surveyFinalArrayId;
			int count = 0;
			Integer tagSampleProcessId = 0;
			Integer pendingAt = 0;
			Integer stageNo = 0;
			Date date = new Date();
			if (Integer.parseInt(surveyFinalOptionId) == 1) {
				List<Object[]> obj = approvalAuthorityEntityRepository.fetchApproveDetails(
						Integer.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.TAGSAMPLE_PROCESS_ID));
				if (obj != null && !obj.isEmpty()) {
					tagSampleProcessId = Integer
							.valueOf(WrsisPortalConstant.wrsisPropertiesFileConstants.TAGSAMPLE_PROCESS_ID);
					pendingAt = (Integer) obj.get(0)[2];
					stageNo = (Integer) obj.get(0)[1];

				} else {
					return msg = "config";
				}

			}
			for (int i = 0; i < arr.length; i++) {
				// Its Update the SurveyDetails Table with surveyStatus, actionBy, ActionRemarks
				// & actionOn fields
				SurveyDetails surveyDetails = surveyDetailsRepository.findBySurveyId(Integer.parseInt(arr[i]));
				surveyDetails.setSurveyStatus(Integer.parseInt(surveyFinalOptionId));
				surveyDetails.setActionBy(userId);
				surveyDetails.setActionRemark(surveyFinalReasonId);
				surveyDetails.setActionOn(date);
				SurveyDetails dr = surveyDetailsRepository.saveAndFlush(surveyDetails);
				if (Integer.parseInt(surveyFinalOptionId) == 2) {
					SurveyDiscardLogEntity surveyDiscrd = new SurveyDiscardLogEntity();
					surveyDiscrd.setSurveyId(surveyDetails.getSurveyId());
					surveyDiscrd.setSurveyNo(surveyDetails.getSurveyNumber());
					surveyDiscrd.setRemark(surveyFinalReasonId);

					surveyDiscrd.setDiscardBy(userId);
					surveyDiscardLogService.saveSurveyDiscardLog(surveyDiscrd);

					// Here Save Data into DhashBoard Notification for discarded Data
					discardedDashBoardNotification(userId, surveyDetails, surveyFinalReasonId);
					// Here Send Mail to Corresponding Surveyor discarded Data
					discardedDashBoardSendMail(sendMailStatus, surveyDetails, surveyFinalReasonId);

				} else if (Integer.parseInt(surveyFinalOptionId) == 1) {
					// Here Save Data into DhashBoard Notification for published Data
					

					// Here Send Mail to Corresponding Surveyor Published Data
					publishedDashBoardSendMail(sendMailStatus, surveyDetails);
					// If Sample I Collected
					if (surveyDetails.isSampleCollected()) {
						// Save ApprovalAuthority
						ApprovalAuthorityEntity authorityEntity = new ApprovalAuthorityEntity();
						authorityEntity.setAprovalPId(tagSampleProcessId);
						authorityEntity.setStageNo(stageNo);
						authorityEntity.setSurveyId(surveyDetails.getSurveyId());
						authorityEntity.setPendingAt(pendingAt);
						authorityEntity.setSentFrom(userId);
						authorityEntity.setStatus(false);
						authorityEntity.setCreadtedBy(userId);
						authorityEntity.setCreatedOn(date);
						approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);

						// Save ApprovalAuthorityHistory
						ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
						history.setAprovalPId(authorityEntity.getAprovalPId());
						history.setStageNo(authorityEntity.getStageNo());
						history.setSurveyId(authorityEntity.getSurveyId());
						history.setPendingAt(authorityEntity.getPendingAt());
						history.setSentFrom(authorityEntity.getSentFrom());
						history.setStatus(authorityEntity.isStatus());
						history.setCreadtedBy(userId);
						history.setCreatedOn(date);

						approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
					}

				}

				if (dr == null) {
					count = 0;
					break;
				}
				// Its Update ApprovalAuthorityEntity
				List<ApprovalAuthorityEntity> appAuthorityEntity = approvalAuthorityEntityRepository
						.findApprovalAuthorityDetailsBySurveyId(surveyDetails.getSurveyId(),
								Integer.parseInt(processId));
				if (appAuthorityEntity.size() == 0) {
					
					
				}
				for (ApprovalAuthorityEntity authorityEntity : appAuthorityEntity) {

					// Hear Save In to ApprovalAuthorityHistoryEntity table
					ApprovalAuthorityHistoryEntity history = new ApprovalAuthorityHistoryEntity();
					history.setAprovalPId(authorityEntity.getAprovalPId());
					history.setStageNo(authorityEntity.getStageNo());
					history.setSurveyId(authorityEntity.getSurveyId());
					history.setPendingAt(authorityEntity.getPendingAt());
					history.setSentFrom(authorityEntity.getSentFrom());
					history.setStatus(authorityEntity.isStatus());
					history.setCreadtedBy(userId);
					history.setCreatedOn(date);

					ApprovalAuthorityHistoryEntity h = approvalAuthorityHistoryEntityRepository.saveAndFlush(history);
					if (h == null) {
						
						
					}
					// Here Update ApprovalAuthorityEntity Table
					authorityEntity.setPendingAt(0);
					authorityEntity.setSentFrom(userId);
					authorityEntity.setUpdatedBy(userId);
					authorityEntity.setUpdatedOn(date);
					ApprovalAuthorityEntity a = approvalAuthorityEntityRepository.saveAndFlush(authorityEntity);
					if (a == null) {
						
						
					}

				}
				count++;
				LOG.info("count==" + count);

			}
			if (count > 0) {

				if (surveyFinalOptionId.equals("1")) {
					msg = count + "\t" + "number of data has published";
				} else {
					msg = count + "\t" + "number of data has discarded";
				}

			} else
				msg = "fail";
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::publishOrDiscardSurveyDataSave():" + e);

		}
		return msg;

	}

	// Save Published Survey Data Into DashBoard Notification
	private void publishedDashBoardNotification(Integer userId, SurveyDetails surveyDetails) {

		Integer intv = new Integer(userId);
		Short sValueCreatedBy = intv.shortValue();

		Integer intSCv = new Integer(surveyDetails.getCreatedBy());
		Short surveyCreadtedBy = intSCv.shortValue();
		MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(4);
		String dashBoardTxt = mailsms.getNotification();
		final String newDashBoardTxt = dashBoardTxt.replace("@surveyno ", surveyDetails.getSurveyNumber());
		DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
		dashBoard.setToUserId(surveyCreadtedBy);
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(sValueCreatedBy);
		dashBoard.setCreatedOn(new Date());
		dashboardNotificationRepository.saveAndFlush(dashBoard);

	}

	// Save Discarded Survey Data Into DashBoard Notification
	private void discardedDashBoardNotification(Integer userId, SurveyDetails surveyDetails,
			String surveyFinalReasonId) {

		Integer intv = new Integer(userId);
		Short sValueCreatedBy = intv.shortValue();

		Integer intSCv = new Integer(surveyDetails.getCreatedBy());
		Short surveyCreadtedBy = intSCv.shortValue();
		MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(5);

		String dashBoardTxt = mailsms.getNotification();

		final String newDashBoardTxt = dashBoardTxt.replace("@surveyno ", surveyDetails.getSurveyNumber())
				.replaceAll("@reason", surveyFinalReasonId);

		DashboardNotificationEntity dashBoard = new DashboardNotificationEntity();
		dashBoard.setToUserId(surveyCreadtedBy);
		dashBoard.setNotificationMsg(newDashBoardTxt);
		dashBoard.setReadStatus(false);
		dashBoard.setStatus(false);
		dashBoard.setCreatedBy(sValueCreatedBy);
		dashBoard.setCreatedOn(new Date());
		dashboardNotificationRepository.saveAndFlush(dashBoard);

	}

	// Published Survey Data mail
	private void publishedDashBoardSendMail(String mail, SurveyDetails surveyDetails) {
		if (WrsisPortalConstant.YES.equalsIgnoreCase(mail)) {
			final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(4);
			User user = userRepository.getOne(surveyDetails.getCreatedBy());
			if (user.getEmail() != null && !"".equalsIgnoreCase(user.getEmail())) {
				try {

					String emailTxt = mailsms.getMailcontent();
					final String newEmailTxt = emailTxt.replace("@surveyno", surveyDetails.getSurveyNumber());
					

					Thread t = new Thread() {
						@Override
						public void run() {

							EmailUtil.sendAppcMail(user.getEmail(), newEmailTxt, mailsms.getMailsubject());
						}
					};
					t.start();
				} catch (Exception e) {
					LOG.error("SurveyPublishOrDiscardServiceImpl::publishedDashBoardSendMail():" + e);

				}
			}
		}
	}

	// Discarded Survey Data mail
	private void discardedDashBoardSendMail(String mail, SurveyDetails surveyDetails, String remark) {
		if (WrsisPortalConstant.YES.equalsIgnoreCase(mail)) {
			final MailSmsContentEntity mailsms = mailSmsContentRepository.findByMscontentid(5);
			User user = userRepository.getOne(surveyDetails.getCreatedBy());
			if (user.getEmail() != null && !"".equalsIgnoreCase(user.getEmail())) {
				try {

					String emailTxt = mailsms.getMailcontent();
					final String newEmailTxt = emailTxt.replace("@surveyno", surveyDetails.getSurveyNumber())
							.replaceAll(" @reason", remark);
					

					Thread t = new Thread() {
						@Override
						public void run() {

							EmailUtil.sendAppcMail(user.getEmail(), newEmailTxt, mailsms.getMailsubject());
						}
					};
					t.start();
				} catch (Exception e) {
					LOG.error("SurveyPublishOrDiscardServiceImpl::discardedDashBoardSendMail():" + e);

				}
			}
		}
	}

	@Override
	public List<SurveyDetailsBean> viewSurveyPublishedAndDiscardReport(Integer regionId, Integer zoneId,
			Integer woredaId, Integer kebeleId, String startDate, String endDate, Integer rustTypeId, String surveyNo,
			Integer fileId, Integer userId, Integer actionStatus, Integer pstart, Integer pLength) {

		List<SurveyDetailsBean> beans = new ArrayList<>();
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				sDate = dt2.format(dt1.parse(startDate));

			} else
				sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
				SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
				eDate = dt4.format(dt3.parse(endDate));

			} else
				eDate = endDate;
			Integer count = 0;
			List<Object[]> details = surveyDetailsRepository.viewSurveyPendingPublishedDiscardDetailsByUser(regionId,
					zoneId, woredaId, kebeleId, sDate, eDate, rustTypeId, surveyNo.trim().toUpperCase(), fileId, userId,
					actionStatus, pstart, pLength);
			for (Object[] ar : details) {

				SurveyDetailsBean sdb = new SurveyDetailsBean();
				sdb.setsNo((pstart) + (++count));
				sdb.setSurveyId((ar[0] != null) ? Integer.valueOf(String.valueOf(ar[0])) : 0);
				sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\"  onclick=\"surveyDetl('"
						+ sdb.getSurveyId() + "')\">" + ar[1].toString() + "</a>");
				sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
				sdb.setPublishedDate((ar[12] != null) ? (String) ar[12] : "");
				sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
				sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
				sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
				sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
				sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
				sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
				sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
				
				sdb.setStatus((ar[9] != null) ? (String) ar[9] : "");
				sdb.setRemark((ar[13] != null) ? (String) ar[13] : "");
				sdb.setAction(
						"<a class='btn btn-info btn-sm editClass' data-toggle='tooltip editClass data-original-title='Edit' survey-id='"
								+ sdb.getSurveyId() + "'><i class='icon-edit1'></i></a>");
				beans.add(sdb);

			}
		} catch (Exception e) {
			LOG.error("SurveyPublishOrDiscardServiceImpl::viewSurveyPublishedAndDiscardReport():" + e);

		}
		return beans;
	}

}
