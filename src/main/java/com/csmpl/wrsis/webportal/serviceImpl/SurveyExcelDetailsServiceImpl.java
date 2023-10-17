package com.csmpl.wrsis.webportal.serviceImpl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyExcelFilesBean;
import com.csmpl.wrsis.webportal.bean.SuveyDetailsSampleReportDataBean;
import com.csmpl.wrsis.webportal.bean.SuveyOtherDiseaseReportDataBean;
import com.csmpl.wrsis.webportal.entity.DiseaseMaster;
import com.csmpl.wrsis.webportal.entity.RustTypeMasterEntity;
import com.csmpl.wrsis.webportal.entity.SampleTypeMaster;
import com.csmpl.wrsis.webportal.repository.DiseaseMasterRepository;
import com.csmpl.wrsis.webportal.repository.RustTypeRepository;
import com.csmpl.wrsis.webportal.repository.SampleTypeRepository;
import com.csmpl.wrsis.webportal.repository.SurveyExcelFilesRepository;
import com.csmpl.wrsis.webportal.service.CommonService;
import com.csmpl.wrsis.webportal.service.SurveyExcelDetailsService;

@Service
public class SurveyExcelDetailsServiceImpl implements SurveyExcelDetailsService {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyExcelDetailsServiceImpl.class);
	@Autowired
	CommonService commonService;
	@Autowired
	RustTypeRepository rustTypeRepository;
	@Autowired
	SampleTypeRepository sampleTypeRepository;
	@Autowired
	DiseaseMasterRepository diseaseMasterRepository;

	@Override
	public HSSFWorkbook createExcelSurvey(HSSFWorkbook workbook, List<SurveyDetailsBean> bean) {
		HSSFWorkbook book = workbook;
		try {

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Sheet sheetSample = workbook.getSheet(WrsisPortalConstant.SURVEY_DATA);
			Row mainRow = sheetSample.getRow(0);
			int mainRowEnd = mainRow.getLastCellNum();

			sheetSample.addMergedRegion(new CellRangeAddress(1, 2, mainRowEnd, mainRowEnd));

			int rowNo = 3;
			int j = 1;
			for (SurveyDetailsBean survey : bean) {
				try {
					JSONObject response = commonService.getFormmatedJsonBySurveyId(survey.getSurveyId());

					Row row = sheetSample.createRow(rowNo++);
					row.createCell(0).setCellValue(j);
					j++;
					// Plotting Data for Surveyor's Name Start
					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					CellStyle sty = workbook.createCellStyle();
					sty.setWrapText(true);
					Cell cell1 = row.createCell(2);
					cell1.setCellStyle(sty);
					cell1.setCellValue(str);

					row.createCell(3).setCellValue(rcName);
					// Plotting Data for Research Center Name End

					cell1 = row.createCell(1);
					cell1.setCellStyle(sty);
					cell1.setCellValue(!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					row.createCell(4).setCellValue(response.getString(WrsisPortalConstant.SEASION_NAME));
					row.createCell(5).setCellValue(response.getString(WrsisPortalConstant.REGION_NAME1));
					row.createCell(6).setCellValue(response.getString(WrsisPortalConstant.ZONE_NAME1));
					row.createCell(7).setCellValue(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					row.createCell(8).setCellValue(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					row.createCell(9).setCellValue(response.getString(WrsisPortalConstant.KEBEL_WOREDA_ADDR));
					row.createCell(10).setCellValue(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					row.createCell(11).setCellValue(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					row.createCell(12).setCellValue(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);
					if (surveyDate != null && !surveyDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(surveyDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(13).setCellValue(strDate);
					} else {
						row.createCell(13).setCellValue(" ");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(plantDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(14).setCellValue(strDate);
					} else {
						row.createCell(14).setCellValue(" ");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(obserDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(15).setCellValue(strDate);
					} else {
						row.createCell(15).setCellValue(" ");
					}

					row.createCell(16).setCellValue((response.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID)) ? "Yes" : "No");
					row.createCell(17)
							.setCellValue(!response.getString(WrsisPortalConstant.REMARK).isEmpty() ? response.getString(WrsisPortalConstant.REMARK) : "");
					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					row.createCell(18).setCellValue(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					row.createCell(19).setCellValue(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					row.createCell(20).setCellValue(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					row.createCell(21).setCellValue(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					row.createCell(22).setCellValue(
							!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");
					row.createCell(23).setCellValue((response.getBoolean("rustAffectedId")) ? "Yes" : "No");
					int cellNo = 23;
					JSONObject locations = getAllFieldLocations(sheetSample.getRow(0));

					int rustStart = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(0);
					int rustEnd = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(1);
					int toalRusts = Math.round(((rustEnd - rustStart) / 3));
					ArrayList<String> rustArr = new ArrayList<>();
					List<RustTypeMasterEntity> rusrtList = rustTypeRepository.fetchAllActiveRustType();
					int start = rustStart;
					for (int l = 0; l < toalRusts; l++) {
					
							rustArr.add(rusrtList.get(l).getVchRustType());
							start = start + 3;	
						
						
					}
					JSONArray rustDetails = response.getJSONArray(WrsisPortalConstant.RUST_DETAILS2);
					for (int l = 0; l < rustDetails.length(); l++) {
						JSONObject json = rustDetails.getJSONObject(l);
						int index = 0;
						Integer rustId = 0;
						int c = 0;
						for (Iterator iterator = rustArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							
							if (string.toLowerCase().contains(json.getString(WrsisPortalConstant.RUST_TYPE_NAME).toLowerCase())) {
								index = c;
								rustId = json.getInt(WrsisPortalConstant.RUST_TYPE_ID);
								break;
							}
							c++;

						}

						//modification added by debendra nayak date=02-09-2021 for headincident and headseverity(yellow rust)
						
							
						start = rustStart + ((index) * 3);
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME)
										: "");
						
						if(rustId == 3) {   //If rust type id 3 than add headincident and headseverity
							//start = rustStart + ((index) * 5);
							start++;
							row.createCell(start)
									.setCellValue(!rustDetails.getJSONObject(l).getString("headIncidentId").isEmpty()
											? rustDetails.getJSONObject(l).getString("headIncidentId")
											: "");
							start++;
							row.createCell(start)
									.setCellValue(!rustDetails.getJSONObject(l).getString("headSeverityId").isEmpty()
											? rustDetails.getJSONObject(l).getString("headSeverityId")
											: "");
						}
						
						
						
					}

					cellNo = rustEnd;
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID)) ? "Yes" : "No");

					// Plotting data from Sample Details Start
					int sampleStar = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(0);
					int sampleEnd = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(1);
					int totalSample = Math.round(((sampleEnd - sampleStar) / 3));

					ArrayList<String> sampleArr = new ArrayList<>();
					int smp_start = sampleStar;
					for (int k = 0; k < totalSample; k++) {
						sampleArr.add(getCellData(sheetSample.getRow(1), smp_start + 1));
						smp_start = smp_start + 3;
					}
					JSONArray sampleDetails = response.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);
					for (int k = 0; k < sampleDetails.length(); k++) {
						JSONObject jObject = sampleDetails.getJSONObject(k);
						int index = 0;
						int c = 0;
						for (Iterator iterator = sampleArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jObject.getString(WrsisPortalConstant.SAMPLE_TYPE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						smp_start = sampleStar + ((index) * 3) + 1;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_COUNT_ID));
						smp_start++;
						row.createCell(smp_start).setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_IDS));
						smp_start++;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_REMARKS));
					}
					// Plotting data for Sample Details End

					// Plotting Data for Fungicide Details Start
					cellNo = sampleEnd;
					JSONObject jsonObject = response.getJSONObject(WrsisPortalConstant.FUNGICIDE_JSON);
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.FUNGISIDE_ID)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME)
									: " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString("dose").isEmpty() ? jsonObject.getString("dose") : " ");
					cellNo++;

					String spryDate = jsonObject.getString(WrsisPortalConstant.SPRAY_DATE);

					if (spryDate != null && !spryDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(spryDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(cellNo).setCellValue(strDate);
					} else {
						row.createCell(cellNo).setCellValue(" ");
					}

					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME)
									: "");

					// Plotting Data for Fungicide Details End

					// Plotting Data for OtherDisease Start

					int diseaseStart = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(0);
					int diseaseEnd = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(1);
					int totalDisease = Math.round(((diseaseEnd - diseaseStart) / 2));
					ArrayList<String> diseasArr = new ArrayList<>();
					int disea_strat = diseaseStart;
					for (int m = 0; m < totalDisease; m++) {
						diseasArr.add(getCellData(sheetSample.getRow(1), disea_strat));
						disea_strat = disea_strat + 2;
					}
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					for (int m = 0; m < diseaseDetails.length(); m++) {
						JSONObject jsObject = diseaseDetails.getJSONObject(m);
						int index = 0;
						int c = 0;
						for (Iterator iterator = diseasArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jsObject.getString(WrsisPortalConstant.DISEASE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						disea_strat = diseaseStart + ((index) * 2);
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
						disea_strat++;
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					}
					// Plotting Data for OtherDisease End

					cellNo = diseaseEnd;
					// Plotting Data for Other Details Start
					JSONObject jsonOtherDtl = response.getJSONObject("otherDetails");
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.IRRIGATED1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.WEED_CONTROL1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue(
							!jsonOtherDtl.getString("soilColor").isEmpty() ? jsonOtherDtl.getString(WrsisPortalConstant.SOIL_NAME) : " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME).isEmpty()
									? jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME)
									: " ");
					// Plotting Data for Other Details End

					// Plotting Data for Any Other Disease start
					
					// Plotting Data for Any Other Disease End

					
				} catch (Exception e2) {
					LOG.error("SurveyExcelDetailsServiceImpl::createExcelSurvey():" + e2);
					continue;
				}
			}

		} catch (Exception e) {

			LOG.error("SurveyExcelDetailsServiceImpl::createExcelSurvey():" + e);

		}
		return book;
	}

	public JSONObject getAllFieldLocations(Row row) throws JSONException {
		JSONArray json = new JSONArray();

		JSONArray jsa = new JSONArray();
		
		for (int j = 0; j < row.getLastCellNum(); j++) {

			String cellData = getCellData(row, j).trim();
			if (!cellData.equals("")) {
				jsa = new JSONArray();
				jsa.put(cellData);
				jsa.put(j);
				json.put(jsa);

			}

		}

		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for (int i = 0; i < json.length() - 1; i++) {
			arr = new JSONArray();
			String cellData = json.getJSONArray(i).getString(0).trim().replaceAll(" ", "");
			int start = json.getJSONArray(i).getInt(1);
			int end = json.getJSONArray(i + 1).getInt(1);
			arr.put(start);
			arr.put(end);
			obj.put(cellData, arr);
		}
		arr = new JSONArray();
		int start = json.getJSONArray(json.length() - 1).getInt(1);
		int end = row.getLastCellNum();
		String cellData = json.getJSONArray(json.length() - 1).getString(0).trim().replaceAll(" ", "");
		arr.put(start);
		arr.put(end);
		obj.put(cellData, arr);

		return obj;
	}

	public String getCellData(Row row, int columnIndex) {

		int cellType;
		try {
			cellType = row.getCell(columnIndex).getCellType();
		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::getCellData():" + e);

			return "";// if no value selected
		}

		if (cellType == CELL_TYPE_STRING) {
			return row.getCell(columnIndex).getStringCellValue();
		}
		if (HSSFDateUtil.isCellDateFormatted(row.getCell(columnIndex))) {
			DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY);
			return (row.getCell(columnIndex).getDateCellValue() != null)
					? dateFormat.format(row.getCell(columnIndex).getDateCellValue())
					: " ";
		
		}

		if (cellType == CELL_TYPE_NUMERIC) {
			return String.valueOf(row.getCell(columnIndex).getNumericCellValue());
		}

		if (cellType == CELL_TYPE_BLANK) {
			return "";
		}
		return "";
	}

	public static final int CELL_TYPE_NUMERIC = 0;
	public static final int CELL_TYPE_STRING = 1;
	public static final int CELL_TYPE_FORMULA = 2;
	public static final int CELL_TYPE_BLANK = 3;
	public static final int CELL_TYPE_BOOLEAN = 4;
	public static final int CELL_TYPE_ERROR = 5;

	@Override
	public HSSFWorkbook createExcelGlobalRust(HSSFWorkbook workbook, List<SurveyDetailsBean> bean) {

		HSSFWorkbook book = workbook;
		try {

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Sheet sheetSample = workbook.getSheet(WrsisPortalConstant.SURVEY_DATA);
			Row mainRow = sheetSample.getRow(0);
			int mainRowEnd = mainRow.getLastCellNum();

			sheetSample.addMergedRegion(new CellRangeAddress(1, 2, mainRowEnd, mainRowEnd));

			int rowNo = 3;
			int j = 1;
			for (SurveyDetailsBean survey : bean) {
				try {
					JSONObject response = commonService.getFormmatedJsonBySurveyId(survey.getSurveyId());

					Row row = sheetSample.createRow(rowNo++);
					row.createCell(0).setCellValue(j);
					j++;

					// Plotting Data for Surveyor's Name Start
					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					CellStyle sty = workbook.createCellStyle();
					sty.setWrapText(true);
					
					Cell cell1 = row.createCell(3);
					cell1.setCellStyle(sty);
					cell1.setCellValue(str);
					row.createCell(2).setCellValue(countryName);

					row.createCell(4).setCellValue(rcName);
					// Plotting Data for Research Center Name End

					cell1 = row.createCell(1);
					cell1.setCellStyle(sty);
					cell1.setCellValue(!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					row.createCell(5).setCellValue(response.getString(WrsisPortalConstant.SEASION_NAME));
					row.createCell(6).setCellValue(response.getString(WrsisPortalConstant.REGION_NAME1));
					row.createCell(7).setCellValue(response.getString(WrsisPortalConstant.ZONE_NAME1));
					row.createCell(8).setCellValue(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					row.createCell(9).setCellValue(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					row.createCell(10).setCellValue(response.getString(WrsisPortalConstant.KEBEL_WOREDA_ADDR));
					row.createCell(11).setCellValue(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					row.createCell(12).setCellValue(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					row.createCell(13).setCellValue(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);
					if (surveyDate != null && !surveyDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(surveyDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(14).setCellValue(strDate);
					} else {
						row.createCell(14).setCellValue(" ");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(plantDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(15).setCellValue(strDate);
					} else {
						row.createCell(15).setCellValue(" ");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(obserDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(16).setCellValue(strDate);
					} else {
						row.createCell(16).setCellValue(" ");
					}

					row.createCell(17).setCellValue((response.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID)) ? "Yes" : "No");
					row.createCell(18)
							.setCellValue(!response.getString(WrsisPortalConstant.REMARK).isEmpty() ? response.getString(WrsisPortalConstant.REMARK) : "");

					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					row.createCell(19).setCellValue(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					row.createCell(20).setCellValue(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					row.createCell(21).setCellValue(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					row.createCell(22).setCellValue(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					row.createCell(23).setCellValue(
							!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");

					
					
					int cellNo = 24;
					JSONObject locations = getAllFieldLocations(sheetSample.getRow(0));
					int rustStart = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(0);
					int rustEnd = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(1);
					int toalRusts = Math.round(((rustEnd - rustStart) / 3));
					List<RustTypeMasterEntity> rusrtList = rustTypeRepository.fetchAllActiveRustType();
					ArrayList<String> rustArr = new ArrayList<>();
					int start = rustStart;
					for (int l = 0; l < toalRusts; l++) {
						
						rustArr.add(rusrtList.get(l).getVchRustType());
						start = start + 3;
					}
					JSONArray rustDetails = response.getJSONArray(WrsisPortalConstant.RUST_DETAILS2);
					for (int l = 0; l < rustDetails.length(); l++) {
						JSONObject json = rustDetails.getJSONObject(l);
						int index = 0;
						int c = 0;
						for (Iterator iterator = rustArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(json.getString(WrsisPortalConstant.RUST_TYPE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;

						}

						
						start = rustStart + ((index) * 3);
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME)
										: "");
					}

					cellNo = rustEnd;
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID)) ? "Yes" : "No");

					// Plotting data from Sample Details Start
					int sampleStar = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(0);
					int sampleEnd = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(1);
					int totalSample = Math.round(((sampleEnd - sampleStar) / 3));

					ArrayList<String> sampleArr = new ArrayList<>();
					int smp_start = sampleStar;
					for (int k = 0; k < totalSample; k++) {
						sampleArr.add(getCellData(sheetSample.getRow(1), smp_start + 1));
						smp_start = smp_start + 3;
					}
					JSONArray sampleDetails = response.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);
					for (int k = 0; k < sampleDetails.length(); k++) {
						JSONObject jObject = sampleDetails.getJSONObject(k);
						int index = 0;
						int c = 0;
						for (Iterator iterator = sampleArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jObject.getString(WrsisPortalConstant.SAMPLE_TYPE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						smp_start = sampleStar + ((index) * 3) + 1;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_COUNT_ID));
						smp_start++;
						row.createCell(smp_start).setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_IDS));
						smp_start++;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_REMARKS));
					}
					// Plotting data for Sample Details End

					// Plotting Data for Fungicide Details Start
					cellNo = sampleEnd;
					JSONObject jsonObject = response.getJSONObject(WrsisPortalConstant.FUNGICIDE_JSON);
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.FUNGISIDE_ID)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME)
									: " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString("dose").isEmpty() ? jsonObject.getString("dose") : " ");
					cellNo++;

					String spryDate = jsonObject.getString(WrsisPortalConstant.SPRAY_DATE);

					if (spryDate != null && !spryDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(spryDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(cellNo).setCellValue(strDate);
					} else {
						row.createCell(cellNo).setCellValue(" ");
					}

					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME)
									: "");

					// Plotting Data for Fungicide Details End

					// Plotting Data for OtherDisease Start

					int diseaseStart = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(0);
					int diseaseEnd = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(1);
					int totalDisease = Math.round(((diseaseEnd - diseaseStart) / 2));
					ArrayList<String> diseasArr = new ArrayList<>();
					int disea_strat = diseaseStart;
					for (int m = 0; m < totalDisease; m++) {
						diseasArr.add(getCellData(sheetSample.getRow(1), disea_strat));
						disea_strat = disea_strat + 2;
					}
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					for (int m = 0; m < diseaseDetails.length(); m++) {
						JSONObject jsObject = diseaseDetails.getJSONObject(m);
						int index = 0;
						int c = 0;
						for (Iterator iterator = diseasArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jsObject.getString(WrsisPortalConstant.DISEASE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						disea_strat = diseaseStart + ((index) * 2);
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
						disea_strat++;
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					}
					// Plotting Data for OtherDisease End

					cellNo = diseaseEnd;
					// Plotting Data for Other Details Start
					JSONObject jsonOtherDtl = response.getJSONObject("otherDetails");
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.IRRIGATED1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.WEED_CONTROL1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue(
							!jsonOtherDtl.getString("soilColor").isEmpty() ? jsonOtherDtl.getString(WrsisPortalConstant.SOIL_NAME) : " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME).isEmpty()
									? jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME)
									: " ");
					// Plotting Data for Other Details End

					// Plotting Data for Any Other Disease start
					
					// Plotting Data for Any Other Disease End

				} catch (Exception e2) {
					LOG.error("SurveyExcelDetailsServiceImpl::createExcelGlobalRust():" + e2);

					continue;
				}
			}

		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::createExcelGlobalRust():" + e);
		}
		return book;

	}

	@Override
	public List<SuveyDetailsSampleReportDataBean> createGRTableSurvey(List<SurveyDetailsBean> bean) {
		List<SuveyDetailsSampleReportDataBean> surveyList = new ArrayList<>();
		try {
			SuveyDetailsSampleReportDataBean surveyData = null;
			for (SurveyDetailsBean survey : bean) {
				surveyData = new SuveyDetailsSampleReportDataBean();

				try {
					LOG.info("Survey Id==" + survey.getSurveyId());
					JSONObject response = commonService.getFormmatedJsonBySurveyId(survey.getSurveyId());

					

					surveyData.setSurveyNo(
							!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					surveyData.setCountry(countryName);
					surveyData.setSurveyourName(str);
					surveyData.setRcName(rcName);
					surveyData.setSeason(response.getString(WrsisPortalConstant.SEASION_NAME));
					surveyData.setRegion(response.getString(WrsisPortalConstant.REGION_NAME1));
					surveyData.setZone(response.getString(WrsisPortalConstant.ZONE_NAME1));
					surveyData.setWoreda(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					surveyData.setKebele(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					surveyData.setAltitude(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					surveyData.setLongitude(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					surveyData.setLatitude(response.getString(WrsisPortalConstant.LATITUDE_ID));
					surveyData.setAddress(response.getString(WrsisPortalConstant.KEBEL_WOREDA_ADDR));
					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);

					if (surveyDate != null && !surveyDate.isEmpty()) {
						surveyData.setSurveyDate(surveyDate);
					} else {
						surveyData.setSurveyDate("");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						surveyData.setPlantingDate(plantDate);
					} else {
						surveyData.setPlantingDate("");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						surveyData.setFirstRustObservationDate(obserDate);
					} else {
						surveyData.setFirstRustObservationDate("");
					}

					surveyData.setContactedFarmer(response.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID) ? "Yes" : "No");
					surveyData.setRemarks(!response.getString(WrsisPortalConstant.REMARK).isEmpty() ? response.getString(WrsisPortalConstant.REMARK) : "");

					// Site Information
					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					

					surveyData.setSurveySite(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					surveyData.setWheatType(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					surveyData.setVariety(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					surveyData.setGrowthStage(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					surveyData.setArea(!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");
					surveyData.setRustAffected((response.getBoolean("rustAffectedId")) ? "Yes" : "No");

					// fetch all active Rust types Start
					JSONArray rustDetails = response.getJSONArray(WrsisPortalConstant.RUST_DETAILS2);

					List<RustTypeMasterEntity> rustTypeList = rustTypeRepository.fetchAllActiveRustType();
					JSONObject hMap = new JSONObject();
					for (RustTypeMasterEntity rustTypeEntity : rustTypeList) {

						for (int i = 0; i < rustDetails.length(); i++) {
							

							if (rustDetails.getJSONObject(i)
									.getInt("rustTypeId") == (rustTypeEntity.getIntRustTypeId())) {

								
								ArrayList<String> t = new ArrayList<>();
								t.add(rustDetails.getJSONObject(i).getString("rustTypeId"));
								t.add(!rustDetails.getJSONObject(i).getString(WrsisPortalConstant.INCIDENT_ID).isEmpty()
										? rustDetails.getJSONObject(i).getString(WrsisPortalConstant.INCIDENT_ID)
										: "");
								t.add(!rustDetails.getJSONObject(i).getString(WrsisPortalConstant.SEVERITY_ID).isEmpty()
										? rustDetails.getJSONObject(i).getString(WrsisPortalConstant.SEVERITY_ID)
										: "");
								t.add(!rustDetails.getJSONObject(i).getString(WrsisPortalConstant.REACTION_NAME).isEmpty()
										? rustDetails.getJSONObject(i).getString(WrsisPortalConstant.REACTION_NAME)
										: "");
								hMap.put(Integer.toString(rustTypeEntity.getIntRustTypeId()), t);

							}
						}

					}
					
					surveyData.setMapData(hMap);
					// fetch all active Rust types End

					// Plotting data from Sample Details Start

					JSONArray sampleDetails = response.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS);
					JSONObject sampleMap = new JSONObject();
					List<SampleTypeMaster> sampleList = sampleTypeRepository.fetchAllActiveSampletypes();
					for (SampleTypeMaster sample : sampleList) {
						for (int i = 0; i < sampleDetails.length(); i++) {
							if (sampleDetails.getJSONObject(i).getInt(WrsisPortalConstant.SAMPLE_TYPE_ID) == sample.getSampleId()) {
								ArrayList<String> t = new ArrayList<>();

								t.add(sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_TYPE_ID));

								t.add(!sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_TYPE_ID).isEmpty()
										? sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_COUNT_ID)
										: "");
								t.add(!sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_IDS).isEmpty()
										? sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_IDS)
										: "");
								t.add(!sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_REMARKS).isEmpty()
										? sampleDetails.getJSONObject(i).getString(WrsisPortalConstant.SAMPLE_REMARKS)
										: "");

								sampleMap.put(Integer.toString(sample.getSampleId()), t);
							}

						}
					}
					
					surveyData.setMapSampleDetails(sampleMap);

					// Plotting Data for Fungicide Details Start
					
					JSONObject jsonObject = response.getJSONObject(WrsisPortalConstant.FUNGICIDE_JSON);

					surveyData.setFungicideApplied((response.getBoolean(WrsisPortalConstant.FUNGISIDE_ID)) ? "Yes" : "No");

					surveyData.setFungicideName(
							!jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME).isEmpty() ? jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME)
									: " ");

					surveyData.setFungicideDose(
							!jsonObject.getString("dose").isEmpty() ? jsonObject.getString("dose") : " ");

					surveyData.setFungicideSprayDate(jsonObject.getString(WrsisPortalConstant.SPRAY_DATE));

					surveyData.setFungicideEffectiveNessName(!jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME).isEmpty()
							? jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME)
							: "");

					// Plotting Data for Fungicide Details End

					// Plotting Data for OtherDisease Start
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);

					JSONObject diseaseDetailsMap = new JSONObject();
					List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
					for (DiseaseMaster diseaseMaster : diseaseList) {
						for (int i = 0; i < diseaseDetails.length(); i++) {
							if (diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.DISEASE_TYPE_ID) == diseaseMaster
									.getDiseaseId()) {
								ArrayList<String> t = new ArrayList<>();

								t.add(diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.DISEASE_TYPE_ID));
								t.add(!diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.OTH_INCIDENT_VAL).isEmpty()
										? diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.OTH_INCIDENT_VAL)
										: "");
								t.add(!diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.OTH_SEVERITY_VAL).isEmpty()
										? diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.OTH_SEVERITY_VAL)
										: "");

								diseaseDetailsMap.put(Integer.toString(diseaseMaster.getDiseaseId()), t);
							}
						}
					}
					
					surveyData.setMapOtherDisease(diseaseDetailsMap);

					JSONObject jsonOtherDtl = response.getJSONObject("otherDetails");
					surveyData.setIrrigated((jsonOtherDtl.getBoolean(WrsisPortalConstant.IRRIGATED1)) ? "Yes" : "No");

					surveyData.setWeedControl(jsonOtherDtl.getBoolean(WrsisPortalConstant.WEED_CONTROL1) ? "Yes" : "No");

					surveyData.setSoilColor(
							!jsonOtherDtl.getString("soilColor").isEmpty() ? jsonOtherDtl.getString(WrsisPortalConstant.SOIL_NAME) : " ");

					surveyData.setMoisture(
							!jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME).isEmpty() ? jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME)
									: " ");
					// Plotting Data for Other Details End

					// Plotting Data for Any Other Disease start

					// Code for now

					

					surveyList.add(surveyData);
				} catch (Exception e) {
					continue;
				}

			}
		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::createGRTableSurvey():" + e);

		}
		return surveyList;
	}

	// plot Other Disease data in view page
	@Override
	public List<SuveyDetailsSampleReportDataBean> createRustSurveyOtherDiseaseTable(List<SurveyDetailsBean> bean) {

		List<SuveyDetailsSampleReportDataBean> surveyList = new ArrayList<>();

		try {
			SuveyDetailsSampleReportDataBean surveyData = null;
			for (SurveyDetailsBean survey : bean) {

				surveyData = new SuveyDetailsSampleReportDataBean();

				try {

					// Get All Data using survey Id for other disease
					
					JSONObject response = commonService.getFormmatedJsonBySurveyId(survey.getSurveyId());
					surveyData.setSurveyNo(
							!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					surveyData.setCountry(countryName);
					surveyData.setSurveyourName(str);
					surveyData.setRcName(rcName);

					surveyData.setSeason(response.getString(WrsisPortalConstant.SEASION_NAME));
					surveyData.setRegion(response.getString(WrsisPortalConstant.REGION_NAME1));
					surveyData.setZone(response.getString(WrsisPortalConstant.ZONE_NAME1));
					surveyData.setWoreda(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					surveyData.setKebele(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					surveyData.setAltitude(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					surveyData.setLongitude(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					surveyData.setLatitude(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);

					if (surveyDate != null && !surveyDate.isEmpty()) {
						surveyData.setSurveyDate(surveyDate);
					} else {
						surveyData.setSurveyDate("");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						surveyData.setPlantingDate(plantDate);
					} else {
						surveyData.setPlantingDate("");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						surveyData.setFirstRustObservationDate(obserDate);
					} else {
						surveyData.setFirstRustObservationDate("");
					}

					// Site Information

					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					surveyData.setSurveySite(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					surveyData.setWheatType(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					surveyData.setVariety(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					surveyData.setGrowthStage(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					surveyData.setArea(!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");

					// Plotting Data for OtherDisease Start
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					JSONObject diseaseDetailsMap = new JSONObject();
					List<DiseaseMaster> diseaseList = diseaseMasterRepository.fetchAllActiveDisease(true);
					for (DiseaseMaster diseaseMaster : diseaseList) {
						for (int i = 0; i < diseaseDetails.length(); i++) {
							
							if (diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.DISEASE_TYPE_ID) == diseaseMaster
									.getDiseaseId()) {
								ArrayList<String> t = new ArrayList<>();
								t.add(diseaseDetails.getJSONObject(i).getString(WrsisPortalConstant.DISEASE_TYPE_ID));

								t.add(!String.valueOf(diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.OTH_INCIDENT_VAL))
										.isEmpty()
												? String.valueOf(
														diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.OTH_INCIDENT_VAL))
												: "");
								try {

									t.add(!String.valueOf(diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.OTH_SEVERITY_VAL))
											.isEmpty()
													? String.valueOf(
															diseaseDetails.getJSONObject(i).getInt(WrsisPortalConstant.OTH_SEVERITY_VAL))
													: "");
								} catch (Exception e) {
									LOG.error("SurveyExcelDetailsServiceImpl::createRustSurveyOtherDiseaseTable():"
											+ e);

									diseaseDetailsMap.put(Integer.toString(diseaseMaster.getDiseaseId()), t);
									continue;
								}
								diseaseDetailsMap.put(Integer.toString(diseaseMaster.getDiseaseId()), t);

							}
						}
					}
					surveyData.setMapOtherDisease(diseaseDetailsMap);

					surveyList.add(surveyData);

				} catch (Exception e) {
					LOG.error("SurveyExcelDetailsServiceImpl::createRustSurveyOtherDiseaseTable():" + e);

					continue;
				}

			}
		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::createRustSurveyOtherDiseaseTable():" + e);

		}

		return surveyList;

	}

	@Override
	public HSSFWorkbook createExcelRustSurveyOtherDisease(HSSFWorkbook workbook, List<SurveyDetailsBean> bean) {

		HSSFWorkbook book = workbook;
		try {

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Sheet sheetSample = workbook.getSheet("Other Diseases");
			Row mainRow = sheetSample.getRow(0);
			int mainRowEnd = mainRow.getLastCellNum();

			sheetSample.addMergedRegion(new CellRangeAddress(1, 2, mainRowEnd, mainRowEnd));

			int rowNo = 3;
			int j = 1;
			for (SurveyDetailsBean survey : bean) {
				try {
					JSONObject response = commonService.getFormmatedJsonBySurveyIdForOtherDisease(survey.getSurveyId());

					Row row = sheetSample.createRow(rowNo++);
					row.createCell(0).setCellValue(j);
					j++;

					// Plotting Data for Surveyor's Name Start
					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					CellStyle sty = workbook.createCellStyle();
					sty.setWrapText(true);
					
					Cell cell1 = row.createCell(3);
					cell1.setCellStyle(sty);
					cell1.setCellValue(str);
					row.createCell(2).setCellValue(countryName);

					row.createCell(4).setCellValue(rcName);
					// Plotting Data for Research Center Name End

					cell1 = row.createCell(1);
					cell1.setCellStyle(sty);
					cell1.setCellValue(!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					row.createCell(5).setCellValue(response.getString(WrsisPortalConstant.SEASION_NAME));
					row.createCell(6).setCellValue(response.getString(WrsisPortalConstant.REGION_NAME1));
					row.createCell(7).setCellValue(response.getString(WrsisPortalConstant.ZONE_NAME1));
					row.createCell(8).setCellValue(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					row.createCell(9).setCellValue(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					row.createCell(10).setCellValue(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					row.createCell(11).setCellValue(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					row.createCell(12).setCellValue(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);
					if (surveyDate != null && !surveyDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(surveyDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(13).setCellValue(strDate);
					} else {
						row.createCell(13).setCellValue(" ");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(plantDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(14).setCellValue(strDate);
					} else {
						row.createCell(14).setCellValue(" ");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(obserDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(15).setCellValue(strDate);
					} else {
						row.createCell(15).setCellValue(" ");
					}

					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					row.createCell(16).setCellValue(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					row.createCell(17).setCellValue(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					row.createCell(18).setCellValue(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					row.createCell(19).setCellValue(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					row.createCell(20).setCellValue(
							!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");

					
					
				
					JSONObject locations = getAllFieldLocations(sheetSample.getRow(0));

					// Plotting Data for OtherDisease Start

					int diseaseStart = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(0);
					int diseaseEnd = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(1);
					int totalDisease = Math.round(((diseaseEnd - diseaseStart) / 2));
					ArrayList<String> diseasArr = new ArrayList<>();
					int disea_strat = diseaseStart;
					for (int m = 0; m < totalDisease; m++) {
						diseasArr.add(getCellData(sheetSample.getRow(1), disea_strat));
						disea_strat = disea_strat + 2;
					}
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					for (int m = 0; m < diseaseDetails.length(); m++) {
						JSONObject jsObject = diseaseDetails.getJSONObject(m);
						int index = 0;
						int c = 0;
						for (Iterator iterator = diseasArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jsObject.getString(WrsisPortalConstant.DISEASE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						disea_strat = diseaseStart + ((index) * 2);
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
						disea_strat++;
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					}

					// Plotting Data for OtherDisease End

				} catch (Exception e2) {
					LOG.error("SurveyExcelDetailsServiceImpl::createExcelRustSurveyOtherDisease():" + e2);
					continue;
				}
			}

		} catch (Exception e) {

			LOG.error("SurveyExcelDetailsServiceImpl::createExcelRustSurveyOtherDisease():" + e);
		}
		return book;

	}

	@Override
	public List<SuveyOtherDiseaseReportDataBean> createCsvRustSurveyOtherDiseaseTable(List<SurveyDetailsBean> bean) {
		
		return null;
	}

	@Override
	public HSSFWorkbook createCsvRustSurveyOtherDisease(HSSFWorkbook workbook, List<SurveyDetailsBean> bean) {

		HSSFWorkbook book = workbook;
		try {

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Sheet sheetSample = workbook.getSheet("Other Diseases");
			Row mainRow = sheetSample.getRow(0);
			int mainRowEnd = mainRow.getLastCellNum();

			sheetSample.addMergedRegion(new CellRangeAddress(1, 2, mainRowEnd, mainRowEnd));

			int rowNo = 3;
			int j = 1;
			for (SurveyDetailsBean survey : bean) {
				try {
					JSONObject response = commonService.getFormmatedJsonBySurveyIdForOtherDisease(survey.getSurveyId());

					Row row = sheetSample.createRow(rowNo++);
					row.createCell(0).setCellValue(j);
					j++;

					// Plotting Data for Surveyor's Name Start
					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					CellStyle sty = workbook.createCellStyle();
					sty.setWrapText(true);
					
					Cell cell1 = row.createCell(3);
					cell1.setCellStyle(sty);
					cell1.setCellValue(str);
					row.createCell(2).setCellValue(countryName);

					row.createCell(4).setCellValue(rcName);
					// Plotting Data for Research Center Name End

					cell1 = row.createCell(1);
					cell1.setCellStyle(sty);
					cell1.setCellValue(!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					row.createCell(5).setCellValue(response.getString(WrsisPortalConstant.SEASION_NAME));
					row.createCell(6).setCellValue(response.getString(WrsisPortalConstant.REGION_NAME1));
					row.createCell(7).setCellValue(response.getString(WrsisPortalConstant.ZONE_NAME1));
					row.createCell(8).setCellValue(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					row.createCell(9).setCellValue(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					row.createCell(10).setCellValue(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					row.createCell(11).setCellValue(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					row.createCell(12).setCellValue(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);
					if (surveyDate != null && !surveyDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(surveyDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(13).setCellValue(strDate);
					} else {
						row.createCell(13).setCellValue(" ");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(plantDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(14).setCellValue(strDate);
					} else {
						row.createCell(14).setCellValue(" ");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(obserDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(15).setCellValue(strDate);
					} else {
						row.createCell(15).setCellValue(" ");
					}

					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					row.createCell(16).setCellValue(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					row.createCell(17).setCellValue(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					row.createCell(18).setCellValue(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					row.createCell(19).setCellValue(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					row.createCell(20).setCellValue(
							!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");

				
			
					// Plotting Data for OtherDisease Start

					int diseaseStart = 21;
					int diseaseEnd = sheetSample.getRow(0).getLastCellNum();
			
					ArrayList<String> diseasArr = new ArrayList<>();
					int disea_strat = diseaseStart;
					for (int c = diseaseStart; c < diseaseEnd; c += 2) {
						String cellV = getCellData(sheetSample.getRow(0), c).split(":")[1].trim();
						diseasArr.add(cellV);
					}

					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					for (int m = 0; m < diseaseDetails.length(); m++) {
						JSONObject jsObject = diseaseDetails.getJSONObject(m);
						int index = 0;
						int c = 0;
						for (Iterator iterator = diseasArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jsObject.getString(WrsisPortalConstant.DISEASE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						disea_strat = diseaseStart + ((index) * 2);
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
						disea_strat++;
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					}

					// Plotting Data for OtherDisease End

				} catch (Exception e2) {
					LOG.error("SurveyExcelDetailsServiceImpl::createCsvRustSurveyOtherDisease():" + e2);

					continue;
				}
			}

		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::createCsvRustSurveyOtherDisease():" + e);

		}
		return book;

	}

	@Override
	public HSSFWorkbook createCsvGlobalRustSurvey(HSSFWorkbook workbook, List<SurveyDetailsBean> bean) {
		HSSFWorkbook book = workbook;
		try {

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workbook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Sheet sheetSample = workbook.getSheet(WrsisPortalConstant.SURVEY_DATA);
			Row mainRow = sheetSample.getRow(0);
			int mainRowEnd = mainRow.getLastCellNum();

			sheetSample.addMergedRegion(new CellRangeAddress(1, 2, mainRowEnd, mainRowEnd));

			int rowNo = 4;
			int j = 1;
			for (SurveyDetailsBean survey : bean) {
				try {
					JSONObject response = commonService.getFormmatedJsonBySurveyId(survey.getSurveyId());

					Row row = sheetSample.createRow(rowNo++);
					row.createCell(0).setCellValue(j);
					j++;

					// Plotting Data for Surveyor's Name Start
					JSONArray survrNameArr = response.getJSONArray(WrsisPortalConstant.SURVEYOR_JSA);
					String[] surveyorList = new String[survrNameArr.length()];
					String rcName = null;
					String countryName = null;
					for (int p = 0; p < survrNameArr.length(); p++) {
						surveyorList[p] = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.SURVEYOR_NAME1);
						rcName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.INSTITUTION_NAME1);
						countryName = survrNameArr.getJSONObject(p).getString(WrsisPortalConstant.COUNTRY_NAME);
					}
					StringBuilder sm = new StringBuilder();
					for (String string : surveyorList) {
						sm.append(string).append(",");
					}
					String str = "";
					if (sm.length() > 0) {
						str = sm.substring(0, sm.length() - 1);
					}
					CellStyle sty = workbook.createCellStyle();
					sty.setWrapText(true);
					
					Cell cell1 = row.createCell(3);
					cell1.setCellStyle(sty);
					cell1.setCellValue(str);
					row.createCell(2).setCellValue(countryName);

					row.createCell(4).setCellValue(rcName);
					// Plotting Data for Research Center Name End

					cell1 = row.createCell(1);
					cell1.setCellStyle(sty);
					cell1.setCellValue(!response.getString(WrsisPortalConstant.SURVEY_NO).isEmpty() ? response.getString(WrsisPortalConstant.SURVEY_NO) : "");

					row.createCell(5).setCellValue(response.getString(WrsisPortalConstant.SEASION_NAME));
					row.createCell(6).setCellValue(response.getString(WrsisPortalConstant.REGION_NAME1));
					row.createCell(7).setCellValue(response.getString(WrsisPortalConstant.ZONE_NAME1));
					row.createCell(8).setCellValue(response.getString(WrsisPortalConstant.WOREDA_NAME1));
					row.createCell(9).setCellValue(response.getString(WrsisPortalConstant.KEBELE_NAME1));
					row.createCell(10).setCellValue(response.getString(WrsisPortalConstant.ALTITUDE_ID));
					row.createCell(11).setCellValue(response.getString(WrsisPortalConstant.LONGITUDE_ID));
					row.createCell(12).setCellValue(response.getString(WrsisPortalConstant.LATITUDE_ID));

					String surveyDate = response.getString(WrsisPortalConstant.SURVEY_DATE_ID);
					if (surveyDate != null && !surveyDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(surveyDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(13).setCellValue(strDate);
					} else {
						row.createCell(13).setCellValue(" ");
					}

					String plantDate = response.getString(WrsisPortalConstant.PLANTING_DATE);
					if (plantDate != null && !plantDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(plantDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(14).setCellValue(strDate);
					} else {
						row.createCell(14).setCellValue(" ");
					}

					String obserDate = response.getString(WrsisPortalConstant.OBSERVATION_ID);
					if (obserDate != null && !obserDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(obserDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(15).setCellValue(strDate);
					} else {
						row.createCell(15).setCellValue(" ");
					}

					row.createCell(16).setCellValue((response.getBoolean(WrsisPortalConstant.CONTACTED_FARMER_ID)) ? "Yes" : "No");
					row.createCell(17)
							.setCellValue(!response.getString(WrsisPortalConstant.REMARK).isEmpty() ? response.getString(WrsisPortalConstant.REMARK) : "");

					JSONObject siteInfo = new JSONObject(response.getString(WrsisPortalConstant.SITE_INFORMATION1));
					row.createCell(18).setCellValue(siteInfo.getString(WrsisPortalConstant.SURVEY_SITE_NAME));
					row.createCell(19).setCellValue(siteInfo.getString(WrsisPortalConstant.WHEAT_TYPE_NAME));
					row.createCell(20).setCellValue(siteInfo.getString(WrsisPortalConstant.VARITY_NAME1));
					row.createCell(21).setCellValue(siteInfo.getString(WrsisPortalConstant.GROWTH_STAGE_NAME));
					row.createCell(22).setCellValue(
							!siteInfo.getString(WrsisPortalConstant.SITE_AREA).isEmpty() ? siteInfo.getString(WrsisPortalConstant.SITE_AREA) : "");

					
					
					int cellNo = 22;
					JSONObject locations = getAllFieldLocations(sheetSample.getRow(0));
					int rustStart = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(0);
					int rustEnd = locations.getJSONArray(WrsisPortalConstant.RUST_DETAILS1).getInt(1);
					int toalRusts = Math.round(((rustEnd - rustStart) / 3));
					ArrayList<String> rustArr = new ArrayList<>();
					List<RustTypeMasterEntity> rusrtList = rustTypeRepository.fetchAllActiveRustType();
					int start = rustStart;
					for (int l = 0; l < toalRusts; l++) {
						
						rustArr.add(rusrtList.get(l).getVchRustType());
						start = start + 3;
					}
					JSONArray rustDetails = response.getJSONArray(WrsisPortalConstant.RUST_DETAILS2);
					for (int l = 0; l < rustDetails.length(); l++) {
						JSONObject json = rustDetails.getJSONObject(l);
						int index = 0;
						int c = 0;
						for (Iterator iterator = rustArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(json.getString(WrsisPortalConstant.RUST_TYPE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;

						}

						
						start = rustStart + ((index) * 3);
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.INCIDENT_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.SEVERITY_ID)
										: "");
						start++;
						row.createCell(start)
								.setCellValue(!rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME).isEmpty()
										? rustDetails.getJSONObject(l).getString(WrsisPortalConstant.REACTION_NAME)
										: "");
					}

					cellNo = rustEnd;
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.SAMPLE_COLLECTED_ID)) ? "Yes" : "No");

					// Plotting data from Sample Details Start
					int sampleStar = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(0);
					int sampleEnd = locations.getJSONArray(WrsisPortalConstant.SAMPLE_DETAILS1).getInt(1);
					int totalSample = Math.round(((sampleEnd - sampleStar) / 3));

					ArrayList<String> sampleArr = new ArrayList<>();
					int smp_start = sampleStar;
					for (int k = 0; k < totalSample; k++) {
						sampleArr.add(getCellData(sheetSample.getRow(1), smp_start + 1));
						smp_start = smp_start + 3;
					}
					JSONArray sampleDetails = response.getJSONArray("sampleDetails");
					for (int k = 0; k < sampleDetails.length(); k++) {
						JSONObject jObject = sampleDetails.getJSONObject(k);
						int index = 0;
						int c = 0;
						for (Iterator iterator = sampleArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jObject.getString(WrsisPortalConstant.SAMPLE_TYPE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						smp_start = sampleStar + ((index) * 3) + 1;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_COUNT_ID));
						smp_start++;
						row.createCell(smp_start).setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_IDS));
						smp_start++;
						row.createCell(smp_start)
								.setCellValue(sampleDetails.getJSONObject(k).getString(WrsisPortalConstant.SAMPLE_REMARKS));
					}
					// Plotting data for Sample Details End

					// Plotting Data for Fungicide Details Start
					cellNo = sampleEnd;
					JSONObject jsonObject = response.getJSONObject(WrsisPortalConstant.FUNGICIDE_JSON);
					row.createCell(cellNo).setCellValue((response.getBoolean(WrsisPortalConstant.FUNGISIDE_ID)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.FUNGICIDE_NAME)
									: " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString("dose").isEmpty() ? jsonObject.getString("dose") : " ");
					cellNo++;

					String spryDate = jsonObject.getString(WrsisPortalConstant.SPRAY_DATE);

					if (spryDate != null && !spryDate.isEmpty()) {
						Date date = new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).parse(spryDate);
						DateFormat dateFormat = new SimpleDateFormat(WrsisPortalConstant.DATEFORMAT_DDMMYYYY);
						String strDate = dateFormat.format(date);
						row.createCell(cellNo).setCellValue(strDate);
					} else {
						row.createCell(cellNo).setCellValue(" ");
					}

					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME).isEmpty()
									? jsonObject.getString(WrsisPortalConstant.EFFECTIVENESS_NAME)
									: "");

					// Plotting Data for Fungicide Details End

					// Plotting Data for OtherDisease Start

					int diseaseStart = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(0);
					int diseaseEnd = locations.getJSONArray(WrsisPortalConstant.OTHER_DSSE).getInt(1);
					int totalDisease = Math.round(((diseaseEnd - diseaseStart) / 2));
					ArrayList<String> diseasArr = new ArrayList<>();
					int disea_strat = diseaseStart;
					for (int m = 0; m < totalDisease; m++) {
						diseasArr.add(getCellData(sheetSample.getRow(1), disea_strat));
						disea_strat = disea_strat + 2;
					}
					JSONArray diseaseDetails = response.getJSONArray(WrsisPortalConstant.OTHER_DISEASE1);
					for (int m = 0; m < diseaseDetails.length(); m++) {
						JSONObject jsObject = diseaseDetails.getJSONObject(m);
						int index = 0;
						int c = 0;
						for (Iterator iterator = diseasArr.iterator(); iterator.hasNext();) {
							String string = (String) iterator.next();
							if (string.toLowerCase().contains(jsObject.getString(WrsisPortalConstant.DISEASE_NAME).toLowerCase())) {
								index = c;
								break;
							}
							c++;
						}
						disea_strat = diseaseStart + ((index) * 2);
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_INCIDENT_VAL));
						disea_strat++;
						row.createCell(disea_strat)
								.setCellValue(diseaseDetails.getJSONObject(m).getString(WrsisPortalConstant.OTH_SEVERITY_VAL));
					}
					// Plotting Data for OtherDisease End

					cellNo = diseaseEnd;
					// Plotting Data for Other Details Start
					JSONObject jsonOtherDtl = response.getJSONObject("otherDetails");
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.IRRIGATED1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue((jsonOtherDtl.getBoolean(WrsisPortalConstant.WEED_CONTROL1)) ? "Yes" : "No");
					cellNo++;
					row.createCell(cellNo).setCellValue(
							!jsonOtherDtl.getString(WrsisPortalConstant.SOIL_COLOR1).isEmpty() ? jsonOtherDtl.getString(WrsisPortalConstant.SOIL_NAME) : " ");
					cellNo++;
					row.createCell(cellNo)
							.setCellValue(!jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME).isEmpty()
									? jsonOtherDtl.getString(WrsisPortalConstant.MOISTURE_NAME)
									: " ");
					// Plotting Data for Other Details End

					// Plotting Data for Any Other Disease start
					
					// Plotting Data for Any Other Disease End

				} catch (Exception e2) {
					LOG.error("SurveyExcelDetailsServiceImpl::createCsvGlobalRustSurvey():" + e2);

					continue;
				}
			}

		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::createCsvGlobalRustSurvey():" + e);

		}
		return book;
	}

	@Autowired
	SurveyExcelFilesRepository surveyExcelFilesRepository;

	@Override
	public List<SurveyExcelFilesBean> viewAllSurveyFileDetails(Integer userId, String startDate, String endDate,
			Integer start, Integer length) {
		List<SurveyExcelFilesBean> list = new ArrayList<>();
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
			int count = 0;
			List<Object[]> obList = surveyExcelFilesRepository.viewAllActiveExcelFile(userId, sDate, eDate, start,
					length);
			for (Object[] ob : obList) {
				SurveyExcelFilesBean s = new SurveyExcelFilesBean();
				s.setSlNo(start + (++count));
				s.setFileSurveyId(Integer.parseInt((String) ob[0]));
				String fName = (String) ob[1];
				File f = new File(WrsisPortalConstant.wrsisPropertiesFileConstants.WR_SURVEY_EXCEL_PATH + File.separator
						+ new String(Base64.getDecoder().decode(fName.getBytes())));
				s.setStatus(f.exists());
				String decName = new String(Base64.getDecoder().decode(fName.getBytes()));
				s.setFileDecodeName((decName.contains(File.separator))
						? decName.substring(decName.lastIndexOf(File.separator) + 1, decName.length())
						: decName);

				if (!s.getStatus()) {
					s.setFileDownloadLink("<a title=\"\"\r\n"
							+ "												href=\"javascript:void(0)\" id=\"downloadIcon\"\r\n"
							+ "												data-toggle=\"tooltip\" data-placement=\"top\"\r\n"
							+ "												data-original-title=\"Download\"\r\n"
							+ "												onclick=\"downloadFile("
							+ s.getFileSurveyId() + ")\">" + s.getFileDecodeName() + "</a>");
				} else if (s.getStatus()) {
					s.setFileDownloadLink("<a href='public/downloadExcel?path=" + fName
							+ "' download='Survey_Uploaded_Success_Data'>" + s.getFileDecodeName() + "</a>");
				}
				s.setCreatedOn((String) ob[4]);
				s.setCount(Integer.parseInt((String) ob[2]));
				s.setCountViewLink("<form action=\"showViewByExcelFileId\" method=\"POST\" id=\"post" + s.getSlNo()
						+ "\">\r\n" + "           \r\n"
						+ "<input type=\"hidden\" name=\"csrf_token_\" value=\"<c:out value='${csrf_token_}'/>\"/>\r\n"
						+ "\r\n" + "            <input type=\"text\" style=\"display:none;\" value=\""
						+ s.getFileSurveyId() + "\" name=\"path\">\r\n" + "            </form>\r\n"
						+ "      <center>    <a href=\"javascript:void(0)\" onclick=\"document.getElementById('post"
						+ s.getSlNo() + "').submit();\">" + s.getCount() + "</a>	</center>");
				list.add(s);
			}

		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::viewAllSurveyFileDetails():" + e);

		}
		return list;
	}

	@Override
	public Integer viewAllSurveyFileDetailsCount(Integer userId, String startDate, String endDate, Integer start,
			Integer length) {
		Integer count = 0;
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
			count = surveyExcelFilesRepository.viewAllActiveExcelFileCount(userId, sDate, eDate, start, length);
		} catch (Exception e) {
			LOG.error("SurveyExcelDetailsServiceImpl::viewAllSurveyfileDetailsCount():" + e);

		}
		return count;
	}

}
