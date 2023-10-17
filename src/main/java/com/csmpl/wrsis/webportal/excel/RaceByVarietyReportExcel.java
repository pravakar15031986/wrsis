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
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.VarietyTypeMasterEntity;
import com.csmpl.wrsis.webportal.repository.VarietyTypeRepository;

public class RaceByVarietyReportExcel {

	private static final Logger LOG = LoggerFactory.getLogger(RaceByVarietyReportExcel.class);

	public HSSFWorkbook buildExcelRaceByVarietyReportDetails(VarietyTypeRepository varietyTypeRepository,
			String jsonData)// String jsonData
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation
			Sheet sheet = workbook.createSheet("Race By Variety Report");
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
			mainCell.setCellValue("Sl. No.");
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
			mainCell.setCellValue("Race Name");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
			mainCell = headerRow.createCell(4);
			mainCell.setCellValue("No. of Samples");
			mainCell.setCellStyle(headerCellStyle);

			List<VarietyTypeMasterEntity> variety = varietyTypeRepository.fetchActiveVarietyType();
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, variety.size() + 4));
			mainCell = headerRow.createCell(5);
			mainCell.setCellValue("Name of the Variety");
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

			int runningCol = 4;
			int varietycount = 1;
			// fetch all active Variety
			List<VarietyTypeMasterEntity> varieties = varietyTypeRepository.fetchActiveVarietyType();
			for (int varie = 0; varie < varieties.size(); varie++) {

				subCell = subrow.createCell(runningCol + varietycount);
				subCell.setCellValue(varieties.get(varie).getVchDesc());
				subCell.setCellStyle(headerCellStyle);

				varietycount = varietycount + 1;

			}

			JSONArray jsonArray = new JSONArray(jsonData);
			int samplecount = 0;
			int count = 2;
			ArrayList<Integer> total = new ArrayList<>();
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonRegObject = new JSONObject();
				jsonRegObject = jsonArray.getJSONObject(i);
				Cell cell = null;
				JSONArray jsonZoneArr = (JSONArray) jsonRegObject.get("zonedtls");

				for (int zone = 0; zone < jsonZoneArr.length(); zone++) {
					JSONObject jsonZoneObject = new JSONObject();
					jsonZoneObject = jsonZoneArr.getJSONObject(zone);

					JSONArray raceArr = (JSONArray) jsonZoneObject.get("raceresults");
					for (int race = 0; race < raceArr.length(); race++) {

						// Region
						Row row1 = sheet.createRow(count);
						cell = row1.createCell(0);
						cell.setCellValue(count - 1);
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(1);
						if (jsonRegObject.getString("regionname") != null) {
							cell.setCellValue(jsonRegObject.getString("regionname"));
							cell.setCellStyle(cellStyle);
						} else {
							cell.setCellValue("");
						}

						// Zone

						cell = row1.createCell(2);
						cell.setCellValue(jsonZoneObject.getString("zonename"));
						cell.setCellStyle(cellStyle);

						// Race
						JSONObject jsonDtlsObject = new JSONObject();
						jsonDtlsObject = raceArr.getJSONObject(race);
						cell = row1.createCell(3);
						cell.setCellValue(jsonDtlsObject.getString("raceresult"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(4);
						cell.setCellValue(jsonDtlsObject.getString("samplecollected"));
						samplecount = samplecount + Integer.parseInt(jsonDtlsObject.getString("samplecollected"));
						cell.setCellStyle(cellStyle);

						int cellcout = 5;
						List<VarietyTypeMasterEntity> varietylist = varietyTypeRepository.fetchActiveVarietyType();
						for (int variM = 0; variM < varietylist.size(); variM++) {

							Object ob = jsonDtlsObject.get(WrsisPortalConstant.RESULT_WISE_VARIETY);
							if (!ob.toString().contains("null")) {
								JSONArray jsonVariArr = (JSONArray) jsonDtlsObject.get(WrsisPortalConstant.RESULT_WISE_VARIETY);
								for (int vari = 0; vari < jsonVariArr.length(); vari++) {
									JSONObject jsonVariObject = new JSONObject();
									jsonVariObject = jsonVariArr.getJSONObject(vari);
									if (varietylist.get(variM).getVarietyTypeId() == jsonVariObject
											.getInt("varietyid")) {
										cell = row1.createCell(cellcout);
										cell.setCellValue(jsonVariObject.getString("count"));
										cell.setCellStyle(cellStyle);
										total.add(variM, Integer.parseInt(jsonVariObject.getString("count")));
										cellcout++;
									} else {
										cell = row1.createCell(cellcout);
										cell.setCellValue("0");
										cell.setCellStyle(cellStyle);
										cellcout++;
										total.add(variM, Integer.parseInt("0"));
									}
								}

							} else {
								cell = row1.createCell(cellcout);
								cell.setCellValue("0");
								cell.setCellStyle(cellStyle);
								cellcout++;
								total.add(variM, Integer.parseInt("0"));
							}

						}
						count++;
					}

				}

			}

			Row row2 = sheet.createRow(count);

			Cell cell2 = row2.createCell(0);
			cell2.setCellValue("");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(1);
			cell2.setCellValue("");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(2);
			cell2.setCellValue("");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(3);
			cell2.setCellValue("Total");
			cell2.setCellStyle(totalCellStyle);

			cell2 = row2.createCell(4);
			cell2.setCellValue(samplecount);
			cell2.setCellStyle(totalCellStyle);

		} catch (Exception e) {
			LOG.error("RaceByVarietyReportExcel::buildExcelRaceByVarietyReportDetails():" + e.getMessage());

		}

		return workbook;
	}

}
