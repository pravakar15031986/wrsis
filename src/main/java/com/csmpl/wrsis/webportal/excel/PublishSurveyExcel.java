package com.csmpl.wrsis.webportal.excel;

import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.csmpl.wrsis.webportal.bean.SurveyDetailsBean;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.csmpl.wrsis.webportal.entity.Question;

public class PublishSurveyExcel {

	public static final Logger LOG = LoggerFactory.getLogger(PublishSurveyExcel.class);

	/* Excel sheet for pending survey */

	public HSSFWorkbook buildPendingSurveyExcel(List<SurveyDetailsBean> beansps) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("Survey Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
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

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Survey No");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SURVEY_DATE);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.INSTITUTION_NAME);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.TYPE_OF_RUST);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.MODE_OF_DATA_COLLECTION);

		for (int i = 0; i < beansps.size(); i++) {
			SurveyDetailsBean bean1 = beansps.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean1.getSurveyNo());

			cell = row.createCell(2);
			cell.setCellValue(bean1.getSurveyDate());

			cell = row.createCell(3);
			cell.setCellValue(bean1.getRegion());

			cell = row.createCell(4);
			cell.setCellValue(bean1.getZone());

			cell = row.createCell(5);
			cell.setCellValue(bean1.getWoreda());

			cell = row.createCell(6);
			cell.setCellValue(bean1.getKebele());

			cell = row.createCell(7);
			cell.setCellValue(bean1.getInstitutionName());

			cell = row.createCell(8);
			cell.setCellValue((bean1.getRust() == null) ? "" : bean1.getRust());

			cell = row.createCell(9);
			cell.setCellValue((bean1.getMode() == null) ? "" : bean1.getMode());

		}

		return workbook;

	}

	/* Excel sheet for published survey */

	public HSSFWorkbook buildPublishedSurveyExcel(List<SurveyDetailsBean> beanspus) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Published Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(9, 8000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Survey No");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SURVEY_DATE);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.PUBLISED_DATE);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.INSTITUTION_NAME);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.TYPE_OF_RUST);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.MODE_OF_DATA_COLLECTION);

		for (int i = 0; i < beanspus.size(); i++) {
			SurveyDetailsBean bean2 = beanspus.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean2.getSurveyNo());

			cell = row.createCell(2);
			cell.setCellValue(bean2.getSurveyDate());

			cell = row.createCell(3);
			cell.setCellValue(bean2.getPublishedDate());

			cell = row.createCell(4);
			cell.setCellValue(bean2.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean2.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean2.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(bean2.getKebele());

			cell = row.createCell(8);
			cell.setCellValue(bean2.getInstitutionName());

			cell = row.createCell(9);
			cell.setCellValue((bean2.getRust() == null) ? "" : bean2.getRust());

			cell = row.createCell(10);
			cell.setCellValue((bean2.getMode() == null) ? "" : bean2.getMode());

		}

		return workbookps;

	}

	/* Excel sheet for Discarded survey */

	public HSSFWorkbook buildDiscardedSurveyExcel(List<SurveyDetailsBean> beansds) {

		HSSFWorkbook workbookds = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookds.createSheet("Discarded Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 8000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);

		// Create a Font for styling header cells
		Font headerFont = workbookds.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookds.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookds.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookds.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Survey No");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SURVEY_DATE);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Discarded Date");

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.INSTITUTION_NAME);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.TYPE_OF_RUST);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.MODE_OF_DATA_COLLECTION);

		columnCell = columnRow.createCell(11);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Reason ");

		for (int i = 0; i < beansds.size(); i++) {
			SurveyDetailsBean bean3 = beansds.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean3.getSurveyNo());

			cell = row.createCell(2);
			cell.setCellValue(bean3.getSurveyDate());

			cell = row.createCell(3);
			cell.setCellValue(bean3.getPublishedDate());

			cell = row.createCell(4);
			cell.setCellValue(bean3.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean3.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean3.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(bean3.getKebele());

			cell = row.createCell(8);
			cell.setCellValue(bean3.getInstitutionName());

			cell = row.createCell(9);
			cell.setCellValue((bean3.getRust() == null) ? "" : bean3.getRust());

			cell = row.createCell(10);
			cell.setCellValue((bean3.getMode() == null) ? "" : bean3.getMode());

			cell = row.createCell(11);
			cell.setCellValue((bean3.getReason() == null) ? "" : bean3.getReason());

		}

		return workbookds;

	}

	public HSSFWorkbook rustPublishedSurveyExcel(List<SurveyDetailsBean> beanspus) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Survey Publish Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 8000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Survey No");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SURVEY_DATE);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.PUBLISED_DATE);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.INSTITUTION_NAME);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.TYPE_OF_RUST);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.MODE_OF_DATA_COLLECTION);

		for (int i = 0; i < beanspus.size(); i++) {
			SurveyDetailsBean bean2 = beanspus.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean2.getSurveyNo());

			cell = row.createCell(2);
			cell.setCellValue(bean2.getSurveyDate());

			cell = row.createCell(3);
			cell.setCellValue(bean2.getPublishedDate());

			cell = row.createCell(4);
			cell.setCellValue(bean2.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean2.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean2.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(bean2.getKebele());

			cell = row.createCell(8);
			cell.setCellValue(bean2.getInstitutionName());

			cell = row.createCell(9);
			cell.setCellValue((bean2.getRust() == null) ? "" : bean2.getRust());

			cell = row.createCell(10);
			cell.setCellValue((bean2.getMode() == null) ? "" : bean2.getMode());

		}

		return workbookps;

	}

	/* Excel sheet for Dashboard RustIncident */
	public HSSFWorkbook buildDashBoardRustIncidentExcel(List<RustIncidentEntityBean> beanspus) throws JSONException {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Rust Incident");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 6000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Incident Date");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Year");

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SEASON_C);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.LONGITUDE);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.LATITUDE);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Incident Submitted By");

		List<String> questionJsa = new ArrayList<>();
		for (int i = 0; i < beanspus.size(); i++) {
			RustIncidentEntityBean rbean = beanspus.get(i);
			org.json.JSONArray jsa = null;
			try {
				jsa = new org.json.JSONArray(rbean.getQueostions());
			} catch (Exception e) {
				LOG.error("PublishSurveyExcel::buildDashBoardRustIncidentExcel():" + e.getMessage());
				continue;
			}
			for (int j = 0; j < jsa.length(); j++) {
				if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

					questionJsa.add(jsa.getJSONObject(j).getString("question"));
				}
			}
		}

		int curtCell = 11;
		for (int i = 0; i < questionJsa.size(); i++) {
			sheet.setColumnWidth(curtCell, 4000);
			columnCell = columnRow.createCell(curtCell);
			columnCell.setCellStyle(headerCellStyle);
			int j = i + 1;
			columnCell.setCellValue("Question" + j);
			curtCell++;
		}

		for (int i = 0; i < beanspus.size(); i++) {
			RustIncidentEntityBean bean2 = beanspus.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean2.getIncidendDate());

			cell = row.createCell(2);
			cell.setCellValue(bean2.getRegionName());

			cell = row.createCell(3);
			cell.setCellValue(bean2.getZoneName());

			cell = row.createCell(4);
			cell.setCellValue(bean2.getWoredaName());

			cell = row.createCell(5);
			cell.setCellValue(bean2.getKebeleName());

			cell = row.createCell(6);
			cell.setCellValue(bean2.getYear());

			cell = row.createCell(7);
			cell.setCellValue(bean2.getSeasonName());

			cell = row.createCell(8);
			cell.setCellValue(bean2.getLongitude());

			cell = row.createCell(9);
			cell.setCellValue(bean2.getLatitude());

			cell = row.createCell(10);
			cell.setCellValue(bean2.getUserFullName());

			int runningCell = 11;
			try {

				for (int m = 0; m < bean2.getQuestionsData().size(); m++) {

					cell = row.createCell(runningCell);
					cell.setCellValue(bean2.getQuestionsData().get(m));
					runningCell++;
				}
			} catch (Exception e) {
				LOG.error("PublishSurveyExcel::buildDashBoardRustIncidentExcel():" + e.getMessage());
				for (int ti = 0; ti < questionJsa.size(); ti++) {
					cell = row.createCell(runningCell);
					cell.setCellValue("");
					runningCell++;
				}

			}
		}
		return workbookps;

	}

	public HSSFWorkbook rustPublishedSurveyExcelByRCTagging(List<SurveyDetailsBean> beanspus) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Survey Publish Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 5000);
		sheet.setColumnWidth(10, 8000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Survey No");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SURVEY_DATE);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.PUBLISED_DATE);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.INSTITUTION_NAME);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.TYPE_OF_RUST);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.MODE_OF_DATA_COLLECTION);

		for (int i = 0; i < beanspus.size(); i++) {
			SurveyDetailsBean bean2 = beanspus.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean2.getSurveyNo());

			cell = row.createCell(2);
			cell.setCellValue(bean2.getSurveyDate());

			cell = row.createCell(3);
			cell.setCellValue(bean2.getPublishedDate());

			cell = row.createCell(4);
			cell.setCellValue(bean2.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean2.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean2.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(bean2.getKebele());

			cell = row.createCell(8);
			cell.setCellValue(bean2.getInstitutionName());

			cell = row.createCell(9);
			cell.setCellValue((bean2.getRust() == null) ? "" : bean2.getRust());

			cell = row.createCell(10);
			cell.setCellValue((bean2.getMode() == null) ? "" : bean2.getMode());

		}
		return workbookps;
	}

	/* Excel sheet for IVR Data */

	public HSSFWorkbook buildIVRSurveyExcel(List<IvrDataReportBean> beansps, List<Question> qsList) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("IVR Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
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

		CellStyle textCellStyle = workbook.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Uploaded Date");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Phone No.");

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Call Date Time");

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		int runnginColumnCell = 7;
		for (int i = 0; i < qsList.size(); i++) {
			sheet.setColumnWidth(runnginColumnCell, 4000);

			columnCell = columnRow.createCell(runnginColumnCell);
			columnCell.setCellStyle(headerCellStyle);
			int j = i + 1;
			columnCell.setCellValue("Question " + j);
			runnginColumnCell++;
		}

		for (int i = 0; i < beansps.size(); i++) {
			IvrDataReportBean bean1 = beansps.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean1.getUploadedDateView());

			cell = row.createCell(2);
			cell.setCellValue(bean1.getPhnNo());

			cell = row.createCell(3);
			cell.setCellValue(bean1.getCallDateView());

			cell = row.createCell(4);
			cell.setCellValue(bean1.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean1.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean1.getWoreda());

			int currentCell = 7;
			for (int j = 0; j < bean1.getIvrQuestionsData().size(); j++) {
				cell = row.createCell(currentCell);
				cell.setCellValue(bean1.getIvrQuestionsData().get(j));	
				
				currentCell++;
			}

		}

		return workbook;

	}

	/* Excel sheet for IVR */

	/* Excel sheet for View RustIncident */
	public HSSFWorkbook generateRustIncidentExcel(List<RustIncidentEntityBean> beanspus) throws JSONException {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Rust Incident");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 6000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Incident Date");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.KEBELE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Year");

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.SEASON_C);

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.LONGITUDE);

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.LATITUDE);

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Incident Submitted By");

		List<String> questionJsa = new ArrayList<>();
		for (int i = 0; i < beanspus.size(); i++) {
			RustIncidentEntityBean rbean = beanspus.get(i);
			org.json.JSONArray jsa = null;
			try {
				jsa = new org.json.JSONArray(rbean.getQueostions());
			} catch (Exception e) {
				LOG.error("PublishSurveyExcel::generateRustIncidentExcel():" + e.getMessage());
				continue;
			}
			for (int j = 0; j < jsa.length(); j++) {
				if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

					questionJsa.add(jsa.getJSONObject(j).getString("question"));
				}
			}
		}

		int curtCell = 11;
		for (int i = 0; i < questionJsa.size(); i++) {
			sheet.setColumnWidth(curtCell, 4000);
			columnCell = columnRow.createCell(curtCell);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue(questionJsa.get(i));
			curtCell++;
		}

		for (int i = 0; i < beanspus.size(); i++) {
			RustIncidentEntityBean bean2 = beanspus.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean2.getIncidendDate());

			cell = row.createCell(2);
			cell.setCellValue(bean2.getRegionName());

			cell = row.createCell(3);
			cell.setCellValue(bean2.getZoneName());

			cell = row.createCell(4);
			cell.setCellValue(bean2.getWoredaName());

			cell = row.createCell(5);
			cell.setCellValue(bean2.getKebeleName());

			cell = row.createCell(6);
			cell.setCellValue(bean2.getYear());

			cell = row.createCell(7);
			cell.setCellValue(bean2.getSeasonName());

			cell = row.createCell(8);
			cell.setCellValue(bean2.getLongitude());

			cell = row.createCell(9);
			cell.setCellValue(bean2.getLatitude());

			cell = row.createCell(10);
			cell.setCellValue(bean2.getUserFullName());

			int runningCell = 11;
			try {

				for (int m = 0; m < bean2.getQuestionsData().size(); m++) {

					cell = row.createCell(runningCell);
					cell.setCellValue(bean2.getQuestionsData().get(m));
					runningCell++;
				}
			} catch (Exception e) {
				LOG.error("PublishSurveyExcel::generateRustIncidentExcel():" + e.getMessage());
				for (int ti = 0; ti < questionJsa.size(); ti++) {
					cell = row.createCell(runningCell);
					cell.setCellValue("");
					runningCell++;
				}

			}
		}
		return workbookps;

	}

	/*------------Excel sheel for sucessweblogin details--------*/

	public HSSFWorkbook sucessWebloginExcel(List<IPTrackBean> bean) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Sucess Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 5000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Organization Name");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("User ID");

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("User Name");

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Designation");

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Role");

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("R.C. Name");

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("IP Address");

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Login Time");

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Status ");

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Logout Time");

		for (int i = 0; i < bean.size(); i++) {
			IPTrackBean bean3 = bean.get(i);

			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean3.getOrganizationName());

			cell = row.createCell(2);
			cell.setCellValue(bean3.getUserId());

			cell = row.createCell(3);
			cell.setCellValue(bean3.getUserName());

			cell = row.createCell(4);
			cell.setCellValue(bean3.getDesignation());

			cell = row.createCell(5);
			cell.setCellValue(bean3.getRole());

			cell = row.createCell(6);
			cell.setCellValue(bean3.getResearchcenterName());

			cell = row.createCell(7);
			cell.setCellValue(bean3.getIpAddress());

			cell = row.createCell(8);
			cell.setCellValue(bean3.getLoginTime());

			cell = row.createCell(9);
			cell.setCellValue(bean3.getStatus());

			cell = row.createCell(10);
			cell.setCellValue(bean3.getLogoutTime());

		}

		return workbookps;

	}

	/*------------Excel sheel for Recomendation Survey details--------*/

	public HSSFWorkbook recomendationSurveyExcel(List<SurveyImplementationBean> bean) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Recomendation Survey Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 9000);
		sheet.setColumnWidth(13, 9000);
		sheet.setColumnWidth(14, 9000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Monitor Ref No.");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Monitor Date");

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Recommend No.");

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("No of PAs Affected");

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Infected(ha)");

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Controlled(ha) ");

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Controlled(%)");

		columnCell = columnRow.createCell(11);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Fungicides Used (kg(lit))");

		columnCell = columnRow.createCell(12);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Male)");

		columnCell = columnRow.createCell(13);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Female)");

		columnCell = columnRow.createCell(14);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Total)");

		for (int i = 0; i < bean.size(); i++) {
			SurveyImplementationBean bean4 = bean.get(i);

			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean4.getMonitorno());

			cell = row.createCell(2);
			cell.setCellValue(bean4.getMonitordate());

			cell = row.createCell(3);
			cell.setCellValue(bean4.getRecomno());

			cell = row.createCell(4);
			cell.setCellValue(bean4.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(bean4.getZone());

			cell = row.createCell(6);
			cell.setCellValue(bean4.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(bean4.getNopaeffected());

			cell = row.createCell(8);
			cell.setCellValue(bean4.getTotalAreaInfested());

			cell = row.createCell(9);
			cell.setCellValue(bean4.getTotalControlledha());

			cell = row.createCell(10);
			cell.setCellValue(bean4.getTotalControlledpercent());

			cell = row.createCell(11);
			cell.setCellValue(bean4.getTotalFungicidesUsed());

			cell = row.createCell(12);
			cell.setCellValue(bean4.getMalefarmer());

			cell = row.createCell(13);
			cell.setCellValue(bean4.getFemalefarmer());

			cell = row.createCell(14);
			cell.setCellValue(bean4.getTotalfarmer());

		}

		return workbookps;

	}

	/*------------Excel sheel for Recomendation Survey details--------*/

	public HSSFWorkbook recomendationPublishedSurveyExcel(List<SurveyImplementationBean> bean) {

		HSSFWorkbook workbookps = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbookps.createSheet("Recomendation Survey Data");

		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 8000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(9, 8000);
		sheet.setColumnWidth(10, 5000);
		sheet.setColumnWidth(11, 5000);
		sheet.setColumnWidth(12, 9000);
		sheet.setColumnWidth(13, 9000);
		sheet.setColumnWidth(14, 9000);

		// Create a Font for styling header cells
		Font headerFont = workbookps.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 11);
		headerFont.setColor(IndexedColors.BLACK.getIndex());

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbookps.createCellStyle();
		headerCellStyle.setFont(headerFont);
		DataFormat fmt = workbookps.createDataFormat();
		headerCellStyle.setDataFormat(fmt.getFormat("@"));
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

		headerCellStyle.setBorderBottom(BorderStyle.THICK);
		headerCellStyle.setBorderTop(BorderStyle.THICK);
		headerCellStyle.setBorderRight(BorderStyle.THICK);
		headerCellStyle.setBorderLeft(BorderStyle.THICK);
		headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

		CellStyle textCellStyle = workbookps.createCellStyle();
		textCellStyle.setDataFormat(fmt.getFormat("@"));

		Row columnRow = sheet.createRow(0);
		Cell columnCell = columnRow.createCell(0);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Sl No");

		columnCell = columnRow.createCell(1);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Monitor Ref No.");

		columnCell = columnRow.createCell(2);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Monitor Date");

		columnCell = columnRow.createCell(3);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Recommendation No.");

		columnCell = columnRow.createCell(4);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.REGION);

		columnCell = columnRow.createCell(5);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.ZONE);

		columnCell = columnRow.createCell(6);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue(WrsisPortalConstant.WOREDA);

		columnCell = columnRow.createCell(7);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("No of PAs Affected");

		columnCell = columnRow.createCell(8);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Infected(ha)");

		columnCell = columnRow.createCell(9);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Controlled(ha) ");

		columnCell = columnRow.createCell(10);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Area Controlled(%)");

		columnCell = columnRow.createCell(11);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Total Fungicides Used (kg(lit))");

		columnCell = columnRow.createCell(12);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Male)");

		columnCell = columnRow.createCell(13);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Female)");

		columnCell = columnRow.createCell(14);
		columnCell.setCellStyle(headerCellStyle);
		columnCell.setCellValue("Numbers of Farmers Participated on Spraying (Total)");

		for (int i = 0; i < bean.size(); i++) {
			SurveyImplementationBean beanD = bean.get(i);

			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(beanD.getMonitorno());

			cell = row.createCell(2);
			cell.setCellValue(beanD.getMonitordate());

			cell = row.createCell(3);
			cell.setCellValue(beanD.getRecomno());

			cell = row.createCell(4);
			cell.setCellValue(beanD.getRegion());

			cell = row.createCell(5);
			cell.setCellValue(beanD.getZone());

			cell = row.createCell(6);
			cell.setCellValue(beanD.getWoreda());

			cell = row.createCell(7);
			cell.setCellValue(beanD.getNopaeffected());

			cell = row.createCell(8);
			cell.setCellValue(beanD.getTotalAreaInfested());

			cell = row.createCell(9);
			cell.setCellValue(beanD.getTotalControlledha());

			cell = row.createCell(10);
			cell.setCellValue(beanD.getTotalControlledpercent());

			cell = row.createCell(11);
			cell.setCellValue(beanD.getTotalFungicidesUsed());

			cell = row.createCell(12);
			cell.setCellValue(beanD.getMalefarmer());

			cell = row.createCell(13);
			cell.setCellValue(beanD.getFemalefarmer());

			cell = row.createCell(14);
			cell.setCellValue(beanD.getTotalfarmer());

		}

		return workbookps;

	}
}
