package com.csmpl.wrsis.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.service.DashboardService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.SampleLabTagBeanDataTable;
import com.csmpl.wrsis.datatable.TagSampleDataTable;
import com.csmpl.wrsis.pdf.TagSamplePDF;
import com.csmpl.wrsis.webportal.bean.ResearchCenterBean;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.ResearchCenter;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.DemographyRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterRepository;
import com.csmpl.wrsis.webportal.repository.ResearchCenterSpelizationRepository;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.SurveyDetailsRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.RaceAnalysisService;
import com.csmpl.wrsis.webportal.service.RaceAnalysisTagSampleService;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;

@Controller
public class RaceAnalysisTagSampleController extends WrsisPortalAbstractController {

	public static final Logger LOG = LoggerFactory.getLogger(RaceAnalysisTagSampleController.class);

	@Autowired
	SurveyDetailsRepository surveyDetailsRepository;

	@Autowired
	ManageDemographicService manageDemographicService;

	@Autowired
	DemographyRepository demographyRepository;

	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;

	@Autowired
	TypeOfRustRepository typeOfRustRepository;

	@Autowired
	RaceAnalysisService raceAnalysisService;

	@Autowired
	ResearchCenterService researchCenterService;

	@Autowired
	ResearchCenterSpelizationRepository researchCenterSpelizationRepository;

	@Autowired
	RaceAnalysisTagSampleService raceAnalysisTagSampleService;

	@Autowired
	DemographicRepository demographicRepository;

	@Autowired
	TypeOfRustService typeOfRustService;

	@Autowired
	DashboardService dashboardService;

	@Autowired
	ResearchCenterRepository researchCenterRepository;

	@RequestMapping(value = "/viewsample")
	public String viewTaggedSamples(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute("statusList", getStatus());
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute("rust", typeOfRustRepository.getRustTypes());
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::viewTaggedSamples():" + e);
		}
		return "viewsample";
	}

	// dynamic pagination view tag sample

	@RequestMapping(value = { "/viewTagSampleDetailsData" })
	public @ResponseBody SampleLabTagBeanDataTable viewTagSampleDetailsData(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("rustTypeId") String rustTypeId, RedirectAttributes redirect) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);


		List<SampleLabTagBean> beans = raceAnalysisTagSampleService.viewTaggedSamples(surveyNo.trim().toUpperCase(),
				sampleId, startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId),
				Integer.valueOf(start), Integer.valueOf(length));
		Integer totalCount = raceAnalysisTagSampleService.viewTagSampleDetailsDataCount(surveyNo.trim().toUpperCase(),
				sampleId, startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), -1, -1);

		SampleLabTagBeanDataTable sdt = new SampleLabTagBeanDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	public List<SearchVo> getStatus() {
		SearchVo vo = null;
		String[] rNames = { WrsisPortalConstant.ACTIVE, WrsisPortalConstant.IN_ACTIVE };
		List<SearchVo> statusList = new ArrayList<>();
		try {
			for (int i = 0; i < rNames.length; i++) {
				vo = new SearchVo();
				vo.setDataId(i + 1);
				vo.setDataName(rNames[i]);
				statusList.add(vo);
			}

		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::getStatus():" + e);
		}
		return statusList;
	}

	@RequestMapping(value = "editLabTaggedSample")
	public String editTaggedSamples(Model model,
			@RequestParam(value = "sampleLabTagId", required = true) int sampleLabTagId) {
		try {
			SampleLabTagBean sample = new SampleLabTagBean();

			List<Object[]> sampleTagDetails = sampleLabTagDetailsRepository.getTagDetails(sampleLabTagId);
			for (Object[] objs : sampleTagDetails) {
				sample.setSampleLabTagId((Integer) objs[0]);
				sample.setSampleIdValue(String.valueOf(objs[1]));
				sample.setSurveyNo(String.valueOf(objs[2]));
				sample.setSurveyDate(String.valueOf(objs[3]));
				sample.setRegionName(String.valueOf(objs[5]));
				int rustId = (Short) objs[6];
				sample.setRustTypeId(rustId);
				sample.setRustType(String.valueOf(objs[7]));
				int rcId = (Short) objs[8];
				sample.setResearchCenterId(rcId);
				sample.setResearchCenterName(String.valueOf(objs[9]));
				sample.setExternalLab(String.valueOf(objs[10]));

			}
			model.addAttribute("labs", researchCenterService.getResearchCenterListByRustType(sample.getRustTypeId()));
			model.addAttribute("taggedSampledetails", sample);
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::editTaggedSamples():" + e);
		}
		return "editLabTaggedSample";
	}

	@RequestMapping(value = "updateLabTaggedSample")
	public String updateLabTaggedSample(Model model,
			@ModelAttribute("taggedSampledetails") SampleLabTagBean sampleDetails, HttpServletRequest request,
			RedirectAttributes redirect) {
		String sts = "";
		HttpSession session = request.getSession();
		sampleDetails.setUpdatedBy((Integer) session.getAttribute(WrsisPortalConstant.USER_ID));
		try {
			sts = raceAnalysisService.updateLab(sampleDetails);
			if (sts.equalsIgnoreCase(WrsisPortalConstant.SUCCESS)) {
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data updated successfully");
				return "redirect:viewsample";
			}
			if (sts.equalsIgnoreCase("fail")) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try again");
			}
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::updateLabTaggedSample():" + e);
		}
		return "editLabTaggedSample";
	}

	public List<ResearchCenterBean> getResearchCenterNameByRustId(String rustId) {
		List<ResearchCenterBean> list = new ArrayList<>();
		if (rustId != null && !"0".equals(rustId)) {

			List<Object[]> researchCenterObj = researchCenterSpelizationRepository
					.findResearchCenteByRustId(Integer.parseInt(rustId));
			for (Object[] obj : researchCenterObj) {
				ResearchCenterBean researchCenter = new ResearchCenterBean();
				researchCenter.setResearchCenterId(Integer.valueOf(String.valueOf(obj[0])));
				researchCenter.setResearchCenter(String.valueOf(obj[1]));
				list.add(researchCenter);
			}

		}
		return list;
	}

	@RequestMapping(value = { "/addTagSample" }, method = RequestMethod.POST)
	public String storeMultileveApprovalAuthority(@ModelAttribute("tagBean") SampleTagBean tagBean,
			@RequestBody String jsonString, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		tagBean.setUserId(userId);
		try {

			String result = raceAnalysisTagSampleService.saveTagSample(tagBean, jsonString);
			if (WrsisPortalConstant.SUCCESS.equalsIgnoreCase(result)) {
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data saved Successfully");
			}

		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::storeMultileveApprovalAuthority():" + e);
		}

		return "allocationforrestanalysis";
	}

	@RequestMapping("/allocationforrestanalysis")
	public String searchSurveyTagSample(Model model, HttpSession session, @ModelAttribute SearchVo searchVo) {
		try {

			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewRegionList());
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, typeOfRustRepository.getRustTypes());
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.TAGSAMPLE_PROCESS_ID;
			Integer totalCount = raceAnalysisTagSampleService.searchTagSampleDetailsDataCount("", "", "", "", 0, 0,
					userId, Integer.valueOf(processId), -1, -1);

			model.addAttribute("Show", totalCount > 0);
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::searchSurveyTagSample():" + e);
		}

		return "allocationforrestanalysis";
	}

	@RequestMapping(value = { "/tagSampleDetailsData" })
	public @ResponseBody TagSampleDataTable tagSampleDetailsData(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate, @RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam("rustTypeId") String rustTypeId, RedirectAttributes redirect) {

		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		String processId = WrsisPortalConstant.wrsisPropertiesFileConstants.TAGSAMPLE_PROCESS_ID;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<SampleTagBean> beans = raceAnalysisTagSampleService.fetchActiveTagSamples(surveyNo.trim().toUpperCase(),
				sampleId, startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), userId,
				Integer.valueOf(processId), Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = raceAnalysisTagSampleService.searchTagSampleDetailsDataCount(surveyNo.trim().toUpperCase(),
				sampleId, startDate, endDate, Integer.valueOf(regionId), Integer.valueOf(rustTypeId), userId,
				Integer.valueOf(processId), -1, -1);

		TagSampleDataTable sdt = new TagSampleDataTable();
		sdt.setData(beans);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));
		return sdt;
	}

	//Author : Raman Shrestha
	//Date : 21-09-2020
	@RequestMapping(value = { "viewRaceAnalysisReport" })
	public String viewRaceAnalysisReport(HttpServletRequest request, Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		List<DemographicEntity> regionList = new ArrayList<>();
		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();
		List<ResearchCenter> laboratoryList = new ArrayList<>();
		try {
			rustTypeList = typeOfRustRepository.vewAllTypeOFRustByStatus();
			regionList = demographicRepository.retriveRegions();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();
			laboratoryList = researchCenterRepository.findByLabStatusAndStatus(true, false);

			model.addAttribute("searchVo", searchVo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);
			model.addAttribute("researchCenterList", researchCenterList);
			model.addAttribute("laboratoryList", laboratoryList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		}catch (Exception ex) {
			LOG.error("RaceAnalysisTagSampleController::viewRaceAnalysisReport():" + ex);
		}
		return "viewRaceAnalysisReport";
	}
	
	//Author : Raman Shrestha
	//Date : 21-09-2020
	@RequestMapping(value = { "viewRaceAnalysisReportSearch" })
	public String viewRaceAnalysisReportSearch(HttpServletRequest request, Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		List<DemographicEntity> regionList = new ArrayList<>();
		List<TypeOfRust> rustTypeList = new ArrayList<>();
		List<ResearchCenter> researchCenterList = new ArrayList<>();
		List<ResearchCenter> laboratoryList = new ArrayList<>();
		try {
			rustTypeList = typeOfRustRepository.vewAllTypeOFRustByStatus();
			regionList = demographicRepository.retriveRegions();
			researchCenterList = researchCenterRepository.findActiveResearchCenter();
			laboratoryList = researchCenterRepository.findByLabStatusAndStatus(true, false);

			model.addAttribute("searchVo", searchVo);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			model.addAttribute(WrsisPortalConstant.RUST_TYPE_LIST, rustTypeList);
			model.addAttribute("researchCenterList", researchCenterList);
			model.addAttribute("laboratoryList", laboratoryList);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}catch (Exception ex) {
			LOG.error("RaceAnalysisTagSampleController::viewRaceAnalysisReportSearch():" + ex);
		}
		return "viewRaceAnalysisReport";
	}

	// excel download view tag samples

	@PostMapping("/tagsampleExcelDownload")
	public void tagsampleExcelDownload(Model model, HttpServletResponse response,
			@RequestParam(value = "sampleIdE", required = false) String sampleIdE,
			@RequestParam(value = "surveyNoE", required = false) String surveyNoE,
			@RequestParam(value = "fromDateE", required = false) String fromDateE,
			@RequestParam(value = "toDateE", required = false) String toDateE,
			@RequestParam(value = "regionIdE", required = false) int regionIdE,
			@RequestParam(value = "filterE", required = false) int filterE) throws ParseException {

		raceAnalysisTagSampleService.tagsampleExcelDownload(response, sampleIdE, surveyNoE, fromDateE, toDateE,
				regionIdE, filterE);
	}

	// down load tag sample pdf

	@RequestMapping(value = { "/tagsamplePdfDownload" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> tagsamplePdfDownload(HttpServletResponse response, HttpSession session,
			Model model, @RequestParam("sampleIdPDF") String sampleIdPDF,
			@RequestParam("surveyNoPDF") String surveyNoPDF, @RequestParam("fromDatePDF") String fromDatePDF,
			@RequestParam("toDatePDF") String toDatePDF, @RequestParam("regionIdPDF") int regionIdPDF,
			@RequestParam("filterPDF") int filterPDF) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		List<SampleLabTagBean> beans = raceAnalysisTagSampleService.tagsamplePdfDownload(sampleIdPDF, surveyNoPDF,
				fromDatePDF, toDatePDF, regionIdPDF, filterPDF);
		TagSamplePDF rIPDF = new TagSamplePDF();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateTagSamplePdf(beans);
		} catch (Exception e) {
			LOG.error("RaceAnalysisTagSampleController::tagsamplePdfDownload():" + e);
		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Tag_Sample_Data" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}

}
