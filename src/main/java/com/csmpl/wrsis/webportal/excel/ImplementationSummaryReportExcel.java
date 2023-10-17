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

public class ImplementationSummaryReportExcel {

	private static final Logger LOG = LoggerFactory.getLogger(ImplementationSummaryReportExcel.class);

	public HSSFWorkbook buildExcelImplementationSummaryReportDetails(String jsonData)// String jsonData
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation
			Sheet sheet = workbook.createSheet("Implementation Summary Report");
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
			Row headerRow = sheet.createRow(0);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			Cell mainCell = headerRow.createCell(0);
			mainCell.setCellValue("#Sl No.");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			mainCell = headerRow.createCell(1);
			mainCell.setCellValue(WrsisPortalConstant.REGION);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
			mainCell = headerRow.createCell(2);
			mainCell.setCellValue(WrsisPortalConstant.ZONE);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
			mainCell = headerRow.createCell(3);
			mainCell.setCellValue("No of Woredas affected");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
			mainCell = headerRow.createCell(4);
			mainCell.setCellValue("No of PAs affected");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
			mainCell = headerRow.createCell(5);
			mainCell.setCellValue("Total area infected (Ha)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
			mainCell = headerRow.createCell(6);
			mainCell.setCellValue("Total area controlled (Ha)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
			mainCell = headerRow.createCell(7);
			mainCell.setCellValue("Total area controlled (%)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
			mainCell = headerRow.createCell(8);
			mainCell.setCellValue("Total fungicides used in kg(lit)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 11));
			mainCell = headerRow.createCell(9);
			mainCell.setCellValue("Numbers of farmers participated on spraying");
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

			subCell = subrow.createCell(2);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(3);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(4);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(5);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(6);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(7);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(8);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(9);
			subCell.setCellValue(WrsisPortalConstant.MALE);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(10);
			subCell.setCellValue(WrsisPortalConstant.FEMALE);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(11);
			subCell.setCellValue("Total");
			subCell.setCellStyle(headerCellStyle);

			JSONArray jsonArray = new JSONArray(jsonData);
			int count = 2;
			int totalCount = 2 + jsonArray.length();
			int nWoreda = 0;
			int pas = 0;
			int area = 0;
			int carea = 0;
			Double careap = 0.0;
			Double fused = 0.0;
			int males = 0;
			int females = 0;
			int totalfarmer = 0;
			int rowcount = 1;
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonRegObject = new JSONObject();
				jsonRegObject = jsonArray.getJSONObject(i);
				Row row1 = sheet.createRow(i + count);
				Cell cell = row1.createCell(0);
				cell.setCellValue(i + rowcount);
				cell.setCellStyle(cellStyle);

				cell = row1.createCell(1);
				if (jsonRegObject.getString("regionname") != null) {
					cell.setCellValue(jsonRegObject.getString("regionname"));
					cell.setCellStyle(cellStyle);
				} else {
					cell.setCellValue("");
				}

				JSONArray jsonZoneArr = (JSONArray) jsonRegObject.get("zonedetails");

				for (int zone = 0; zone < jsonZoneArr.length(); zone++) {
					JSONObject jsonZoneObject = new JSONObject();
					jsonZoneObject = jsonZoneArr.getJSONObject(zone);

					cell = row1.createCell(2);
					cell.setCellValue(jsonZoneObject.getString("zonename"));
					cell.setCellStyle(cellStyle);

					JSONArray countArr = (JSONArray) jsonZoneObject.get("finalcounts");
					for (int dtls = 0; dtls < countArr.length(); dtls++) {
						JSONObject jsonDtlsObject = new JSONObject();
						jsonDtlsObject = countArr.getJSONObject(dtls);

						cell = row1.createCell(3);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("woredas")));
						nWoreda = nWoreda + Integer.parseInt((jsonDtlsObject.getString("woredas")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(4);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("kebels")));
						pas = pas + Integer.parseInt((jsonDtlsObject.getString("kebels")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(5);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("totalinfectedarea")));
						area = area + Integer.parseInt((jsonDtlsObject.getString("totalinfectedarea")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(6);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("controlledarea")));
						carea = carea + Integer.parseInt((jsonDtlsObject.getString("controlledarea")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(7);
						cell.setCellValue(Double.parseDouble(jsonDtlsObject.getString("totalareaper")));
						careap = careap + Double.parseDouble(jsonDtlsObject.getString("totalareaper"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(8);
						cell.setCellValue(Double.parseDouble(jsonDtlsObject.getString("fungicideused")));
						fused = fused + Double.parseDouble((jsonDtlsObject.getString("fungicideused")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(9);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("male")));
						males = males + Integer.parseInt((jsonDtlsObject.getString("male")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(10);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("female")));
						females = females + Integer.parseInt((jsonDtlsObject.getString("female")));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(11);
						cell.setCellValue(Integer.parseInt(jsonDtlsObject.getString("totalfamer")));
						totalfarmer = totalfarmer + Integer.parseInt((jsonDtlsObject.getString("totalfamer")));
						cell.setCellStyle(cellStyle);
					}

				}

			}

			Row row2 = sheet.createRow(totalCount);

			Cell cell2 = row2.createCell(0);
			cell2.setCellValue("");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(1);
			cell2.setCellValue("");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(2);
			cell2.setCellValue("Total");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(3);
			cell2.setCellValue(nWoreda);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(4);
			cell2.setCellValue(pas);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(5);
			cell2.setCellValue(area);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(6);
			cell2.setCellValue(carea);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(7);
			cell2.setCellValue(careap);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(8);
			cell2.setCellValue(fused);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(9);
			cell2.setCellValue(males);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(10);
			cell2.setCellValue(females);
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(11);
			cell2.setCellValue(totalfarmer);
			cell2.setCellStyle(totalCellStyle);

		} catch (Exception e) {
			LOG.error("ImplementationReportExcel::buildExcelImplementationSummaryReportDetails():" + e.getMessage());

		}

		return workbook;
	}

}
