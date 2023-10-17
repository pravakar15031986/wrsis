package com.csmpl.wrsis.webportal.control;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.entity.UserResearchCenterMapping;
import com.csmpl.adminconsole.webportal.service.UserService;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.SampleLabTagBeanDataTable;
import com.csmpl.wrsis.webportal.bean.SampleLabTagBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.SampleLabTagDetails;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.entity.TypeOfRust;
import com.csmpl.wrsis.webportal.repository.SampleLabTagDetailsRepository;
import com.csmpl.wrsis.webportal.repository.TypeOfRustRepository;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.ResearchCenterService;
import com.csmpl.wrsis.webportal.service.SampleTypeService;
import com.csmpl.wrsis.webportal.util.Validation;

@Controller
public class SampleTypeController extends WrsisPortalAbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(SampleTypeController.class);
	@Autowired
	SampleTypeService sampleTypeService;
	@Autowired
	TypeOfRustRepository typeOfRustRepository;
	@Autowired
	ManageDemographicService manageDemographicService;
	@Autowired
	SampleLabTagDetailsRepository sampleLabTagDetailsRepository;
	@Autowired
	ResearchCenterService researchCenterService;
	@Autowired
	UserService userService;

	@RequestMapping(value = { "/addSampleType" })
	public String addSampleType(Model model, @ModelAttribute("sample") SampleTypeMaster sample) {

		model.addAttribute(WrsisPortalConstant.RUST_TYPES, typeOfRustRepository.getTypeOfRusts());
		return "addSampleTypeMaster";
	}

	// @PostMapping("/save-sample-type")
	public String check(@ModelAttribute("sample") SampleTypeMaster sampleTypeMaster) {

		return "addSampleTypeMaster";
	}

	@PostMapping("/save-sample-type")
	public String saveAndUpdate(Model model, @ModelAttribute("sample") SampleTypeMaster sampleTypeMaster,
			HttpServletRequest request, RedirectAttributes redirect) {
		String returnPage = "addSampleTypeMaster";
		if (sampleTypeMaster.getSampleId() != 0) {
			returnPage = "editSampleType";
		}
		try {
			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			model.addAttribute(WrsisPortalConstant.RUST_TYPES, typeOfRustRepository.getTypeOfRusts());
			if (sampleTypeMaster.getRustTypeId() == 0) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please select Rust Type");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.RUSTID);
				return returnPage;
			}
			if (sampleTypeMaster.getSampleName().isEmpty() || sampleTypeMaster.getSampleName() == "") {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter the Sample Type");
				model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
				return returnPage;
			}
			if (!sampleTypeMaster.getSampleName().isEmpty()) {
				if (!Validation.validateAlphabatesOnly(sampleTypeMaster.getSampleName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Sample Type accept only alphabets");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateFirstBlankSpace(sampleTypeMaster.getSampleName())
						|| !Validation.validateLastBlankSpace(sampleTypeMaster.getSampleName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in sample type");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(sampleTypeMaster.getSampleName())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Sample type should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DEMO_TEXT_INPUT1);
					return returnPage;
				}
			}
			if (!sampleTypeMaster.getDescription().isEmpty()) {
				if (!Validation.validateFirstBlankSpace(sampleTypeMaster.getDescription())
						|| !Validation.validateLastBlankSpace(sampleTypeMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return returnPage;
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(sampleTypeMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description should not contain consecutive blank spaces");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return returnPage;
				}
				if (!Validation.validateSpecialCharAtFirstAndLast(sampleTypeMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"!@#$&*() characters are not allowed at initial and last place in description");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return returnPage;
				}
				if (!Validation.validateUserAddress(sampleTypeMaster.getDescription())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Description accept only alphabets,numbers and (:)#;/,.-\\ characters");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, WrsisPortalConstant.DESCRIPTION);
					return returnPage;
				}
			}
			sampleTypeMaster.setCreatedBy(userId);
			String msg = sampleTypeService.saveAndUpdateSampleType(sampleTypeMaster);
			if (msg.equals(WrsisPortalConstant.EXIST)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Sample Type already exists");
			}
			if (WrsisPortalConstant.DEPENDENT.equalsIgnoreCase(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG,
						"This Sample Type cannot be deactive. Already in use somewhere.");
			}
			if (msg.equals(WrsisPortalConstant.SAVE)) {
				model.addAttribute("sample", new SampleTypeMaster());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Saved Successfully");
			}
			if (msg.equals(WrsisPortalConstant.UPDATE)) {
				
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
				returnPage = "redirect:viewsampletype";
			}
			if (msg.equals(WrsisPortalConstant.FAILURE)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}
		} catch (Exception e) {
			LOG.error("SampleTypeController::saveAndUpdate():" + e);
		}

		return returnPage;
	}

	@RequestMapping(value = { "/viewsampletype" }, method = RequestMethod.GET)
	public String deseaseMasterView(Model model) {

		model.addAttribute("rust", typeOfRustRepository.getRustTypes());
		return "viewSampleTypeMaster";
	}

	@ResponseBody
	@GetMapping("/viewAllSampleTypePage")
	public String viewDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return sampleTypeService.viewSampleTypeByPage(pageSize, pageNumber, pageable);
	}

	@PostMapping("/editSampleType")
	public String editSampleType(Model model, @RequestParam(value = WrsisPortalConstant.SAMPLEID) Integer sampleId) {
		try {
			SampleTypeMaster sample = sampleTypeService.getSampleTypeById(sampleId);

			model.addAttribute(WrsisPortalConstant.RUST_TYPES, typeOfRustRepository.getTypeOfRusts());
			model.addAttribute("sample", sample);
		} catch (Exception e) {
			LOG.error("SampleTypeController::editSampleType():" + e);
		}

		return "editSampleTypeMaster";
	}

	@ResponseBody
	@GetMapping("/searchAllSampleTypePage")
	public String searchDiseaseMasterPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "rustType", required = false) String rustType,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "sort", required = false) Sort sort) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by(WrsisPortalConstant.SAMPLEID));
		return sampleTypeService.searchSampleTypeBySampleName(pageSize, pageNumber, pageable, rustType, status);
	}

	@RequestMapping(value = "/viewSurvryDetailsOnExternalSample")
	public String viewSurvryDetailsOnExternalSample(Model model, @ModelAttribute SearchVo searchVo) {
		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute("rust", typeOfRustRepository.getRustTypes());
			model.addAttribute("extTaggedSamplesList", sampleTypeService.viewExtTaggedSamples(searchVo));
		} catch (Exception e) {
			LOG.error("SampleTypeController::viewSurvryDetailsOnExternalSample():" + e);
		}
		return "viewSurvryDetailsOnExternalSample";
	}

	@RequestMapping(value = "/externalTaggedSamples")
	public String externalTaggedSamples(Model model,
			@RequestParam(value = WrsisPortalConstant.SAMPLEID, required = false) String sampleId,
			@RequestParam(value = WrsisPortalConstant.SURVEYNO, required = false) String surveyNo,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_FROM, required = false) String surveyDateFrom,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_TO, required = false) String surveyDateTo,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = WrsisPortalConstant.RUST_TYPE_ID, required = false) Integer rustTypeId) {
		if (sampleId != null || surveyNo != null || surveyDateFrom != null || regionId != null || rustTypeId != null) {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} else {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}
		try {
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute("rust", typeOfRustRepository.getRustTypes());
			
			model.addAttribute(WrsisPortalConstant.SAMPLEID, sampleId);
			model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_FROM, surveyDateFrom);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_TO, surveyDateTo);
			if (regionId == null)
				model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			else
				model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);

			if (rustTypeId == null)
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, 0);
			else
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);

		} catch (Exception e) {
			LOG.error("SampleTypeController::externalTaggedSamples():" + e);

		}
		return "externalTaggedSamples";
	}

	@RequestMapping(value = "/externalTaggedSamplesPageWise")
	@ResponseBody
	public SampleLabTagBeanDataTable externalTaggedSamplesPage(HttpServletRequest request, Model model,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.SURVEY_DATE_FROM) String surveyDateFrom, @RequestParam(WrsisPortalConstant.SURVEY_DATE_TO) String surveyDateTo,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("filter") Integer rustTypeId) {
		SampleLabTagBeanDataTable tdl = null;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		try {
			SearchVo searchVo = new SearchVo();
			
			searchVo.setSampleId(sampleId);
			searchVo.setSurveyNo(surveyNo);
			searchVo.setSurveyDateFrom(surveyDateFrom);
			searchVo.setSurveyDateTo(surveyDateTo);
			searchVo.setRustTypeId(rustTypeId);
			searchVo.setRegionId(regionId);
			searchVo.setPageSize(Integer.parseInt(start));
			searchVo.setPageLength(Integer.parseInt(length));
			List<SampleLabTagBean> data = sampleTypeService.viewExtTaggedSamples(searchVo);
			searchVo.setPageSize(-1);
			searchVo.setPageLength(-1);
			Integer recordsTotal = sampleTypeService.viewExtTaggedSamplesCount(searchVo);
			tdl = new SampleLabTagBeanDataTable();
			tdl.setData(data);
			tdl.setRecordsTotal(recordsTotal);
			tdl.setRecordsFiltered(recordsTotal);
			tdl.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SampleTypeController::externalTaggedSamplesPage():" + e);

		}
		return tdl;
	}

	public List<SearchVo> getStatus() {
		SearchVo vo = null;
		String rNames[] = { "Active", "Inactive" };
		List<SearchVo> statusList = new ArrayList<>();
		try {
			for (int i = 0; i < rNames.length; i++) {
				vo = new SearchVo();
				vo.setDataId(i + 1);
				vo.setDataName(rNames[i]);
				statusList.add(vo);
			}

		} catch (Exception e) {
			LOG.error("SampleTypeController::getStatus():" + e);
		}
		return statusList;
	}

	@RequestMapping(value = "editextTaggedSample")
	public String editTaggedSamples(Model model,
			@RequestParam(value = "sampleLabTagId", required = true) int sampleLabTagId) {
		try {
			model.addAttribute("extTaggedSampledetails", sampleTypeService.getSampleDetails(sampleLabTagId));
		} catch (Exception e) {
			LOG.error("SampleTypeController::editTaggedSamples():" + e);
		}
		return "editextTaggedSample";
	}

	@RequestMapping(value = "updateExtTaggedSample", method = RequestMethod.POST)
	public String updateExtTaggedSample(Model model, SampleLabTagDetails extTaggedSampledetails,
			HttpServletRequest request, RedirectAttributes redirect) {
		HttpSession session = request.getSession();
		int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
		extTaggedSampledetails.setUpdatedBy(userId);
		try {
			String sts = null;
			
			model.addAttribute("extTaggedSampledetails",
					sampleTypeService.getSampleDetails(extTaggedSampledetails.getSampleLabTagId()));

			if (extTaggedSampledetails.getRaceStatus() != 2 && extTaggedSampledetails.getRaceStatus() != 3) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please choose a Sample Action");
				return "editextTaggedSample";
			}
			if (extTaggedSampledetails.getRaceStatus() == 2) {
				if (extTaggedSampledetails.getRaceResult() == null
						|| extTaggedSampledetails.getRaceResult().isEmpty()) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Race Result");
					return "editextTaggedSample";
				}
				if (!Validation.validateFirstBlankSpace(extTaggedSampledetails.getRaceResult())
						|| !Validation.validateLastBlankSpace(extTaggedSampledetails.getRaceResult())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place");
					return "editextTaggedSample";
				}
				if (!Validation.validateDemographyName(extTaggedSampledetails.getRaceResult())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Race Result accept alphabets and numbers");
					return "editextTaggedSample";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(extTaggedSampledetails.getRaceResult())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Race Result should not contain consicutive blank spaces");
					return "editextTaggedSample";
				}
			}
			if (extTaggedSampledetails.getRaceStatus() == 3) {
				if (extTaggedSampledetails.getRejectRemark() == null
						|| extTaggedSampledetails.getRejectRemark().isEmpty()) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Please enter Remark");
					return "editextTaggedSample";
				}
				if (!Validation.validateFirstBlankSpace(extTaggedSampledetails.getRejectRemark())
						|| !Validation.validateLastBlankSpace(extTaggedSampledetails.getRejectRemark())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"White space is not allowed at initial and last place");
					return "editextTaggedSample";
				}
				if (!Validation.validateConsecutiveBlankSpacesInString(extTaggedSampledetails.getRejectRemark())) {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG,
							"Remark should not contain consicutive blank spaces");
					return "editextTaggedSample";
				}
			}
			sts = sampleTypeService.updtExtTaggedSample(extTaggedSampledetails);
			if ("dumpSuccess".equals(sts))
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Sample Dumped successfully");
			else if ("raceResultSuccess".equals(sts))
				redirect.addFlashAttribute(WrsisPortalConstant.SUCCESS_MSG, "Race Result updated successfully");
			else {
				redirect.addFlashAttribute(WrsisPortalConstant.ERROR_MSG, "Sample not updated");
			}
		} catch (Exception e) {
			LOG.error("SampleTypeController::updateExtTaggedSample():" + e);
		}
		return "redirect:externalTaggedSamples";
	}

	@RequestMapping(value = "/extRaceResult")
	public String extRaceResult(Model model, HttpSession session,
			@RequestParam(value = WrsisPortalConstant.SAMPLEID, required = false) String sampleId,
			@RequestParam(value = WrsisPortalConstant.SURVEYNO, required = false) String surveyNo,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_FROM, required = false) String surveyDateFrom,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_TO, required = false) String surveyDateTo,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = WrsisPortalConstant.RUST_TYPE_ID, required = false) Integer rustTypeId) {
		if (sampleId != null || surveyNo != null || surveyDateFrom != null || regionId != null || rustTypeId != null) {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} else {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}
		try {
			int user = (int) session.getAttribute(WrsisPortalConstant.USER_ID);

			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("regionlist", manageDemographicService.viewRegionList());
			model.addAttribute("rust", typeOfRustRepository.getRustTypes());
			model.addAttribute(WrsisPortalConstant.SAMPLEID, sampleId);
			model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_FROM, surveyDateFrom);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_TO, surveyDateTo);
			if (regionId == null)
				model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			else
				model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			if (rustTypeId == null)
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, 0);
			else
				model.addAttribute(WrsisPortalConstant.RUST_TYPE_ID, rustTypeId);
			LOG.info("USER ID IS" + user);
		} catch (Exception e) {
			LOG.error("SampleTypeController::extRaceResult():" + e);

		}
		return "extRaceResult";
	}

	@RequestMapping(value = "/extRaceResultPageWise")
	@ResponseBody
	public SampleLabTagBeanDataTable extRaceResultPageWise(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.SURVEY_DATE_FROM) String surveyDateFrom, @RequestParam(WrsisPortalConstant.SURVEY_DATE_TO) String surveyDateTo,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("filter") Integer rustTypeId) {
		SampleLabTagBeanDataTable sdt = null;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		try {
			int user = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			SearchVo searchVo = new SearchVo();
			searchVo.setUserId(user);
			searchVo.setSampleId(sampleId);
			searchVo.setSurveyNo(surveyNo);
			searchVo.setSurveyDateFrom(surveyDateFrom);
			searchVo.setSurveyDateTo(surveyDateTo);
			searchVo.setRustTypeId(rustTypeId);
			searchVo.setRegionId(regionId);
			searchVo.setPageSize(Integer.parseInt(start));
			searchVo.setPageLength(Integer.parseInt(length));
			List<SampleLabTagBean> data = sampleTypeService.viewExtRaceResult(searchVo);

			searchVo.setPageSize(-1);
			searchVo.setPageLength(-1);
			Integer recordsTotal = sampleTypeService.viewExtRaceResultCount(searchVo);
			sdt = new SampleLabTagBeanDataTable();
			sdt.setData(data);
			sdt.setRecordsTotal(recordsTotal);
			sdt.setRecordsFiltered(recordsTotal);
			sdt.setDraw(Integer.parseInt(draw));

		} catch (Exception e) {
			LOG.error("SampleTypeController::extRaceResultPageWise():" + e);

		}
		return sdt;
	}

	@RequestMapping(value = "/dumpedExternalSamples")
	public String viewdumpedSamplese(Model model, HttpServletRequest request,
			@RequestParam(value = WrsisPortalConstant.SAMPLEID, required = false) String sampleId,
			@RequestParam(value = WrsisPortalConstant.SURVEYNO, required = false) String surveyNo,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_FROM, required = false) String surveyDateFrom,
			@RequestParam(value = WrsisPortalConstant.SURVEY_DATE_TO, required = false) String surveyDateTo,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = WrsisPortalConstant.RUST_TYPE_ID, required = false) Integer rustTypeId) {
		model.addAttribute(WrsisPortalConstant.R_URL, request.getParameter(WrsisPortalConstant.R_URL));

		int rcId = 0;
		if (sampleId != null || surveyNo != null || surveyDateFrom != null || regionId != null || rustTypeId != null) {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		} else {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		}
		try {
			HttpSession session = request.getSession();
			int user = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			UserResearchCenterMapping rcObj = userService.getUserResearchCenterMapping(user);
			if (rcObj != null) {
				rcId = rcObj.getRecearchCenterId();
			}
			List<DemographicEntity> rlist = sampleTypeService.viewRegionList(rcId);
			List<TypeOfRust> rustList = sampleTypeService.getRustList(rcId);
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("regionlist", rlist);
			model.addAttribute("rust", rustList);
			model.addAttribute(WrsisPortalConstant.SAMPLEID, sampleId);
			model.addAttribute(WrsisPortalConstant.SURVEYNO, surveyNo);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_FROM, surveyDateFrom);
			model.addAttribute(WrsisPortalConstant.SURVEY_DATE_TO, surveyDateTo);
			if (regionId == null)
				model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			else
				model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);

			if (rustTypeId == null)
				model.addAttribute("filter", 0);
			else
				model.addAttribute("filter", rustTypeId);

		} catch (Exception e) {
			LOG.error("SampleTypeController::viewdumpedSamplese():" + e);

		}
		return "dumpedExternalSamples";
	}

	@RequestMapping(value = { "/dumpedExternalSamplesByPageWise" })
	public @ResponseBody SampleLabTagBeanDataTable viewdumpedSamplesPage(Model model, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.SAMPLEID) String sampleId, @RequestParam(WrsisPortalConstant.SURVEYNO) String surveyNo,
			@RequestParam(WrsisPortalConstant.SURVEY_DATE_FROM) String surveyDateFrom, @RequestParam(WrsisPortalConstant.SURVEY_DATE_TO) String surveyDateTo,
			@RequestParam(WrsisPortalConstant.REGION_ID) Integer regionId, @RequestParam("filter") Integer rustTypeId) {
		int rcId = 0;

		SampleLabTagBeanDataTable sdt = null;
		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);
		try {
			HttpSession session = request.getSession();
			int user = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			SearchVo searchVo = new SearchVo();
			searchVo.setUserId(user);
			searchVo.setSampleId(sampleId);
			searchVo.setSurveyNo(surveyNo);
			searchVo.setSurveyDateFrom(surveyDateFrom);
			searchVo.setSurveyDateTo(surveyDateTo);
			searchVo.setRustTypeId(rustTypeId);
			searchVo.setRegionId(regionId);
			searchVo.setPageSize(Integer.parseInt(start));
			searchVo.setPageLength(Integer.parseInt(length));
			UserResearchCenterMapping rcObj = userService.getUserResearchCenterMapping(searchVo.getUserId());
			if (rcObj != null) {
				rcId = rcObj.getRecearchCenterId();
			}
			List<DemographicEntity> rlist = sampleTypeService.viewRegionList(rcId);
			List<TypeOfRust> rustList = sampleTypeService.getRustList(rcId);
			model.addAttribute(WrsisPortalConstant.STATUS_LIST, getStatus());
			model.addAttribute("regionlist", rlist);
			model.addAttribute("rust", rustList);
			

			List<SampleLabTagBean> data = sampleTypeService.viewdumpedSamples(searchVo);
			searchVo.setPageSize(-1);
			searchVo.setPageLength(-1);
			Integer totalCount = sampleTypeService.viewdumpedSamplesCount(searchVo);
			sdt = new SampleLabTagBeanDataTable();
			sdt.setData(data);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("SampleTypeController::viewdumpedSamplesPage():" + e);

		}
		return sdt;
	}

}
