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

public class RustIncidenceSeverityReportExcel {
	private static final Logger LOG = LoggerFactory.getLogger(RustIncidenceSeverityReportExcel.class);

	public HSSFWorkbook generateRustIncidentSeverityExcel(String jsonData) {
		HSSFWorkbook workBook = new HSSFWorkbook();
		try {
			Sheet sheet = workBook.createSheet("Rust Incidence Severity");
			// Space the cells
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 4000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 4000);
			sheet.setColumnWidth(4, 4000);
			sheet.setColumnWidth(5, 4000);
			sheet.setColumnWidth(6, 4000);
			sheet.setColumnWidth(7, 4000);
			sheet.setColumnWidth(8, 4000);
			sheet.setColumnWidth(9, 4000);
			sheet.setColumnWidth(10, 4000);
			sheet.setColumnWidth(11, 4000);
			sheet.setColumnWidth(12, 4000);
			sheet.setColumnWidth(13, 4000);

			// Create a Font for styling header cells
			Font headerFont = workBook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setColor(IndexedColors.BLACK.index);

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workBook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			DataFormat fmt = workBook.createDataFormat();
			headerCellStyle.setDataFormat(fmt.getFormat("@"));
			headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

			headerCellStyle.setBorderBottom(BorderStyle.THICK);
			headerCellStyle.setBorderTop(BorderStyle.THICK);
			headerCellStyle.setBorderRight(BorderStyle.THICK);
			headerCellStyle.setBorderLeft(BorderStyle.THICK);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			CellStyle textCellStyle = workBook.createCellStyle();
			textCellStyle.setDataFormat(fmt.getFormat("@"));

			CellStyle totalCellStyle = workBook.createCellStyle();
			totalCellStyle.setFont(headerFont);
			totalCellStyle.setAlignment(HorizontalAlignment.LEFT);
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREEN.getIndex());

			Row headerRow = sheet.createRow(0);

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			Cell mainCell = headerRow.createCell(0);
			mainCell.setCellValue(WrsisPortalConstant.REGION);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			mainCell = headerRow.createCell(1);
			mainCell.setCellValue(WrsisPortalConstant.ZONE);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 5));
			mainCell = headerRow.createCell(2);
			mainCell.setCellValue("Yellow Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 9));
			mainCell = headerRow.createCell(6);
			mainCell.setCellValue("Stem Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 10, 13));
			mainCell = headerRow.createCell(10);
			mainCell.setCellValue("Leaf rust");
			mainCell.setCellStyle(headerCellStyle);

			// 2nd sub headers
			Row subrow = sheet.createRow(1);

			// 2nd sub header Cells
			Cell subCell = subrow.createCell(0);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(1);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
			subCell = subrow.createCell(2);
			subCell.setCellValue("Incidence");
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 5));
			subCell = subrow.createCell(4);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));
			subCell = subrow.createCell(6);
			subCell.setCellValue("Incidence ");
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
			subCell = subrow.createCell(8);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 11));
			subCell = subrow.createCell(10);
			subCell.setCellValue("Incidence");
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 13));
			subCell = subrow.createCell(12);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			// 3rd sub headers
			Row subsubrow = sheet.createRow(2);

			// sub header headers
			Cell subsubCell = subsubrow.createCell(0);
			subsubCell.setCellValue("");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(1);
			subsubCell.setCellValue("");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(2);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(3);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(4);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(5);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(6);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(7);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(8);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(9);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(10);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(11);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(12);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(13);
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			int rowCount = 3;
			JSONArray jsonArr = new JSONArray(jsonData);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject jsonRegObj = new JSONObject();
				jsonRegObj = jsonArr.getJSONObject(i);

				int totalRowC = rowCount + i;

				Integer yIncidenceHigh = 0;
				Integer ySeverityHigh = 0;

				Integer sIncidenceHigh = 0;
				Integer sSeverityHigh = 0;

				Integer lIncidenceHigh = 0;
				Integer lSeverityHigh = 0;

				Double yIncidenceMeanHigh = 0.0;
				Double ySeverityMeanHigh = 0.0;

				Double sIncidenceMeanHigh = 0.0;
				Double sSeverityMeanHigh = 0.0;

				Double lIncidenceMeanHigh = 0.0;
				Double lSeverityMeanHigh = 0.0;

				JSONArray jsonZoneArr = (JSONArray) jsonRegObj.get("zonedetails");
				int zoneLength = jsonZoneArr.length();
				for (int zone = 0; zone < jsonZoneArr.length(); zone++) {
					Row row = sheet.createRow(zone + totalRowC);

					Cell cell = null;
					if (jsonRegObj.getString("regionname") != null) {
						cell = row.createCell(0);
						cell.setCellValue(jsonRegObj.getString("regionname"));
					} else {
						cell = row.createCell(0);
						cell.setCellValue("");
					}

					JSONObject jsonZoneObj = new JSONObject();
					jsonZoneObj = jsonZoneArr.getJSONObject(zone);
					if (jsonZoneObj.getString("zonename") != null) {
						cell = row.createCell(1);
						cell.setCellValue(jsonZoneObj.getString("zonename"));
					} else {
						cell = row.createCell(1);
						cell.setCellValue("");
					}
					JSONArray jsonIncSevArr = (JSONArray) jsonZoneObj.get("incsevdetails");

					// Yellow Rust
					JSONObject jsonIncSevYellowObj = new JSONObject();
					jsonIncSevYellowObj = jsonIncSevArr.getJSONObject(0);

					if (!jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_RANGE).contentEquals("0-")) {
						cell = row.createCell(2);
						cell.setCellValue(jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_RANGE));
						Object obj = jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_RANGE);
						String incRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = yIncidenceHigh;
						yIncidenceHigh = Integer.parseInt(incRenge);
						yIncidenceHigh = (yIncidenceHigh > x) ? yIncidenceHigh : x;
					} else {
						cell = row.createCell(2);
						cell.setCellValue("0-0");
						yIncidenceHigh = 0;
					}

					Object yellIncMean = jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_MEAN);
					if (!yellIncMean.toString().contains("null")) {
						cell = row.createCell(3);
						cell.setCellValue(jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
						yIncidenceMeanHigh = yIncidenceMeanHigh
								+ Double.parseDouble(jsonIncSevYellowObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
					} else {
						cell = row.createCell(3);
						cell.setCellValue("0");
						yIncidenceMeanHigh = yIncidenceMeanHigh + 0;
					}

					if (!jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_RANGE).contentEquals("0-")) {
						cell = row.createCell(4);
						cell.setCellValue(jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_RANGE));
						Object obj = jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_RANGE);
						String sevRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = ySeverityHigh;
						ySeverityHigh = Integer.parseInt(sevRenge);
						ySeverityHigh = (ySeverityHigh > x) ? ySeverityHigh : x;
					} else {
						cell = row.createCell(4);
						cell.setCellValue("0-0");
						ySeverityHigh = 0;
					}

					Object yellSevMean = jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_MEAN);
					if (!yellSevMean.toString().contains("null")) {
						cell = row.createCell(5);
						cell.setCellValue(jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
						ySeverityMeanHigh = ySeverityMeanHigh
								+ Double.parseDouble(jsonIncSevYellowObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
					} else {
						cell = row.createCell(5);
						cell.setCellValue("0");
						ySeverityMeanHigh = ySeverityMeanHigh + 0;
					}

					// Stem Rust
					JSONObject jsonIncSevStemRustObj = new JSONObject();
					jsonIncSevStemRustObj = jsonIncSevArr.getJSONObject(1);

					if (!jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE).contentEquals("0-")) {
						cell = row.createCell(6);
						cell.setCellValue(jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE));
						Object obj = jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE);
						String incRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = sIncidenceHigh;
						sIncidenceHigh = Integer.parseInt(incRenge);
						sIncidenceHigh = (sIncidenceHigh > x) ? sIncidenceHigh : x;
					} else {
						cell = row.createCell(6);
						cell.setCellValue("0-0");
						sIncidenceHigh = 0;
					}
					Object stemIncMean = jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN);
					if (!stemIncMean.toString().contains("null")) {
						cell = row.createCell(7);
						cell.setCellValue(jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
						sIncidenceMeanHigh = sIncidenceMeanHigh
								+ Double.parseDouble(jsonIncSevStemRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
					} else {
						cell = row.createCell(7);
						cell.setCellValue("0");
						sIncidenceMeanHigh = sIncidenceMeanHigh + 0;
					}
					if (!jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE).contentEquals("0-")) {
						cell = row.createCell(8);
						cell.setCellValue(jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE));
						Object obj = jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE);
						String sevRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = sSeverityHigh;
						sSeverityHigh = Integer.parseInt(sevRenge);
						sSeverityHigh = (sSeverityHigh > x) ? sSeverityHigh : x;
					} else {
						cell = row.createCell(8);
						cell.setCellValue("0-0");
					}

					Object stemSevMean = jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN);
					if (!stemSevMean.toString().contains("null")) {
						cell = row.createCell(9);
						cell.setCellValue(jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
						sSeverityMeanHigh = sSeverityMeanHigh
								+ Double.parseDouble(jsonIncSevStemRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
					} else {
						cell = row.createCell(9);
						cell.setCellValue("0");
						sSeverityMeanHigh = sSeverityMeanHigh + 0;
					}

					// Leaf rust
					JSONObject jsonIncSevLeafRustObj = new JSONObject();
					jsonIncSevLeafRustObj = jsonIncSevArr.getJSONObject(2);

					if (!jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE).contentEquals("0-")) {

						cell = row.createCell(10);
						cell.setCellValue(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE));
						Object obj = jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_RANGE);
						String incRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = lIncidenceHigh;
						lIncidenceHigh = Integer.parseInt(incRenge);

						lIncidenceHigh = (lIncidenceHigh > x) ? lIncidenceHigh : x;

					} else {
						cell = row.createCell(10);
						cell.setCellValue("0-0");
					}

					Object leafIncMean = jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN);
					if (!leafIncMean.toString().contains("null")) {
						cell = row.createCell(11);
						cell.setCellValue(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
						lIncidenceMeanHigh = lIncidenceMeanHigh
								+ Double.parseDouble(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.INCIDENCE_MEAN));
					} else {
						cell = row.createCell(11);
						cell.setCellValue("0");
						lIncidenceMeanHigh = lIncidenceMeanHigh + 0;
					}
					if (!jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE).contentEquals("0-")) {
						cell = row.createCell(12);
						cell.setCellValue(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE));
						Object obj = jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_RANGE);
						String sevRenge = obj.toString().replaceAll("[^0-9]", "");
						Integer x = lSeverityHigh;
						lSeverityHigh = Integer.parseInt(sevRenge);
						lSeverityHigh = (lSeverityHigh > x) ? lSeverityHigh : x;
					} else {
						cell = row.createCell(12);
						cell.setCellValue("0-0");
					}

					Object leafSevMean = jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN);
					if (!leafSevMean.toString().contains("null")) {
						cell = row.createCell(13);
						cell.setCellValue(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
						lSeverityMeanHigh = lSeverityMeanHigh
								+ Double.parseDouble(jsonIncSevLeafRustObj.getString(WrsisPortalConstant.SEVERITY_MEAN));
					} else {
						cell = row.createCell(13);
						cell.setCellValue("0");
						lSeverityMeanHigh = lSeverityMeanHigh + 0;
					}
					int zonee = zone + 1;
					if (zoneLength == zonee) {

						int countt = totalRowC + zonee;
						Row row2 = sheet.createRow(countt);
						Cell cell2 = row2.createCell(1);
						cell2.setCellValue("Total");
						cell2.setCellStyle(totalCellStyle);
						rowCount = countt;

						cell2 = row2.createCell(2);
						cell2.setCellValue("0-" + yIncidenceHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(3);
						Double yTotalIncMeanHig = (yIncidenceMeanHigh / zoneLength);
						// d2.set
						cell2.setCellValue(yTotalIncMeanHig);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(4);
						cell2.setCellValue("0-" + ySeverityHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(5);
						Double yTotalSeveMeanHig = (ySeverityMeanHigh / zoneLength);
						cell2.setCellValue(yTotalSeveMeanHig);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(6);
						cell2.setCellValue("0-" + sIncidenceHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(7);
						Double sTotalIncMeanHig = (sIncidenceMeanHigh / zoneLength);
						cell2.setCellValue(sTotalIncMeanHig);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(8);
						cell2.setCellValue("0-" + sSeverityHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(9);
						Double sTotalSeveMeanHig = (sSeverityMeanHigh / zoneLength);
						cell2.setCellValue(sTotalSeveMeanHig);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(10);
						cell2.setCellValue("0-" + lIncidenceHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(11);
						Double lTotalIncMeanHig = (lIncidenceMeanHigh / zoneLength);
						cell2.setCellValue(lTotalIncMeanHig);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(12);
						cell2.setCellValue("0-" + lSeverityHigh);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(13);
						Double lTotalSeveMeanHig = (lSeverityMeanHigh / zoneLength);
						
						cell2.setCellValue(lTotalSeveMeanHig);
						cell2.setCellStyle(totalCellStyle);
					}

				}

			}

		} catch (Exception e) {
			LOG.error("RustIncidenceSeverityReportExcel::generateRustIncidentSeverityExcel():" + e.getMessage());

		}
		return workBook;
	}
}
