package com.csmpl.wrsis.webportal.excel;

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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;

public class RaceBySample {

	private static final Logger LOG = LoggerFactory.getLogger(RaceBySample.class);

	public HSSFWorkbook buildExcelRaceBySampleReportDetails(JSONObject responsedata)// String jsonData
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation
			Sheet sheet = workbook.createSheet("Race By Sample Report");
			sheet.setColumnWidth(0, 6000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
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

			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);

			CellStyle totalCellStyle = workbook.createCellStyle();
			totalCellStyle.setFont(headerFont);
			totalCellStyle.setAlignment(HorizontalAlignment.CENTER);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			// Create a Row
			// main header
			JSONArray jsonarr = responsedata.getJSONArray("Columns");
			Row headerRow = sheet.createRow(0);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			Cell mainCell = headerRow.createCell(0);
			mainCell.setCellValue(WrsisPortalConstant.REGION);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			mainCell = headerRow.createCell(1);
			mainCell.setCellValue("Samples analyzed");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, jsonarr.length() + 1));
			mainCell = headerRow.createCell(2);
			mainCell.setCellValue("Races");
			mainCell.setCellStyle(headerCellStyle);

			// sub headers
			Row subrow = sheet.createRow(1);

			// sub header headers
			Cell subCell = subrow.createCell(0);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(1);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			int runningCol = 2;
			int racescount = 0;
			JSONArray jsonArray = (JSONArray) responsedata.get("Columns");
			for (int race = 0; race < jsonArray.length(); race++) {

				subCell = subrow.createCell(runningCol + racescount);
				subCell.setCellValue(jsonArray.get(race).toString());
				subCell.setCellStyle(headerCellStyle);

				racescount = racescount + 1;

			}
			int totalCount = 2;

			JSONArray tableArray = (JSONArray) responsedata.get("TableData");
			if (tableArray != null && tableArray.length() > 0) {
				for (int i = 0; i < tableArray.length(); i++) {
					JSONArray childJsonArray = tableArray.optJSONArray(i);
					if (childJsonArray != null && childJsonArray.length() > 0) {
						int resultcell = 0;
						Row row2 = sheet.createRow(totalCount + i);
						Cell cell2 = null;
						for (int j = 0; j < childJsonArray.length() - 1; j++) {

							cell2 = row2.createCell(resultcell);
							cell2.setCellValue(childJsonArray.optString(j));
							cell2.setCellStyle(cellStyle);

							resultcell++;
						}
					}
				}
			}

		} catch (Exception e) {
			LOG.error("RaceBySample::buildExcelRaceBySampleReportDetails():" + e.getMessage());

		}

		return workbook;
	}

}
