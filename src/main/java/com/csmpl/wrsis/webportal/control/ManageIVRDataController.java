package com.csmpl.wrsis.webportal.control;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.controller.WrsisPortalAbstractController;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.IvrDataTable;
import com.csmpl.wrsis.webportal.bean.IVRExcelCellBean;
import com.csmpl.wrsis.webportal.bean.ImportIVRFileBean;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.IVRDataQuestionEntity;
import com.csmpl.wrsis.webportal.entity.ImportIVRFile;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.QuestionOption;
import com.csmpl.wrsis.webportal.repository.IVRDataQuestionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionOptionRepository;
import com.csmpl.wrsis.webportal.repository.QuestionRepository;
import com.csmpl.wrsis.webportal.service.IVRDataService;
import com.csmpl.wrsis.webportal.service.ManageDemographicService;
import com.csmpl.wrsis.webportal.service.ManageIVRDataService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ManageIVRDataController extends WrsisPortalAbstractController {

	private final static Logger LOG = LoggerFactory.getLogger(ManageIVRDataController.class);

	@Autowired
	ManageIVRDataService manageIVRDataService;
	@Autowired
	IVRDataService iVRDataService;
	@Autowired
	ManageDemographicService manageDemographicService;
	@Autowired
	QuestionRepository questionRepository;

	@RequestMapping(value = { "/uploadIvrData" })
	public String uploadIvrData(@ModelAttribute(WrsisPortalConstant.IVR_DATA) ImportIVRFileBean ivrData) {

		try {

		} catch (Exception e) {
			LOG.error("ManageIVRDataController::uploadIvrData():" + e);
		}

		return "uploadIvrData";
	}

	@PostMapping("/uploadExcelFileRedData")
	public String uploadExcelForRed(Model model, @RequestParam("ivrExcel") MultipartFile ivrExcel,
			HttpServletRequest request, @ModelAttribute(WrsisPortalConstant.IVR_DATA) ImportIVRFileBean ivrr) {

		ImportIVRFileBean ivr = new ImportIVRFileBean();
		ivr.setFileNameBEan(ivrExcel);
		try {

			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			ivr.setCreatedByBean(userId);
			if (ivrExcel != null && !ivrExcel.isEmpty()) {
				String fileName = ivr.getFileNameBEan().getOriginalFilename();
				String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));

				if (fileExtensionName.equals(".xlsx") || fileExtensionName.equals(".xls") || fileExtensionName.equals(".csv")) {
				} else {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Upload .xlsx and .xls file only");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "demo-text-input1");
					return "uploadIvrData";
				}
				long fileSize = ivr.getFileNameBEan().getSize();
				LOG.info("ManageIVRDataController::uploadExcelForRed(): fileSize" + fileSize);
				if (fileSize > 2097152) {

					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "File size should be less than 2MB");
					model.addAttribute(WrsisPortalConstant.FIELD_ID, "demo-text-input1");
					return "uploadIvrData";
				}

				List<IVRExcelCellBean> excelList = manageIVRDataService.readIvrExcel(ivr);
				if (excelList.get(0).getZone() == "nameError") {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, excelList.get(0).getWoreda());
					return "uploadIvrData";
				}
				if (excelList.get(0).getZone() == "existError") {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, excelList.get(0).getWoreda());
					return "uploadIvrData";
				}
				if (excelList != null && excelList.size() > 0) {
					model.addAttribute("excelList", excelList);
					model.addAttribute("qustList", excelList.get(0).getQustions());
				} else {
					model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Wrong excel format, try again");
					return "uploadIvrData";
				}
			}

		} catch (Exception e) {
			LOG.error("ManageIVRDataController::uploadExcelForRed():" + e);
			model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Wrong excel format, try again");
		}
		return "uploadIvrData";
	}

	@PostMapping("/insert-ivr-data")
	public String saveAndUpdateIvrData(Model model,
			@ModelAttribute(WrsisPortalConstant.IVR_DATA) ImportIVRFileBean ivrData, HttpServletRequest request) {

		try {

			HttpSession session = request.getSession();
			int userId = (Integer) session.getAttribute(WrsisPortalConstant.USER_ID);
			ivrData.setCreatedByBean(userId);

			ivrData.setCellBeanList(extractReqExcelDetailsFromJson(ivrData.getExcelBeanListString()));

			ivrData.setRecordCountBean(ivrData.getCellBeanList().size());
			String fName = ivrData.getCellBeanList().get(0).getFileName();
			ivrData.setFile_Name(fName);

			String msg = manageIVRDataService.saveIVRFile(ivrData);
			if (WrsisPortalConstant.SAVE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.IVR_DATA, new ImportIVRFileBean());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Save Successfully");
			}
			if (WrsisPortalConstant.UPDATE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.IVR_DATA, new ImportIVRFileBean());
				model.addAttribute(WrsisPortalConstant.SUCCESS_MSG, "Data Updated Successfully");
			}
			if (WrsisPortalConstant.FAILURE.equals(msg)) {
				model.addAttribute(WrsisPortalConstant.ERROR_MSG, "Try Again");
			}
		} catch (Exception e) {
			LOG.error("ManageIVRDataController::saveAndUpdateIvrData():" + e);

		}

		return "uploadIvrData";
	}

	private List<IVRExcelCellBean> extractReqExcelDetailsFromJson(String excelBeanListString) {
		ObjectMapper mapper = new ObjectMapper();
		List<IVRExcelCellBean> list = null;
		try {
			list = mapper.readValue(excelBeanListString, new TypeReference<List<IVRExcelCellBean>>() {
			});

		} catch (JsonParseException e) {
			LOG.error("ManageIVRDataController::extractReqExcelDetailsFromJson():" + e);
		} catch (JsonMappingException e) {
			LOG.error("ManageIVRDataController::extractReqExcelDetailsFromJson():" + e);
		} catch (IOException e) {
			LOG.error("ManageIVRDataController::extractReqExcelDetailsFromJson():" + e);
		}

		return list;
	}

	@GetMapping("/viewIvrData")
	public String viewIvrData(Model model, SearchVo searchVo) {

		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewIvrData";
	}

	@ResponseBody
	@GetMapping("/viewAllIvrFilePage")
	public String viewIvrDataFilePage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort) {
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return manageIVRDataService.viewIVRFileDataByPage(pageSize, pageNumber, pageable);
	}

	@PostMapping("/ivr-data-excel-download")
	public void ivrDownloadExcelByFileName(Model m1, ModelMap model, @RequestParam("fileId") Integer fileId,
			HttpServletResponse response) throws ParseException {

		manageIVRDataService.downloadIVRFile(response, fileId, model);

	}

	@ResponseBody
	@GetMapping("/searchAllIvrFilePage")
	public String searchIvrDataFilePage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = WrsisPortalConstant.FROM_DATE, required = false) Date fromDate,
			@RequestParam(value = WrsisPortalConstant.TO_DATE, required = false) Date toDate) {
		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort.by("createdOn"));
		return manageIVRDataService.searchIVRFileDataByDatePage(pageSize, pageNumber, pageable, fromDate, toDate);
	}

	// Author : Raman
	@RequestMapping(value = { "/viewIvrSearch" })
	public String moarecommendationSearch(Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "viewIvrData";
	}

	// Author : Raman
	@RequestMapping(value = { "/viewIvrDataPage" })
	public @ResponseBody IvrDataTable viewIvrDataPage(Model modelHttp, HttpSession session, HttpServletRequest request,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate) {

		IvrDataTable sdt = null;
		try {
			String start = request.getParameter(WrsisPortalConstant.START);
			String length = request.getParameter(WrsisPortalConstant.LENGTH);
			String draw = request.getParameter(WrsisPortalConstant.DRAW);
			List<IvrDataReportBean> ivrList = manageIVRDataService.viewIvrDataPage(startDate, endDate,
					Integer.valueOf(start), Integer.valueOf(length));

			Integer totalCount = manageIVRDataService.countIvrDataPage(startDate, endDate, -1, -1);

			sdt = new IvrDataTable();
			sdt.setData(ivrList);
			sdt.setRecordsFiltered(totalCount);
			sdt.setRecordsTotal(totalCount);
			sdt.setDraw(Integer.parseInt(draw));
		} catch (Exception e) {
			LOG.error("ManageIVRDataController::viewIvrDataPage():" + e);

		}
		return sdt;
	}

	@Autowired
	IVRDataQuestionRepository iVRDataQuestionRepository;
	@Autowired
	QuestionOptionRepository questionOptionRepository;

	@PostMapping("ivr-data-excel-data-count")
	public String getQustionDetailsByFileCount(Model model, @RequestParam("fileId") Integer fileId) {
		try {
			ImportIVRFile ivr = manageIVRDataService.getQustionDetailsByImprtIVRId(fileId);
			model.addAttribute("fileDtl", ivr);
			model.addAttribute("fileId", ivr.getImprtIVRId());
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			List<ImportIVRFileBean> ivrList = iVRDataService.viewIVRFileDataByPageTest(fileId);

			List<String> pageQustionJS = new ArrayList<>();
			for (int i = 0; i < ivrList.size(); i++) {
				ImportIVRFileBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getPageQustion());
				} catch (Exception e) {
					LOG.error("ManageIVRDataController::getQustionDetailsByFileCount():" + e);
					continue;
				}
				
                 /*for (int j = 0; j < jsa.length(); j++) {

					
					JSONObject objects = jsa.getJSONObject(j);
					String question_option = objects.getString("qust_tabl_id");
					String ques_name = objects.getString("qust_name");
					
					pageQustionJS.add(ques_name);
                 }*/
				
				
				for (int j = 0; j < jsa.length(); j++) {
					Question hQuestion = questionRepository
							.getOne(Integer.parseInt(jsa.getJSONObject(j).getString("qust_tabl_id")));

					if (!pageQustionJS.contains(hQuestion.getQustion())) {
						pageQustionJS.add(hQuestion.getQustion());
					}
				}
			}
			model.addAttribute(WrsisPortalConstant.QUESTIONS, pageQustionJS);

			for (int i = 0; i < ivrList.size(); i++) {
				ImportIVRFileBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getQuestionOption());

				} catch (Exception e) {
					LOG.error("ManageIVRDataController::getQustionDetailsByFileCount():" + e);
					continue;
				}
				ArrayList<String> ivrQustionValue = new ArrayList<>();
				for (int j = 0; j < jsa.length(); j++) {

					
					JSONObject objects = jsa.getJSONObject(j);
					String question_option = objects.getString("options");
					String incd_option = objects.getString("incdoption");
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(incd_option);
					}
					/*IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
							.getOne(Integer.parseInt(jsa.getJSONObject(j).getString("ivr_qst")));
					
					String question_option = null;
					List<QuestionOption> optionList = questionOptionRepository
							.findByQustionId(ivrQuestionEntity.getQustionId());
					for (QuestionOption qOption : optionList) {
						if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
							question_option = qOption.getOptionInfo();

						}
					}
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(ivrQuestionEntity.getQustionResponse());
					}*/

				}

				qust.setIvrQuestionsData(ivrQustionValue);
				ivrList.set(i, qust);
			}
			model.addAttribute(WrsisPortalConstant.QUESTIONS, pageQustionJS);
			model.addAttribute("ivrList", ivrList);
			model.addAttribute(WrsisPortalConstant.REGION_ID, 0);
			model.addAttribute("zoneId", 0);
			model.addAttribute("woredaId", 0);
		} catch (Exception e) {
			LOG.error("ManageIVRDataController::getQustionDetailsByFileCount():" + e);
		}

		return "ivrSummaryReport";
	}

	@ResponseBody
	@GetMapping("/viewAllIvrQustionPage")
	public String viewAllIvrQustionPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "id", required = false) Integer ivrFileId) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		return iVRDataService.viewIVRFileDataByPage(pageSize, pageNumber, pageable, ivrFileId);
	}

	@ResponseBody
	@GetMapping("/searchIvrQustionPage")
	public String searchIvrQustionPage(@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
			@RequestParam(value = "sort", required = false) Sort sort,
			@RequestParam(value = "id", required = false) Integer ivrFileId,
			@RequestParam(value = "textPhone", required = false) String textPhone,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = "zoneId", required = false) Integer zoneId,
			@RequestParam(value = "WoredaId", required = false) Integer WoredaId) {

		Pageable pageable = new PageRequest(pageNumber - 1, pageSize, sort);
		if ("".equalsIgnoreCase(textPhone) && regionId == 0 && zoneId == null && WoredaId == null) {
			return iVRDataService.viewIVRFileDataByPage(pageSize, pageNumber, pageable, ivrFileId);
		} else {
			return iVRDataService.searachIVRFileDataByPage(pageSize, pageNumber, pageable, ivrFileId, textPhone,
					regionId, zoneId, WoredaId);
		}

	}

	@PostMapping("/searchIvrQustionPageTest")
	public String searchIvrQustion(@RequestParam(value = "fileId", required = false) Integer ivrFileId,
			@RequestParam(value = "textPhone", required = false) String textPhone,
			@RequestParam(value = WrsisPortalConstant.REGION_ID, required = false) Integer regionId,
			@RequestParam(value = "zoneId", required = false) Integer zoneId,
			@RequestParam(value = "woredaId", required = false) Integer woredaId, Model model) {
		try {
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
			model.addAttribute(WrsisPortalConstant.REGION_LIST, manageDemographicService.viewAllRegionByStatus());
			model.addAttribute("fileId", ivrFileId);
			model.addAttribute("textPhone", textPhone);
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute("zoneId", zoneId);
			model.addAttribute("woredaId", woredaId);
			List<ImportIVRFileBean> ivrList = iVRDataService.searachIVRFileDataByPageTest(ivrFileId, textPhone,
					regionId, zoneId, woredaId);
			List<String> pageQustionJS = new ArrayList<>();
			for (int i = 0; i < ivrList.size(); i++) {
				ImportIVRFileBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getPageQustion());
				} catch (Exception e) {
					LOG.error("ManageIVRDataController::searchIvrQustion():" + e);
					continue;
				}
				for (int j = 0; j < jsa.length(); j++) {
					Question hQuestion = questionRepository
							.getOne(Integer.parseInt(jsa.getJSONObject(j).getString("qust_tabl_id")));

					if (!pageQustionJS.contains(hQuestion.getQustion())) {
						pageQustionJS.add(hQuestion.getQustion());
					}
				}
			}
			model.addAttribute(WrsisPortalConstant.QUESTIONS, pageQustionJS);

			for (int i = 0; i < ivrList.size(); i++) {
				ImportIVRFileBean qust = ivrList.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(qust.getIvrQustion());

				} catch (Exception e) {
					LOG.error("ManageIVRDataController::ivrList.get(i):" + e);
					continue;
				}

				ArrayList<String> ivrQustionValue = new ArrayList<>();
				for (int j = 0; j < jsa.length(); j++) {

					IVRDataQuestionEntity ivrQuestionEntity = iVRDataQuestionRepository
							.getOne(Integer.parseInt(jsa.getJSONObject(j).getString("ivr_qst")));

					
					String question_option = null;
					List<QuestionOption> optionList = questionOptionRepository
							.findByQustionId(ivrQuestionEntity.getQustionId());
					for (QuestionOption qOption : optionList) {

						if (qOption.getOptionValue().equals(ivrQuestionEntity.getQustionResponse())) {
							question_option = qOption.getOptionInfo();

						}
					}
					if (question_option != null) {
						ivrQustionValue.add(question_option);
					} else {
						ivrQustionValue.add(ivrQuestionEntity.getQustionResponse());
					}

				}

				qust.setIvrQuestionsData(ivrQustionValue);
				ivrList.set(i, qust);
			}

			model.addAttribute(WrsisPortalConstant.QUESTIONS, pageQustionJS);
			model.addAttribute("ivrList", ivrList);
		} catch (Exception e) {
			LOG.error("ManageIVRDataController::searchIvrQustion():" + e);
		}
		return "ivrSummaryReport";
	}

	@PostMapping("ivr-excel-file-download")
	public void ivrExcelFileDownload(HttpServletResponse response) {
		iVRDataService.ivrExcelFileDownload(response);
	}

	@ResponseBody
	@GetMapping("/ivr-file-exist")
	public String irvFileExits(Model model, @RequestParam("fileId") Integer fileId) {
		return manageIVRDataService.ivrFileExits(fileId);
	}
	
	//View IVR API Data
	@GetMapping("/viewIvrApiLogData")
	public String viewIvrApiData(Model model, SearchVo searchVo) {

		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
		return "viewIvrApiData";
	}
	

	@RequestMapping(value = { "/viewIvrApiLogDataSearch" })
	public String viewIvrApiLogDataSearch(Model model, SearchVo searchVo) {
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		return "viewIvrApiData";
	}
}
