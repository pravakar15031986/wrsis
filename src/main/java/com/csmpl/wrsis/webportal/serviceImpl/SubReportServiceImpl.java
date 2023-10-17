package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.csmpl.adminconsole.webportal.bean.SearchVo;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.DemographicEntity;
import com.csmpl.wrsis.webportal.entity.Question;
import com.csmpl.wrsis.webportal.entity.RaceAnalysis;
import com.csmpl.wrsis.webportal.entity.RustIncidentEntity;
import com.csmpl.wrsis.webportal.entity.SeasonMonth;
import com.csmpl.wrsis.webportal.repository.DemographicRepository;
import com.csmpl.wrsis.webportal.repository.RaceAnalysisRepository;
import com.csmpl.wrsis.webportal.repository.RustIncidentRepository;
import com.csmpl.wrsis.webportal.repository.SeasonMonthRepository;
import com.csmpl.wrsis.webportal.repository.SubReportRepository;
import com.csmpl.wrsis.webportal.service.SubReportService;

@Service("subReportService")
public class SubReportServiceImpl implements SubReportService {

	public static final Logger LOG = LoggerFactory.getLogger(SubReportServiceImpl.class);

	@Autowired
	SubReportRepository subReportRepository;

	@Autowired
	RustIncidentRepository rustIncidentRepository;

	@Autowired
	DemographicRepository demographicRepository;
	@Autowired
	SeasonMonthRepository seasonMonthRepository;

	@Autowired
	RaceAnalysisRepository raceAnalysisRepository;


	@Override
	public List<SurveyDetailsBean> retriveSearchRustList(int regionId, int zoneId, int woredaId, int kebeleId,
			String startDate, String endDate, int seasonTypeId, int researchId, int rustTypeId, String surveyNo,
			int dataModeId, int yearId,Integer start, Integer length) {

		List<SurveyDetailsBean> searchRustList = new ArrayList<>();
		SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		try {
			String sDate = null;
			if (startDate != null && !"".equalsIgnoreCase(startDate)) {
				sDate = dt2.format(dt1.parse(startDate));
			} else
					sDate = startDate;
			String eDate = null;
			if (endDate != null && !"".equalsIgnoreCase(endDate)) {
				eDate = dt2.format(dt1.parse(endDate));
			} else
					eDate = endDate;

			List<Object[]> searchListObj = subReportRepository.getRustReportList(regionId, zoneId, woredaId, kebeleId,
					sDate, eDate, seasonTypeId, researchId, rustTypeId, surveyNo.toUpperCase(), dataModeId,
					Integer.parseInt("0"), Integer.parseInt("0"), yearId,start,length);
			Integer count=0;
			String surveyNum="";
			String surveyId="";
			for (Object[] ar : searchListObj) {
				if (ar[9].toString().equals("1")) {
					SurveyDetailsBean sdb = new SurveyDetailsBean();
					
					sdb.setsNo((start) + (++count));
					if(ar[0] != null) {
						surveyId=String.valueOf(ar[0]);
					}else {
						surveyId="0";
					}
					
					if(ar[1] != null) {
						surveyNum=String.valueOf(ar[1]);
					}
					else {
						surveyNum="";
					}
					sdb.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" survey-id=\""+surveyId+"\">"+surveyNum+"</a>");
					sdb.setSurveyNoPdf(surveyNum);
					sdb.setSurveyDate((ar[2] != null) ? (String) ar[2] : "");
					sdb.setRegion((ar[4] != null) ? (String) ar[4] : "");
					sdb.setZone((ar[5] != null) ? (String) ar[5] : "");
					sdb.setWoreda((ar[6] != null) ? (String) ar[6] : "");
					sdb.setKebele((ar[7] != null) ? (String) ar[7] : "");
					sdb.setInstitutionName((ar[11] != null) ? (String) ar[11] : "");
					sdb.setRust((ar[8] != null) ? (String) ar[8] : "");
					sdb.setMode((ar[10] != null) ? (String) ar[10] : "");
					searchRustList.add(sdb);
				}
			}

		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveSearchRustList():" + ex);
		}
		return searchRustList;
	}

	@Override
	public List<SampleTagBean> retriveRaceReportList(String surveyId, String sampleId, String surveyDateFrom,
			String surveyDateTo, String recDtFrom, String recDtTo, Integer regionId, Integer rustId, Integer rcId,
			Integer labId, Integer start, Integer length) {
		List<SampleTagBean> searchRustList = new ArrayList<>();
		try {
			if (surveyId == null) {
				surveyId="";
			}
			if (sampleId== null) {
				sampleId="";
			}
			if (surveyDateFrom == null) {
				surveyDateFrom="";
			}
			if (surveyDateTo == null) {
				surveyDateTo="";
			}
			if (recDtFrom == null) {
				recDtFrom="";
			}
			if (recDtTo == null) {
				recDtTo="";
			}
			List<Object[]> raceObj = subReportRepository.getRaceReportList(surveyId,
					sampleId, surveyDateFrom, surveyDateTo, regionId,
					rustId, rcId, labId, recDtFrom, recDtTo,start,length);
			Integer count=0;
			for (Object[] obj : raceObj) {
				SampleTagBean sample = new SampleTagBean();
				sample.setsNo((start) + (++count));
				sample.setSurveyNo("<a href=\"javascript:void(0);\" class=\"viewsurvey\" "
						+ "survey-id=\""+obj[9]+"\">"+obj[0]+"</a>");
				sample.setSampleValue(String.valueOf(obj[1]));
				sample.setRustType(String.valueOf(obj[2]));
				sample.setSurveyDate(String.valueOf(obj[3]));
				sample.setRegion(String.valueOf(obj[5]));
				sample.setZone(String.valueOf(obj[6]));
				sample.setWoreda(String.valueOf(obj[7]));
				sample.setKebel(String.valueOf(obj[8]));
				if((obj[12])==null)
				{
					sample.setResearchCenter("Externel");
				}else {
					sample.setResearchCenter(String.valueOf(obj[12]));
				}
				sample.setRacePublishDate(String.valueOf(obj[15]));
				sample.setRaceResult(String.valueOf(obj[13]));
				Integer analysisId= 0;
				if(obj[16] != null)
				{
					analysisId= Integer.valueOf(String.valueOf(obj[16]));
				}
				//sample.setAnalysisId((obj[16] != null) ? Integer.valueOf(String.valueOf(obj[16])) : 0);// (Integer.valueOf(String.valueOf(obj[16]))
				if(obj[17] != null) 																			// != null) ?
				{
					if(String.valueOf(obj[17]).endsWith(".pdf") || String.valueOf(obj[17]).endsWith(".PDF")) {
					sample.setRaceDocument("<a title=\"\"href=\"javascript:void(0)\" id=\"downloadIcon\""
							+"data-toggle=\"tooltip\" data-placement=\"top\" data-original-title=\"Download\""
							+"onclick=\"generateDocument("+analysisId+")\"><i class=\"fa fa-file-pdf-o\" aria-hidden=\"true\"></i></a>");
					}else {
						sample.setRaceDocument("<a title=\"\"href=\"javascript:void(0)\" id=\"downloadIcon\""
								+"data-toggle=\"tooltip\" data-placement=\"top\" data-original-title=\"Download\""
								+"onclick=\"generateDocument("+analysisId+")\"><i class=\"fa fa-file-image-o\" aria-hidden=\"true\"></i></a>");
					}
				}else {
					sample.setRaceDocument("");
				}
				searchRustList.add(sample);
			}
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveRaceReportList():" + ex);
		}
		return searchRustList;
	}

	@Override
	public void raceReportExcel(HttpServletResponse response, String surveyNo, String sampleId, String startDate,
			String endDate, int demography, int rustType, int center, int labCenter, String recDtFrom, String recDtTo,Integer start,Integer length) {
		try {
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

				// Race Date

				String raceFDate = null;
				if (recDtFrom != null && !"".equalsIgnoreCase(recDtFrom)) {
					SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
					SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
					raceFDate = dt2.format(dt1.parse(recDtFrom));

				} else
					raceFDate = recDtFrom;
				String raceTDate = null;
				if (recDtTo != null && !"".equalsIgnoreCase(recDtTo)) {
					SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
					SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
					raceTDate = dt4.format(dt3.parse(recDtTo));

				} else
						raceTDate = recDtTo;

				List<Object[]> obj = null;
				if ("".equalsIgnoreCase(surveyNo) && "".equalsIgnoreCase(sampleId) && "".equalsIgnoreCase(sDate)
						&& "".equalsIgnoreCase(eDate) && demography == 0 && rustType == 0 && center == 0
						&& labCenter == 0 && "".equalsIgnoreCase(raceFDate) && "".equalsIgnoreCase(raceTDate)) {
					obj = subReportRepository.getRaceReportList("", "", "", "", 0, 0, 0, 0, "", "",-1,-1);
				} else {
					obj = subReportRepository.getRaceReportList(surveyNo, sampleId, sDate, eDate, demography, rustType,
							center, labCenter, raceFDate, raceTDate,start,length);
					
				}
				// Excel Code Start
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Race Analysts Report");

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
				String[] columns = { WrsisPortalConstant.SLNO, WrsisPortalConstant.SURVEY_NUMBER, "Sample ID", "Rust Type", "Survey Date", "Location",
						"Laboratory Name", "Race Published Date", "Race" };

				Cell cell = headRow.createCell(0);
				cell.setCellValue(WrsisPortalConstant.SLNO);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(1);
				cell.setCellValue(WrsisPortalConstant.SURVEY_NUMBER);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(2);
				cell.setCellValue("Sample ID");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(3);
				cell.setCellValue("Rust Type");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(4);
				cell.setCellValue("Survey Date");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(5);
				cell.setCellValue("Location");
				cell.setCellStyle(headerCellStyle);

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 8));

				cell = headRow.createCell(9);
				cell.setCellValue("Laboratory Name");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(10);
				cell.setCellValue("Race Published Date");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(11);
				cell.setCellValue("Race");
				cell.setCellStyle(headerCellStyle);

				Row rowMiddle = sheet.createRow(1);

				cell = rowMiddle.createCell(5);
				cell.setCellValue(WrsisPortalConstant.REGION);
				cell.setCellStyle(headerCellStyle);

				cell = rowMiddle.createCell(6);
				cell.setCellValue(WrsisPortalConstant.ZONE);
				cell.setCellStyle(headerCellStyle);

				cell = rowMiddle.createCell(7);
				cell.setCellValue(WrsisPortalConstant.WOREDA);
				cell.setCellStyle(headerCellStyle);

				cell = rowMiddle.createCell(8);
				cell.setCellValue(WrsisPortalConstant.KEBELE);
				cell.setCellStyle(headerCellStyle);

				int rowNo = 2;
				int count = 1;
				for (Object[] ar : obj) {
					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[0] != null) ? (String) ar[0] : "-NA-");
					row.createCell(2).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(3).setCellValue((ar[2] != null) ? (String) ar[2] : "-NA-");
					row.createCell(4).setCellValue((ar[3] != null) ? (String) ar[3] : "-NA-");
					row.createCell(5).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(6).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(7).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(8).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(9).setCellValue((ar[12] != null) ? (String) ar[12] : "-NA-");
					row.createCell(10).setCellValue((ar[15] != null) ? (String) ar[15] : "-NA-");
					row.createCell(11).setCellValue((ar[13] != null) ? (String) ar[13] : "-NA-");

				}
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
				String FILENAME = "Race-Analysis-Report.xlsx";
				response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
				try {
					workbook.write(response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
					workbook.close();
				} catch (IOException e) {

					LOG.error("SubReportServiceImpl::raceReportExcel():" + e);
				}
				// Excel Code End

			} catch (Exception e) {
				LOG.error("SubReportServiceImpl::raceReportExcel():" + e);

			}
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::raceReportExcel():" + e);

		}

	}

	@Override
	public void rustReportExcel(HttpServletResponse response, Integer yearIdXl, Integer seasonTypeIdXl,
			Integer regionXl, Integer zoneXl, Integer woredaXl, Integer kebeleXl, String startDateXl, String endDateXl,
			Integer researchIdXl, Integer rustTypeIdXl, String surveyNoXl, Integer dataModeIdXl) {
		try {
			try {

				String sDate = null;
				if (startDateXl != null && !"".equalsIgnoreCase(startDateXl)) {
					SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
					SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
					sDate = dt2.format(dt1.parse(startDateXl));

				} else
						sDate = startDateXl;
				String eDate = null;
				if (endDateXl != null && !"".equalsIgnoreCase(endDateXl)) {
					SimpleDateFormat dt3 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
					SimpleDateFormat dt4 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
					eDate = dt4.format(dt3.parse(endDateXl));

				} else
						eDate = endDateXl;
				

				List<Object[]> details = subReportRepository.getRustReportList(regionXl, zoneXl, woredaXl, kebeleXl,
						sDate, eDate, seasonTypeIdXl, researchIdXl, rustTypeIdXl, surveyNoXl, dataModeIdXl,
						Integer.parseInt("0"), Integer.parseInt("0"), yearIdXl,-1,-1);

				// Excel Code Start
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Rust Report Data");

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
				String[] columns = { WrsisPortalConstant.SLNO, WrsisPortalConstant.SURVEY_NUMBER, "Survey Date", WrsisPortalConstant.REGION, WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA, WrsisPortalConstant.KEBELE,
						"Instituition Name", "Type of Rust", "Mode Of Data Collection" };
				for (int i = 0; i < columns.length; i++) {
					Cell cell = headRow.createCell(i);
					cell.setCellValue(columns[i]);
					cell.setCellStyle(headerCellStyle);
				}
				int rowNo = 1;
				int count = 1;
				for (Object[] ar : details) {

					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(2).setCellValue((ar[2] != null) ? (String) ar[2] : "-NA-");
					row.createCell(3).setCellValue((ar[4] != null) ? (String) ar[4] : "-NA-");
					row.createCell(4).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(5).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(6).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(7).setCellValue((ar[11] != null) ? (String) ar[11] : "-NA-");
					row.createCell(8).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(9).setCellValue((ar[10] != null) ? (String) ar[10] : "-NA-");

				}
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
				String FILENAME = "Rust-Report.xlsx";
				response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
				try {
					workbook.write(response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
					workbook.close();
				} catch (IOException e) {

					LOG.error("SubReportServiceImpl::rustReportExcel():" + e);

				}
				// Excel Code End

			} catch (Exception e) {
				LOG.error("SubReportServiceImpl::rustReportExcel():" + e);
			}
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::rustReportExcel():" + e);
		}

	}

	@Override
	public List<SurveyImplementationBean> retriveMonitorData(Integer regionId, Integer zoneId, String monitorNo,
			String recommendationId, Integer userId, String monitorFromDate, String monitorToDate, Integer year,
			Integer seasonId, Integer start, Integer length) {
		List<SurveyImplementationBean> monitorList = new ArrayList<>();
		try {

			if (monitorNo == null) {
				monitorNo="";
			}
			if (recommendationId == null) {
				recommendationId="";
			}
			if (monitorFromDate == null) {
				monitorFromDate="";
			}
			if (monitorToDate == null) {
				monitorToDate="";
			}
			if (year == null) {
				year=0;
			}
			if (seasonId == null) {
				seasonId=0;
			}

			List<Object[]> objList = subReportRepository.getMonitorData(regionId, zoneId,
					monitorNo, recommendationId,userId, monitorFromDate, monitorToDate, year, seasonId, start,length);
			Integer count=0;
			for (Object[] obj : objList) {
				SurveyImplementationBean list = new SurveyImplementationBean();
				list.setsNo((start) + (++count));
				list.setMonitorid(Integer.valueOf(String.valueOf(obj[0])));
				list.setMonitorno(String.valueOf(obj[1]));
				list.setMonitordate(String.valueOf(obj[2]));
				list.setRecomno(String.valueOf(obj[3]));
				list.setRegion(String.valueOf(obj[4]));
				list.setZone(String.valueOf(obj[5]));
				list.setWoreda(String.valueOf(obj[6]));
				list.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				list.setTotalAreaInfested(String.valueOf(obj[8]));
				list.setTotalControlledha(String.valueOf(obj[9]));
				list.setTotalControlledpercent(String.valueOf(obj[10]));
				list.setTotalFungicidesUsed(String.valueOf(obj[15]));
				list.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				list.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				list.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));

				monitorList.add(list);
			}
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveMonitorData():" + ex);

		}
		return monitorList;
	}

	@Override
	public void excelMonitorData(HttpServletResponse response, int region, int zone, String monitor, String recNo,
			Integer userId, String fromdate, String todate, Integer year, Integer seasonid) {

		try {
			try {
				List<Object[]> dataObj = null;
				if (region == 0 && zone == 0 && monitor == null && recNo == null && userId == 0 && fromdate == null
						&& todate == null && year == 0 && seasonid == 0) {
					dataObj = subReportRepository.getMonitorData(0, 0, "", "", 0, "", "", 0, 0,-1,-1);
				} else {
					dataObj = subReportRepository.getMonitorData(region, zone, monitor, recNo, userId, fromdate, todate,
							year, seasonid,-1,-1);
				}
				// Excel Code Start
				Workbook workbook = new XSSFWorkbook();
				Sheet sheet = workbook.createSheet("Dashboard Monitor Data Report");

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
				String[] columns = { WrsisPortalConstant.SLNO, "Monitor Ref No.", "Monitor Created Date", "Recommendationa No.",
						WrsisPortalConstant.REGION, WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA, "No of PAs Affected", "Total Area Infected(ha)",
						"Total Area Controlled(ha)", "Total Area Controlled(%)", "Total Fungicides Used (kg(lit))",
						"Numbers of Farmers Participated on Spraying" };

				Cell cell = headRow.createCell(0);
				cell.setCellValue(WrsisPortalConstant.SLNO);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(1);
				cell.setCellValue("Monitor Ref No.");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(2);
				cell.setCellValue("Monitor Created Date");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(3);
				cell.setCellValue("Recommendationa No.");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(4);
				cell.setCellValue(WrsisPortalConstant.REGION);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(5);
				cell.setCellValue(WrsisPortalConstant.ZONE);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(6);
				cell.setCellValue(WrsisPortalConstant.WOREDA);
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(7);
				cell.setCellValue("No of PAs Affected");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(8);
				cell.setCellValue("Total Area Infected(ha)");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(9);
				cell.setCellValue("Total Area Controlled(ha)");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(10);
				cell.setCellValue("Total Area Controlled(%)");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(11);
				cell.setCellValue("Total Fungicides Used (kg(lit))");
				cell.setCellStyle(headerCellStyle);

				cell = headRow.createCell(12);
				cell.setCellValue("Numbers of Farmers Participated on Spraying");
				cell.setCellStyle(headerCellStyle);

				sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 14));

				Row rowMiddle = sheet.createRow(1);

				cell = rowMiddle.createCell(12);
				cell.setCellValue(WrsisPortalConstant.MALE);
				cell.setCellStyle(headerCellStyle);

				cell = rowMiddle.createCell(13);
				cell.setCellValue(WrsisPortalConstant.FEMALE);
				cell.setCellStyle(headerCellStyle);

				cell = rowMiddle.createCell(14);
				cell.setCellValue("Total");
				cell.setCellStyle(headerCellStyle);

				int rowNo = 2;
				int count = 1;
				for (Object[] ar : dataObj) {
					Row row = sheet.createRow(rowNo++);
					row.createCell(0).setCellValue(Integer.toString(count++));
					row.createCell(1).setCellValue((ar[1] != null) ? (String) ar[1] : "-NA-");
					row.createCell(2).setCellValue((ar[2] != null) ? (String) ar[2] : "-NA-");
					row.createCell(3).setCellValue((ar[3] != null) ? (String) ar[3] : "-NA-");
					row.createCell(4).setCellValue((ar[4] != null) ? (String) ar[4] : "-NA-");
					row.createCell(5).setCellValue((ar[5] != null) ? (String) ar[5] : "-NA-");
					row.createCell(6).setCellValue((ar[6] != null) ? (String) ar[6] : "-NA-");
					row.createCell(7).setCellValue((ar[7] != null) ? (String) ar[7] : "-NA-");
					row.createCell(8).setCellValue((ar[8] != null) ? (String) ar[8] : "-NA-");
					row.createCell(9).setCellValue((ar[9] != null) ? (String) ar[9] : "-NA-");
					row.createCell(10).setCellValue((ar[10] != null) ? (String) ar[10] : "-NA-");
					row.createCell(11).setCellValue((ar[15] != null) ? (String) ar[15] : "-NA-");
					row.createCell(12).setCellValue((ar[11] != null) ? (String) ar[11] : "-NA-");
					row.createCell(13).setCellValue((ar[12] != null) ? (String) ar[12] : "-NA-");
					row.createCell(14).setCellValue((ar[13] != null) ? (String) ar[13] : "-NA-");

				}
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				response.setContentType(WrsisPortalConstant.APPLICATION_VND_MS_EXCEL);
				String FILENAME = "Dashboard-Monitor-Report.xlsx";
				response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME);
				try {
					workbook.write(response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
					workbook.close();
				} catch (IOException e) {

					LOG.error("SubReportServiceImpl::excelMonitorData():" + e);

				}
				// Excel Code End
			} catch (Exception ex) {
				LOG.error("SubReportServiceImpl::excelMonitorData():" + ex);
			}
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::excelMonitorData():" + ex);
		}

	}

	@Override
	public List<RustIncidentEntityBean> retriveRustIncidendReport() {
		List<RustIncidentEntityBean> incidendReportList = new ArrayList<>();
		try {
			List<RustIncidentEntity> inciList = rustIncidentRepository.viewAllIncident();
			for (RustIncidentEntity incident : inciList) {
				RustIncidentEntityBean obj = new RustIncidentEntityBean();
				DemographicEntity demo = demographicRepository
						.findByparentIdAndStatu(incident.getDemographyId().getDemographyId());
				if (demo.getLevelId() == 5) {
					obj.setKebeleName(demo.getDemographyName());
					DemographicEntity woreda = demographicRepository.findByparentIdAndStatu(demo.getParentId());
					obj.setWoredaName(woreda.getDemographyName());
				} else {
					obj.setWoredaName(demo.getDemographyName());
					obj.setKebeleName("-NA-");
				}
				obj.setIncidentId(incident.getIncidentId());
				obj.setRustDate(incident.getRustDate());
				obj.setSeasonId(incident.getSeasonId());
				obj.setLongitude(incident.getLongitude());
				obj.setLatitude(incident.getLatitude());
				obj.setYear(incident.getYear());
				incidendReportList.add(obj);
			}
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveRustIncidentReport():" + ex);

		}
		return incidendReportList;
	}

	@Override
	public List<RustIncidentEntityBean> retriveIncidendReport(SearchVo searchVo) {
		List<RustIncidentEntityBean> reportList = new ArrayList<>();
		try {

			

			List<Object[]> objList = subReportRepository.getIncidendReportListDetails(searchVo.getRegionId(),
					searchVo.getZoneId(), searchVo.getWoredaId(), searchVo.getKebeleId(), searchVo.getYearId(),
					searchVo.getSeasonTypeId());

			for (Object[] obj : objList) {
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
				reportList.add(objReport);
			}
			if (searchVo.getYearId() == 0) {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				Integer currentYear = year;
				searchVo.setYearId(currentYear);
			}
			if (searchVo.getSeasonTypeId() == 0) {
				Date date = new Date();
				LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				int month = localDate.getMonthValue();
				SeasonMonth seasonId = seasonMonthRepository.findSeasonIdByMonthId(month);
				searchVo.setSeasonTypeId(seasonId.getSeasonId());
			}
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveIncidentReport():" + ex);

		}
		return reportList;
	}

	@Override
	public List<Question> retriveQuesnList() {

		return null; 
	}

	@Override
	public List<SurveyImplementationBean> getMonitorDataforPdf(Integer regionPDF, Integer zonePDF, String monitorPDF,
			String recNoPDF, Integer userId, String fromdatePDF, String todatePDF, Integer yearPDF, Integer seasonPDF) {

		List<SurveyImplementationBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = null;
			if (regionPDF == 0 && zonePDF == 0 && monitorPDF == null && recNoPDF == null && userId == 0
					&& fromdatePDF == null && todatePDF == null && yearPDF == 0 && seasonPDF == 0) {
				details = subReportRepository.getMonitorData(0, 0, "", "", 0, "", "", 0, 0,-1,-1);
			} else {
				details = subReportRepository.getMonitorData(regionPDF, zonePDF, monitorPDF, recNoPDF, userId,
						fromdatePDF, todatePDF, yearPDF, seasonPDF,-1,-1);
			}
			
			for (Object[] obj : details) {
				SurveyImplementationBean objReport = new SurveyImplementationBean();

				objReport.setMonitorno(String.valueOf(obj[1]));
				objReport.setMonitordate(String.valueOf(obj[2]));
				objReport.setRecomno(String.valueOf(obj[3]));
				objReport.setRegion(String.valueOf(obj[4]));
				objReport.setZone(String.valueOf(obj[5]));
				objReport.setWoreda(String.valueOf(obj[6]));
				objReport.setNopaeffected(Integer.valueOf(String.valueOf(obj[7])));
				objReport.setTotalAreaInfested(String.valueOf(obj[8]));
				objReport.setTotalControlledha(String.valueOf(obj[9]));
				objReport.setTotalControlledpercent(String.valueOf(obj[10]));
				objReport.setTotalFungicidesUsed(String.valueOf(obj[15]));
				objReport.setMalefarmer(Integer.valueOf(String.valueOf(obj[11])));
				objReport.setFemalefarmer(Integer.valueOf(String.valueOf(obj[12])));
				objReport.setTotalfarmer(Integer.valueOf(String.valueOf(obj[13])));
				beans.add(objReport);
			}
		} catch (Exception e) {

			LOG.error("SubReportServiceImpl::getForPdf():" + e);

		}

		return beans;
	}

	@Override
	public List<SampleTagBean> getRaceAnalysisPDFData(String surveyidPDF, String sampidPDF, String startDatePDF,
			String endDatePDF, int demographyPDF, int rustidPDF, int centeridPDF, int labCenteridPDF,
			String recDtFromPDF, String recDtToPDF,Integer start,Integer length) {

		List<SampleTagBean> beans = new ArrayList<>();
		try {
			List<Object[]> details = null;
			if (surveyidPDF == null && sampidPDF == null && startDatePDF == null && endDatePDF == null
					&& demographyPDF == 0 && rustidPDF == 0 && centeridPDF == 0 && labCenteridPDF == 0) {
				details = subReportRepository.getRaceReportList("", "", "", "", 0, 0, 0, 0, "","",-1, -1);
			} else {
				details = subReportRepository.getRaceReportList(surveyidPDF, sampidPDF, startDatePDF, endDatePDF,
						demographyPDF, rustidPDF, centeridPDF, labCenteridPDF, recDtFromPDF, recDtToPDF,start,length);
			}
	
			for (Object[] obj : details) {
				SampleTagBean objReport = new SampleTagBean();

				objReport.setSurveyNo(String.valueOf(obj[0]));
				objReport.setSampleValue(String.valueOf(obj[1]));
				objReport.setRustType(String.valueOf(obj[2]));
				objReport.setSurveyDate(String.valueOf(obj[3]));
				objReport.setRegion(String.valueOf(obj[5]));
				objReport.setZone(String.valueOf(obj[6]));
				objReport.setWoreda(String.valueOf(obj[7]));
				objReport.setKebel(String.valueOf(obj[8]));
				objReport.setResearchCenter(String.valueOf(obj[12]));
				objReport.setRaceResult(String.valueOf(obj[13]));
				objReport.setRacePublishDate(String.valueOf(obj[15]));
				beans.add(objReport);
			}
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::getRaceAnalysisPDFData():" + e);

		}
		return beans;
	}

	@Override
	public String checkRaceFilePath(Integer fileId) {
		JSONObject jObject = new JSONObject();
		try {
			RaceAnalysis obj = raceAnalysisRepository.getOne(fileId);
			if (obj.getRaceDoc() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH;
				Path path = Paths.get(dataDirectory, obj.getRaceDoc());
				if (Files.exists(path)) {
					jObject.put("msg", "Yes");
				} else {
					jObject.put("msg", "No");
				}
			}
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::checkraceFilePath():" + e);

		}
		return jObject.toString();

	}

	@Override
	public void downloadRaceFile(HttpServletResponse response, Integer fileId, ModelMap model) {
		try {
			RaceAnalysis obj = raceAnalysisRepository.getOne(fileId);
			if (obj.getRaceDoc() != null) {
				String dataDirectory = WrsisPortalConstant.wrsisPropertiesFileConstants.WR_RACE_ANALYSIS_PUBLISH;
				Path path = Paths.get(dataDirectory, obj.getRaceDoc());
				if (Files.exists(path)) {
					response.setContentType("application/pdf");
					response.addHeader(WrsisPortalConstant.CONTENT_DISPOSITION, "attachment; filename=" + obj.getRaceDoc());
					try {
						Files.copy(path, response.getOutputStream());
						response.getOutputStream().flush();

					} catch (IOException ex) {
						LOG.error("SubReportServiceImpl::downloadRaceFile():" + ex);

					}
				} else {
					model.addAttribute("msg", "File not found");
				}
			}
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::downloadRaceFile():" + e);

		}

	}

	@Override
	public Integer retriveRaceReportListCount(String surveyId, String sampleId, String surveyDateFrom,
			String surveyDateTo, String recDtFrom, String recDtTo, Integer regionId, Integer rustId, Integer rcId,
			Integer labId, int start, int length) {
		Integer count=0;
		try {
			if (surveyId == null) {
				surveyId="";
			}
			if (sampleId== null) {
				sampleId="";
			}
			if (surveyDateFrom == null) {
				surveyDateFrom="";
			}
			if (surveyDateTo == null) {
				surveyDateTo="";
			}
			if (recDtFrom == null) {
				recDtFrom="";
			}
			if (recDtTo == null) {
				recDtTo="";
			}
			count = subReportRepository.getRaceReportListCount(surveyId,
					sampleId, surveyDateFrom, surveyDateTo, regionId,
					rustId, rcId, labId, recDtFrom, recDtTo,start,length);
			
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveRaceReportList():" + ex);
		}
		return count;
	}

	@Override
	public Integer retriveRustReportListCount(Integer regionId, Integer zoneId, Integer woredaId, Integer kebeleId,
			String surveyDateFrom, String surveyDateTo, Integer seasonId, Integer rcId, Integer rustId, String surveyNo,
			Integer dataCollectModeId, Integer year, int start, int length) {
		Integer count=0;
		SimpleDateFormat dt1 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
		SimpleDateFormat dt2 = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_YYYYMMDD);
		
		try {
			String sDate = null;
			if (surveyDateFrom != null && !"".equalsIgnoreCase(surveyDateFrom)) {
				sDate = dt2.format(dt1.parse(surveyDateFrom));
			} else
					sDate = surveyDateFrom;
			String eDate = null;
			if (surveyDateTo != null && !"".equalsIgnoreCase(surveyDateTo)) {
				eDate = dt2.format(dt1.parse(surveyDateTo));
			} else
					eDate = surveyDateTo;

			count = subReportRepository.getRustReportListCount(regionId, zoneId, woredaId, kebeleId,
					sDate, eDate, seasonId, rcId, rustId, surveyNo.toUpperCase(), dataCollectModeId,
					Integer.parseInt("0"), Integer.parseInt("0"), year,start,length);
		} catch (Exception ex) {
			LOG.error("SubReportServiceImpl::retriveRustReportListCount():" + ex);
		}
		return count;
	}

	@Override
	public Integer retriveMonitorDataCount(Integer regionId, Integer zoneId, String monitorNo, String recommendationId,
			Integer userId, String monitorFromDate, String monitorToDate, Integer year, Integer seasonId, int start,
			int length) {
		Integer count = 0;

		try {
			if (monitorNo == null) {
				monitorNo = "";
			}
			if (recommendationId == null) {
				recommendationId = "";
			}
			if (monitorFromDate == null) {
				monitorFromDate = "";
			}
			if (monitorToDate == null) {
				monitorToDate = "";
			}
			if (year == null) {
				year = 0;
			}
			if (seasonId == null) {
				seasonId = 0;
			}

			count = subReportRepository.getMonitorDataCount(regionId, zoneId, monitorNo, recommendationId, userId,
					monitorFromDate, monitorToDate, year, seasonId, start, length);
		} catch (Exception e) {
			LOG.error("SubReportServiceImpl::retriveMonitorDataCount():" + e);
		}
		return count;
	}
}
