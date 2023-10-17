package com.csmpl.wrsis.webportal.excel;

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
import org.apache.poi.ss.util.CellRangeAddress;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.bean.UserBean;

public class ReportExcel {

	public HSSFWorkbook buildReportExcel(List<SampleTagBean> beans) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("Report");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 6000);
		sheet.setColumnWidth(11, 6000);
		sheet.setColumnWidth(12, 4000);

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

		Row headerRow = sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sl#");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		mainCell = headerRow.createCell(1);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_NUMBER);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		mainCell = headerRow.createCell(2);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sample ID");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		mainCell = headerRow.createCell(3);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Rust Type");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		mainCell = headerRow.createCell(4);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Survey Date");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		mainCell = headerRow.createCell(5);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Innoculation Date");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
		mainCell = headerRow.createCell(6);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.LOCATION);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
		mainCell = headerRow.createCell(10);
		mainCell.setCellValue("Laboratory Name");
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
		mainCell = headerRow.createCell(11);
		mainCell.setCellValue("Race Published Date");
		mainCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 12, 12));
		mainCell = headerRow.createCell(12);
		mainCell.setCellValue("Race Name");
		mainCell.setCellStyle(headerCellStyle);

		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(2);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(3);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(4);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(5);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(9);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(10);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(11);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(12);
		subCell.setCellStyle(headerCellStyle);

		for (int i = 0; i < beans.size(); i++) {
			SampleTagBean bean1 = beans.get(i);
			Row row1 = sheet.createRow(i + 2);
			Cell cell = row1.createCell(0);
			cell.setCellValue(i + 1);

			cell = row1.createCell(1);
			cell.setCellValue(bean1.getSurveyNo());

			cell = row1.createCell(2);
			cell.setCellValue(bean1.getSampleValue());

			cell = row1.createCell(3);
			cell.setCellValue(bean1.getRustType());

			cell = row1.createCell(4);
			cell.setCellValue(bean1.getSurveyDate());

			cell = row1.createCell(5);
			cell.setCellValue(bean1.getInoculationDate());

			cell = row1.createCell(6);
			cell.setCellValue(bean1.getRegion());

			cell = row1.createCell(7);
			cell.setCellValue(bean1.getZone());

			cell = row1.createCell(8);
			cell.setCellValue(bean1.getWoreda());

			cell = row1.createCell(9);
			cell.setCellValue(bean1.getKebel());

			cell = row1.createCell(10);
			cell.setCellValue(bean1.getResearchCenter());

			cell = row1.createCell(11);
			cell.setCellValue(bean1.getRacePublishDate());

			cell = row1.createCell(12);
			cell.setCellValue(bean1.getRaceResult());
		}

		return workbook;

	}

	public HSSFWorkbook buildRaceAnalysisStatusExcel(List<SampleTagBean> beans) {

		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("RaceAnalysisStatus");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 4000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 4000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 4000);
		sheet.setColumnWidth(9, 4000);
		sheet.setColumnWidth(10, 6000);
		sheet.setColumnWidth(11, 6000);
		sheet.setColumnWidth(12, 4000);
		sheet.setColumnWidth(13, 4000);

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

		Row headerRow = sheet.createRow(0);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sl#");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		mainCell = headerRow.createCell(1);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.SURVEY_NUMBER);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		mainCell = headerRow.createCell(2);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sample ID");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		mainCell = headerRow.createCell(3);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Rust Type");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		mainCell = headerRow.createCell(4);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Survey Date");

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		mainCell = headerRow.createCell(5);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Innoculation Date");

		sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
		mainCell = headerRow.createCell(6);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.LOCATION);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
		mainCell = headerRow.createCell(10);
		mainCell.setCellValue("Laboratory Name");
		mainCell.setCellStyle(headerCellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 11, 11));
		mainCell = headerRow.createCell(11);
		mainCell.setCellValue("Race Published Date");
		mainCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 12, 12));
		mainCell = headerRow.createCell(12);
		mainCell.setCellValue("Race Name");
		mainCell.setCellStyle(headerCellStyle);

		sheet.addMergedRegion(new CellRangeAddress(0, 1, 13, 13));
		mainCell = headerRow.createCell(13);
		mainCell.setCellValue("Status");
		mainCell.setCellStyle(headerCellStyle);

		// sub headers
		Row row = sheet.createRow(1);

		// sub header headers
		Cell subCell = row.createCell(0);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(1);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(2);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(3);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(4);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(5);
		subCell.setCellValue("");
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(6);
		subCell.setCellValue(WrsisPortalConstant.REGION);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(7);
		subCell.setCellValue(WrsisPortalConstant.ZONE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(8);
		subCell.setCellValue(WrsisPortalConstant.WOREDA);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(9);
		subCell.setCellValue(WrsisPortalConstant.KEBELE);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(10);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(11);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(12);
		subCell.setCellStyle(headerCellStyle);

		subCell = row.createCell(13);
		subCell.setCellStyle(headerCellStyle);

		for (int i = 0; i < beans.size(); i++) {
			SampleTagBean bean1 = beans.get(i);
			Row row1 = sheet.createRow(i + 2);
			Cell cell = row1.createCell(0);
			cell.setCellValue(i + 1);

			cell = row1.createCell(1);
			cell.setCellValue(bean1.getSurveyNo());

			cell = row1.createCell(2);
			cell.setCellValue(bean1.getSampleValue());

			cell = row1.createCell(3);
			cell.setCellValue(bean1.getRustType());

			cell = row1.createCell(4);
			cell.setCellValue(bean1.getSurveyDate());

			cell = row1.createCell(5);
			cell.setCellValue(bean1.getInoculationDate());

			cell = row1.createCell(6);
			cell.setCellValue(bean1.getRegion());

			cell = row1.createCell(7);
			cell.setCellValue(bean1.getZone());

			cell = row1.createCell(8);
			cell.setCellValue(bean1.getWoreda());

			cell = row1.createCell(9);
			cell.setCellValue(bean1.getKebel());

			cell = row1.createCell(10);
			cell.setCellValue(bean1.getResearchCenter());

			cell = row1.createCell(11);
			cell.setCellValue(bean1.getRacePublishDate());

			cell = row1.createCell(12);
			cell.setCellValue(bean1.getRaceResult());

			cell = row1.createCell(13);
			cell.setCellValue(bean1.getStatus());
		}

		return workbook;

	}

	public HSSFWorkbook buildPathologistReportExcel(List<UserBean> beans) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("Pathalogist Report");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
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

		Row headerRow = sheet.createRow(0);
		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sl#");

		mainCell = headerRow.createCell(1);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Research Center Name");

		mainCell = headerRow.createCell(2);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Pathologist Name");

		mainCell = headerRow.createCell(3);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.GENDER);

		mainCell = headerRow.createCell(4);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Designation");

		mainCell = headerRow.createCell(5);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Role");

		mainCell = headerRow.createCell(6);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.MOBILE);

		mainCell = headerRow.createCell(7);
		mainCell.setCellValue(WrsisPortalConstant.EMAIL);
		mainCell.setCellStyle(headerCellStyle);

		for (int i = 0; i < beans.size(); i++) {
			UserBean bean1 = beans.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean1.getOrganizationName());

			cell = row.createCell(2);
			cell.setCellValue(bean1.getFullName());

			cell = row.createCell(3);
			cell.setCellValue(bean1.getGenderName());

			cell = row.createCell(4);
			cell.setCellValue(bean1.getDesignation());

			cell = row.createCell(5);
			cell.setCellValue(bean1.getRoleName());

			cell = row.createCell(6);
			cell.setCellValue(bean1.getMobile());

			cell = row.createCell(7);
			cell.setCellValue(bean1.getEmail());

		}

		return workbook;
	}

	public HSSFWorkbook buildWoredaExpertReportExcel(List<UserBean> beans) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("Woreda Expert Report");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
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

		Row headerRow = sheet.createRow(0);
		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sl#");

		mainCell = headerRow.createCell(1);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Woreda Expert Name");

		mainCell = headerRow.createCell(2);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.GENDER);

		mainCell = headerRow.createCell(3);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Designation");

		mainCell = headerRow.createCell(4);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Role");

		mainCell = headerRow.createCell(5);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.MOBILE);

		mainCell = headerRow.createCell(6);
		mainCell.setCellValue(WrsisPortalConstant.EMAIL);
		mainCell.setCellStyle(headerCellStyle);

		mainCell = headerRow.createCell(7);
		mainCell.setCellValue(WrsisPortalConstant.LOCATION);
		mainCell.setCellStyle(headerCellStyle);

		for (int i = 0; i < beans.size(); i++) {
			UserBean bean1 = beans.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean1.getFullName());

			cell = row.createCell(2);
			cell.setCellValue(bean1.getGenderName());

			cell = row.createCell(3);
			cell.setCellValue(bean1.getDesignation());

			cell = row.createCell(4);
			cell.setCellValue(bean1.getRoleName());

			cell = row.createCell(5);
			cell.setCellValue(bean1.getMobile());

			cell = row.createCell(6);
			cell.setCellValue(bean1.getEmail());

			cell = row.createCell(7);
			cell.setCellValue(bean1.getDemoName());

		}

		return workbook;
	}

	public HSSFWorkbook buildDevAgentReportExcel(List<UserBean> beans) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		// sheet creation
		Sheet sheet = workbook.createSheet("Woreda Expert Report");

		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
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

		Row headerRow = sheet.createRow(0);
		Cell mainCell = headerRow.createCell(0);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Sl#");

		mainCell = headerRow.createCell(1);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Development Agent Name");

		mainCell = headerRow.createCell(2);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.GENDER);

		mainCell = headerRow.createCell(3);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Designation");

		mainCell = headerRow.createCell(4);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue("Role");

		mainCell = headerRow.createCell(5);
		mainCell.setCellStyle(headerCellStyle);
		mainCell.setCellValue(WrsisPortalConstant.MOBILE);

		mainCell = headerRow.createCell(6);
		mainCell.setCellValue(WrsisPortalConstant.EMAIL);
		mainCell.setCellStyle(headerCellStyle);

		mainCell = headerRow.createCell(7);
		mainCell.setCellValue(WrsisPortalConstant.LOCATION);
		mainCell.setCellStyle(headerCellStyle);

		for (int i = 0; i < beans.size(); i++) {
			UserBean bean1 = beans.get(i);
			Row row = sheet.createRow(i + 1);
			Cell cell = row.createCell(0);
			cell.setCellValue(i + 1);

			cell = row.createCell(1);
			cell.setCellValue(bean1.getFullName());

			cell = row.createCell(2);
			cell.setCellValue(bean1.getGenderName());

			cell = row.createCell(3);
			cell.setCellValue(bean1.getDesignation());

			cell = row.createCell(4);
			cell.setCellValue(bean1.getRoleName());

			cell = row.createCell(5);
			cell.setCellValue(bean1.getMobile());

			cell = row.createCell(6);
			cell.setCellValue(bean1.getEmail());

			cell = row.createCell(7);
			cell.setCellValue(bean1.getDemoName());

		}

		return workbook;
	}

}
