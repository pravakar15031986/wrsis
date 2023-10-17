package com.csmpl.wrsis.webportal.control;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.datatable.RustIncidentDataTable;
import com.csmpl.wrsis.pdf.RustIncidentPDFView;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.excel.PublishSurveyExcel;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentRepository;
import com.csmpl.wrsis.webportal.repository.SeasionMasterRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMasterRepository;
import com.csmpl.wrsis.webportal.service.RustIncidentViewService;
import com.csmpl.wrsis.webportal.service.TypeOfRustService;
import com.csmpl.wrsis.webportal.util.DateUtil;

@Controller
public class RustIncidentViewController {

	private static final Logger LOG = LoggerFactory.getLogger(RustIncidentViewController.class);
	@Autowired
	RustIncidentViewService rustIncidentViewService;
	@Autowired
	SeasonMasterRepository seasonMasterRepository;
	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	TypeOfRustService typeOfRustService;
	@Autowired
	SeasionMasterRepository seasionMasterRepository;

	@GetMapping("/rustIncidentView")
	public String rustIncidentView(Model model) {
		String year = null;
		Integer seasionId = 0;
		try {
			String yearSeason = seasionMasterRepository.getCurrentYearAndSeason();
			JSONObject jso = new JSONObject(yearSeason);
			year = jso.getString(WrsisPortalConstant.YEAR);
			seasionId = jso.getInt("seasonid");
		} catch (Exception e) {
			LOG.error("RustIncidentViewController::rustIncidentView():" + e);
		}
		model.addAttribute("yearId", year);
		model.addAttribute(WrsisPortalConstant.SEASON_ID, seasionId);
		model.addAttribute("seasonList", seasonMasterRepository.viewAllSeasonByStatus());
		List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
		if (regionList != null) {
			model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
		}
		List<Integer> yearlist = DateUtil.fetchYearListInteger();
		model.addAttribute(WrsisPortalConstant.YEAR, yearlist);
		model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);
		model.addAttribute("rustTypeList", typeOfRustService.vewAllTypeOFRust());
		model.addAttribute(WrsisPortalConstant.SEARCH_TYPE, 0);
		return "rustIncidentView";
	}

	@Autowired
	RustIncidentRepository rustIncidentRepository;

	@RequestMapping(value = { "/rustIncidentViewPageWise" })
	public @ResponseBody RustIncidentDataTable publishedSurveyDataPageWise(Model modelHttp, HttpSession session,
			HttpServletRequest request, @RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId, @RequestParam("kebeleId") String kebeleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam("yearId") String yearId,
			@RequestParam(WrsisPortalConstant.SEASON_ID) String seasionId) {

		String start = request.getParameter(WrsisPortalConstant.START);
		String length = request.getParameter(WrsisPortalConstant.LENGTH);
		String draw = request.getParameter(WrsisPortalConstant.DRAW);

		List<RustIncidentEntityBean> rustIncidentList = rustIncidentViewService.viewRustIncidentDetails(
				Integer.parseInt(regionId), Integer.parseInt(zoneId), Integer.parseInt(woredaId),
				Integer.parseInt(kebeleId), Integer.parseInt(yearId), Integer.parseInt(seasionId), startDate, endDate,
				Integer.valueOf(start), Integer.valueOf(length));

		Integer totalCount = rustIncidentRepository.getRustIncidendDetailsCount(Integer.parseInt(regionId),
				Integer.parseInt(zoneId), Integer.parseInt(woredaId), Integer.parseInt(kebeleId),
				Integer.parseInt(yearId), Integer.parseInt(seasionId), startDate, endDate, -1, -1);

		RustIncidentDataTable sdt = new RustIncidentDataTable();
		sdt.setData(rustIncidentList);
		sdt.setRecordsFiltered(totalCount);
		sdt.setRecordsTotal(totalCount);
		sdt.setDraw(Integer.parseInt(draw));

		return sdt;
	}

	@ResponseBody
	@GetMapping("/viewRustIncidentDetailsById")
	public String viewRustIncidentDetailsById(@RequestParam("incidentId") Integer incidentId) {
		return rustIncidentViewService.viewIncidentDetailsById(incidentId);
	}

	@PostMapping("/rustIncidentViewSearch")
	public String rustIncidentViewSearch(@RequestParam(WrsisPortalConstant.SEARCH_TYPE) String searchType,
			@RequestParam(WrsisPortalConstant.REGION_ID) String regionId,
			@RequestParam(WrsisPortalConstant.ZONE_ID) String zoneId,
			@RequestParam(WrsisPortalConstant.WOREDA_ID) String woredaId, @RequestParam("kebeleId") String kebeleId,
			@RequestParam(WrsisPortalConstant.START_DATE) String startDate,
			@RequestParam(WrsisPortalConstant.END_DATE) String endDate, @RequestParam("yearId") String yearId,
			@RequestParam(WrsisPortalConstant.SEASON_ID) String seasonId, Model model) {
		try {

			if (searchType.equals("0")) {
				model.addAttribute(WrsisPortalConstant.SEARCH_TYPE, "0");
			}
			if (searchType.equals("1")) {
				model.addAttribute(WrsisPortalConstant.SEARCH_TYPE, "1");
			}
			if (searchType.equals("x")) {
				model.addAttribute(WrsisPortalConstant.SEARCH_TYPE, "x");
			}
			List<RustIncidentEntityBean> rustIncidentList = rustIncidentViewService.viewRustIncidentDetails(
					Integer.parseInt(regionId), Integer.parseInt(zoneId), Integer.parseInt(woredaId),
					Integer.parseInt(kebeleId), Integer.parseInt(yearId), Integer.parseInt(seasonId), startDate,
					endDate, -1, -1);

			model.addAttribute("seasonList", seasonMasterRepository.viewAllSeasonByStatus());
			List<DemographicEntity> regionList = demographicRepository.viewAllRegionByStatus();
			if (regionList != null) {
				model.addAttribute(WrsisPortalConstant.REGION_LIST, regionList);
			}
			List<Integer> yearlist = DateUtil.fetchYearListInteger();
			model.addAttribute(WrsisPortalConstant.YEAR, yearlist);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, false);
			model.addAttribute(WrsisPortalConstant.REGION_ID, regionId);
			model.addAttribute(WrsisPortalConstant.ZONE_ID, zoneId);
			model.addAttribute(WrsisPortalConstant.WOREDA_ID, woredaId);
			model.addAttribute("kebeleId", kebeleId);
			model.addAttribute(WrsisPortalConstant.START_DATE, startDate);
			model.addAttribute(WrsisPortalConstant.END_DATE, endDate);
			model.addAttribute("yearId", yearId);
			model.addAttribute(WrsisPortalConstant.SEASON_ID, seasonId);
			model.addAttribute(WrsisPortalConstant.SHOW_SEARCH, true);

			LOG.info("rustIncidentList ->"+rustIncidentList);
		} catch (Exception e) {
			LOG.error("RustIncidentViewController::rustIncidentViewSearch():" + e);
		}
		return "rustIncidentView";
	}

	@RequestMapping(value = { "viewRustIncident" })
	public String viewRustIncidentUserwise(Model model, HttpSession session, SearchVo searchVo) {
		try {
			int userId = (int) session.getAttribute(WrsisPortalConstant.USER_ID);
			List<RustIncidentEntityBean> rustIncidentList = null;
			rustIncidentList = rustIncidentViewService.viewALLRustIncidentByUserId(searchVo.getStartDate(),
					searchVo.getEndDate(), userId);
			model.addAttribute("incidentList", rustIncidentList);
		} catch (Exception e) {
			LOG.error("RustIncidentViewController::viewRustIncidentUserwise():" + e);
		}
		return "viewRustIncident";
	}

	@PostMapping("/downloadRustIncidentExcel")
	public void downloadRustIncidentExcel(HttpServletResponse response, HttpSession session,
			@RequestParam("regionIdExcel") String regionId, @RequestParam("zoneIdExcel") String zoneId,
			@RequestParam("woredaIdExcel") String woredaId, @RequestParam("kebeleIdExcel") String kebeleId,
			@RequestParam("startrDIdExcel") String startDate, @RequestParam("endDIdExcel") String endDate,
			@RequestParam("yearIdExcel") String yearId, @RequestParam("seasonIdExcel") String seasonId, Model model) {
		HSSFWorkbook myExcelBook = null;
		OutputStream out = null;
		try {

			List<RustIncidentEntityBean> beans = rustIncidentViewService.viewRustIncidentDetails(
					Integer.parseInt(regionId), Integer.parseInt(zoneId), Integer.parseInt(woredaId),
					Integer.parseInt(kebeleId), Integer.parseInt(yearId), Integer.parseInt(seasonId), startDate,
					endDate, -1, -1);

			PublishSurveyExcel publishedSurveyExcel = new PublishSurveyExcel();
			List<String> questionJsa = new ArrayList<>();
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("RustIncidentViewController::downloadRustIncidentExcel():" + e);
					continue;
				}
				for (int j = 0; j < jsa.length(); j++) {
					if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

						questionJsa.add(jsa.getJSONObject(j).getString("question"));
					}
				}
			}
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("RustIncidentViewController::downloadRustIncidentExcel():" + e);

					continue;
				}
				org.json.JSONObject jso = new org.json.JSONObject();

				for (int j = 0; j < jsa.length(); j++) {
					jso.put(jsa.getJSONObject(j).getString("question"), jsa.getJSONObject(j).getString("options"));
				}

				List<String> questionVals = new ArrayList<>();
				for (int j = 0; j < questionJsa.size(); j++) {
					String qJsa = questionJsa.get(j);
					if (jso.has(qJsa)) {
						questionVals.add(jso.getString(qJsa));
					} else {
						questionVals.add("--");

					}

				}
				rbean.setQuestionsData(questionVals);
				beans.set(i, rbean);
			}

			myExcelBook = publishedSurveyExcel.generateRustIncidentExcel(beans);
			Date date = new Date();
			long time = date.getTime();
			Timestamp ts = new Timestamp(time);
			String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
			String FILENAME = null;

			FILENAME = "Rust_Incident_Report" + timeStamp + ".xls";

			response.setHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
			out = response.getOutputStream();

		} catch (Exception e) {
			LOG.error("RustIncidentViewController::downloadRustIncidentExcel():" + e);

		} finally {
			try {
				myExcelBook.write(out);
				out.flush();
				out.close();
				myExcelBook.close();
			} catch (IOException e) {
				LOG.error("RustIncidentViewController::downloadRustIncidentExcel():" + e);

			}

		}
	}

	// PDF Download For Rust Incident
	@RequestMapping(value = { "/downloadViewRustIncidentPdf" }, produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> downloadDashBoardRustIncidentPDF(HttpServletResponse response,
			HttpSession session, Model model, @RequestParam("regionIdExcel") String regionId,
			@RequestParam("zoneIdExcel") String zoneId, @RequestParam("woredaIdExcel") String woredaId,
			@RequestParam("kebeleIdExcel") String kebeleId, @RequestParam("startrDIdExcel") String startDate,
			@RequestParam("endDIdExcel") String endDate, @RequestParam("yearIdExcel") String yearId,
			@RequestParam("seasonIdExcel") String seasonId) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		List<RustIncidentEntityBean> beans = rustIncidentViewService.viewRustIncidentDetails(Integer.parseInt(regionId),
				Integer.parseInt(zoneId), Integer.parseInt(woredaId), Integer.parseInt(kebeleId),
				Integer.parseInt(yearId), Integer.parseInt(seasonId), startDate, endDate, -1, -1);

		try {
			List<String> questionJsa = new ArrayList<>();
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {

					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("RustIncidentViewController::downloadDashBoardRustIncidentPDF():" + e);

					continue;
				}

				for (int j = 0; j < jsa.length(); j++) {
					if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

						questionJsa.add(jsa.getJSONObject(j).getString("question"));
					}
				}
			}
			for (int i = 0; i < beans.size(); i++) {
				RustIncidentEntityBean rbean = beans.get(i);
				org.json.JSONArray jsa = null;
				try {
					jsa = new org.json.JSONArray(rbean.getQueostions());
				} catch (Exception e) {
					LOG.error("RustIncidentViewController::downloadDashBoardRustIncidentPDF():" + e);

					continue;
				}

				org.json.JSONObject jso = new org.json.JSONObject();

				for (int j = 0; j < jsa.length(); j++) {
					jso.put(jsa.getJSONObject(j).getString("question"), jsa.getJSONObject(j).getString("options"));
				}

				List<String> questionVals = new ArrayList<>();
				for (int j = 0; j < questionJsa.size(); j++) {
					String qJsa = questionJsa.get(j);
					if (jso.has(qJsa)) {
						questionVals.add(jso.getString(qJsa));
					} else {
						questionVals.add("--");

					}

				}
				rbean.setQuestionsData(questionVals);
				beans.set(i, rbean);
			}
		} catch (Exception e) {
			LOG.error("RustIncidentViewController::downloadDashBoardRustIncidentPDF():" + e);

		}
		RustIncidentPDFView rIPDF = new RustIncidentPDFView();
		ByteArrayInputStream bar = null;
		try {
			bar = rIPDF.generateRustIncidentPdf(beans);
		} catch (Exception e) {
			LOG.error("RustIncidentViewController::downloadDashBoardRustIncidentPDF():" + e);

		}
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		String timeStamp = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_SSMMHHDDMMYYYY).format(ts);
		String FILENAME = null;

		FILENAME = "Rust_Incident_Report" + timeStamp + ".pdf";

		headers.add(WrsisPortalConstant.CONTENT_DISPOSITION, "inline; filename=" + FILENAME);

		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(bar));
	}
}
