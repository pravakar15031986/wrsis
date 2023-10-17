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

public class RustPrevalenceReportExcel {

	private static final Logger LOG = LoggerFactory.getLogger(RustPrevalenceReportExcel.class);

	public HSSFWorkbook buildExcelRustPrevalenceReportDetails(String jsonData) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation
			Sheet sheet = workbook.createSheet("Rust Prevalence Report");
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
			mainCell.setCellValue(WrsisPortalConstant.REGION);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			mainCell = headerRow.createCell(1);
			mainCell.setCellValue(WrsisPortalConstant.ZONE);
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
			mainCell = headerRow.createCell(2);
			mainCell.setCellValue("No. of fields assessed");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
			mainCell = headerRow.createCell(3);
			mainCell.setCellValue("Yellow Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
			mainCell = headerRow.createCell(5);
			mainCell.setCellValue("Stem Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
			mainCell = headerRow.createCell(7);
			mainCell.setCellValue("Leaf rust");
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
			subCell.setCellValue(WrsisPortalConstant.INFECTED_FIELD);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(4);
			subCell.setCellValue("Prev (%)");
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(5);
			subCell.setCellValue(WrsisPortalConstant.INFECTED_FIELD);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(6);
			subCell.setCellValue("Prev (%)");
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(7);
			subCell.setCellValue(WrsisPortalConstant.INFECTED_FIELD);
			subCell.setCellStyle(headerCellStyle);

			subCell = row.createCell(8);
			subCell.setCellValue("Prev (%)");
			subCell.setCellStyle(headerCellStyle);

			JSONArray jsonArray = new JSONArray(jsonData);
			int rowCount = 2;
			for (int i = 0; i < jsonArray.length(); i++) {

				int totalRowC = rowCount + i;

				JSONObject jsonRegObject = new JSONObject();
				jsonRegObject = jsonArray.getJSONObject(i);

				Integer fields = 0;
				Double yelloInfectedField = 0.0;
				Double stemInfectedField = 0.0;
				Double leafInfectedField = 0.0;
				Double yelloInfectedFieldPer = 0.0;
				Double stemInfectedFieldPer = 0.0;
				Double leafInfectedFieldPer = 0.0;

				JSONArray jsonZoneArr = (JSONArray) jsonRegObject.get("zonedetails");
				int zoneLength = jsonZoneArr.length();
				for (int zone = 0; zone < jsonZoneArr.length(); zone++) {
					Boolean yellowRust = false;
					Boolean stemRust = false;
					Boolean leafRust = false;

					Row row1 = sheet.createRow(zone + totalRowC);
					Cell cell = row1.createCell(0);
					if (jsonRegObject.getString("regionname") != null) {
						cell.setCellValue(jsonRegObject.getString("regionname"));
						cell.setCellStyle(cellStyle);
					} else {
						cell.setCellValue("");
					}

					JSONObject jsonZoneObject = new JSONObject();
					jsonZoneObject = jsonZoneArr.getJSONObject(zone);

					cell = row1.createCell(1);
					cell.setCellValue(jsonZoneObject.getString("zonename"));
					cell.setCellStyle(cellStyle);

					cell = row1.createCell(2);
					cell.setCellValue(Integer.parseInt((jsonZoneObject.getString("field"))));
					cell.setCellStyle(cellStyle);

					fields = fields + Integer.parseInt(jsonZoneObject.getString("field"));

					Object ob = jsonZoneObject.get("rustdtls");
					if (!ob.toString().contains("null")) {
						JSONArray jsonRustArr = (JSONArray) jsonZoneObject.get("rustdtls");

						for (int rust = 0; rust < jsonRustArr.length(); rust++) {
							JSONObject jsonRustObject = new JSONObject();
							jsonRustObject = jsonRustArr.getJSONObject(rust);

							if (Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.RUST_TYPE)) == 3)// yellow
							{
								yellowRust = true;
								cell = row1.createCell(3);
								cell.setCellValue(Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.INFECTED)));
								yelloInfectedField = yelloInfectedField
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED));
								cell.setCellStyle(cellStyle);

								cell = row1.createCell(4);
								cell.setCellValue(Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER)));
								yelloInfectedFieldPer = yelloInfectedFieldPer
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER));
								cell.setCellStyle(cellStyle);
							}

							if (Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.RUST_TYPE)) == 1)// Stem
							{
								stemRust = true;
								cell = row1.createCell(5);
								cell.setCellValue(Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.INFECTED)));
								stemInfectedField = stemInfectedField
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED));
								cell.setCellStyle(cellStyle);

								cell = row1.createCell(6);
								cell.setCellValue(Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER)));
								stemInfectedFieldPer = stemInfectedFieldPer
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER));
								cell.setCellStyle(cellStyle);
							}

							if (Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.RUST_TYPE)) == 2)// Leaf
							{
								leafRust = true;
								cell = row1.createCell(7);
								cell.setCellValue(Integer.parseInt(jsonRustObject.getString(WrsisPortalConstant.INFECTED)));
								leafInfectedField = leafInfectedField
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED));
								cell.setCellStyle(cellStyle);

								cell = row1.createCell(8);
								cell.setCellValue(Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER)));
								leafInfectedFieldPer = leafInfectedFieldPer
										+ Double.parseDouble(jsonRustObject.getString(WrsisPortalConstant.INFECTED_PER));
								cell.setCellStyle(cellStyle);

							}
						}
						if (!yellowRust) {

							cell = row1.createCell(3);
							cell.setCellValue(Integer.parseInt("0"));
							yelloInfectedField = yelloInfectedField + 0;
							cell.setCellStyle(cellStyle);

							cell = row1.createCell(4);
							cell.setCellValue(Double.parseDouble("0"));
							yelloInfectedFieldPer = yelloInfectedFieldPer + 0;
							cell.setCellStyle(cellStyle);

						}

						if (!stemRust) {

							cell = row1.createCell(5);
							cell.setCellValue(Integer.parseInt("0"));
							stemInfectedField = stemInfectedField + 0;
							cell.setCellStyle(cellStyle);

							cell = row1.createCell(6);
							cell.setCellValue(Double.parseDouble("0"));
							stemInfectedFieldPer = stemInfectedFieldPer + 0;
							cell.setCellStyle(cellStyle);

						}

						if (!leafRust) {

							cell = row1.createCell(7);
							cell.setCellValue(Integer.parseInt("0"));
							leafInfectedField = leafInfectedField + 0;
							cell.setCellStyle(cellStyle);

							cell = row1.createCell(8);
							cell.setCellValue(Double.parseDouble("0"));
							yelloInfectedFieldPer = yelloInfectedFieldPer + 0;
							cell.setCellStyle(cellStyle);

						}

					} else {
						cell = row1.createCell(3);
						cell.setCellValue(Integer.parseInt("0"));
						yelloInfectedField = yelloInfectedField + 0;
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(4);
						cell.setCellValue(Double.parseDouble("0"));
						yelloInfectedFieldPer = yelloInfectedFieldPer + 0;
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(5);
						cell.setCellValue(Integer.parseInt("0"));
						stemInfectedField = stemInfectedField + 0;
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(6);
						cell.setCellValue(Double.parseDouble("0"));
						stemInfectedFieldPer = stemInfectedFieldPer + 0;
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(7);
						cell.setCellValue(Integer.parseInt("0"));
						leafInfectedField = leafInfectedField + 0;
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(8);
						cell.setCellValue(Double.parseDouble("0"));
						yelloInfectedFieldPer = yelloInfectedFieldPer + 0;
						cell.setCellStyle(cellStyle);

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
						cell2.setCellValue(fields);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(3);
						cell2.setCellValue(yelloInfectedField);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(4);
						cell2.setCellValue((((yelloInfectedFieldPer) * 100) / (jsonZoneArr.length() * 100)));
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(5);
						cell2.setCellValue(stemInfectedField);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(6);
						cell2.setCellValue((((stemInfectedFieldPer) * 100) / (jsonZoneArr.length() * 100)));
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(7);
						cell2.setCellValue(leafInfectedField);
						cell2.setCellStyle(totalCellStyle);

						cell2 = row2.createCell(8);
						cell2.setCellValue((((leafInfectedFieldPer) * 100) / (jsonZoneArr.length() * 100)));
						cell2.setCellStyle(totalCellStyle);
					}
				}
			}
		} catch (Exception e) {

			LOG.error("RustPrevalenceReportExcel::buildExcelRustPrevalenceReportDetails():" + e.getMessage());
		}
		return workbook;
	}

}
