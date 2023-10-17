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
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.entity.FungicideMaster;
import com.csmpl.wrsis.webportal.repository.FungicideMasterRepository;

public class ImplementationReportExcel {

	private static final Logger LOG = LoggerFactory.getLogger(ImplementationReportExcel.class);

	public HSSFWorkbook buildExcelImplementationReportDetails(FungicideMasterRepository fungicideDetailsRepository,
			String jsonData)// String jsonData
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation

			Sheet sheet = workbook.createSheet("Implementation Report");
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
			mainCell.setCellValue("No of PAs affected");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
			mainCell = headerRow.createCell(3);
			mainCell.setCellValue("Type of rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
			mainCell = headerRow.createCell(4);
			mainCell.setCellValue("Variety of crop affected");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));
			mainCell = headerRow.createCell(5);
			mainCell.setCellValue("Plant  Disease Assessment");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
			mainCell = headerRow.createCell(7);
			mainCell.setCellValue("Crop growth stage");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
			mainCell = headerRow.createCell(8);
			mainCell.setCellValue("Sowing land(Ha)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));
			mainCell = headerRow.createCell(9);
			mainCell.setCellValue("Total area infected (Ha)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 1, 10, 10));
			mainCell = headerRow.createCell(10);
			mainCell.setCellValue("Total area controlled  (Ha)");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 13));
			mainCell = headerRow.createCell(11);
			mainCell.setCellValue("Numbers of farmers participated on spraying");
			mainCell.setCellStyle(headerCellStyle);

			List<FungicideMaster> fungicide = fungicideDetailsRepository.fetchAllActiveFungicides();
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, fungicide.size() + 13));
			mainCell = headerRow.createCell(14);
			mainCell.setCellValue("Type & am't of fungicide used (kg(lit)");
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
			subCell.setCellValue("Incidence (%)");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(6);
			subCell.setCellValue("Severity (%)");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(7);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(8);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(9);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(10);
			subCell.setCellValue("");
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(11);
			subCell.setCellValue(WrsisPortalConstant.MALE);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(12);
			subCell.setCellValue(WrsisPortalConstant.FEMALE);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(13);
			subCell.setCellValue("Total");
			subCell.setCellStyle(headerCellStyle);

			int runningCol = 13;
			int fungicount = 1;
			// fetch all active Fungicide
			List<FungicideMaster> fungicides = fungicideDetailsRepository.fetchAllActiveFungicides();
			for (int fungi = 0; fungi < fungicides.size(); fungi++) {

				subCell = subrow.createCell(runningCol + fungicount);
				subCell.setCellValue(fungicides.get(fungi).getFungicideName());
				subCell.setCellStyle(headerCellStyle);

				fungicount = fungicount + 1;

			}

			JSONArray jsonArray = new JSONArray(jsonData);
			int count = 2;
			for (int i = 0; i < jsonArray.length(); i++) {

				JSONObject jsonRegObject = new JSONObject();
				jsonRegObject = jsonArray.getJSONObject(i);
				Row row1 = sheet.createRow(i + count);
				Cell cell = row1.createCell(0);
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

					cell = row1.createCell(1);
					cell.setCellValue(jsonZoneObject.getString("zonename"));
					cell.setCellStyle(cellStyle);

					JSONArray jsonDtlsArr = (JSONArray) jsonZoneObject.get("finalcounts");
					for (int dtls = 0; dtls < jsonDtlsArr.length(); dtls++) {
						JSONObject jsonDtlsObject = new JSONObject();
						jsonDtlsObject = jsonDtlsArr.getJSONObject(dtls);

						cell = row1.createCell(2);
						cell.setCellValue(jsonDtlsObject.getString("kebels"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(3);
						cell.setCellValue(jsonDtlsObject.getString("rust"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(4);
						cell.setCellValue(jsonDtlsObject.getString("variety"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(5);
						cell.setCellValue(jsonDtlsObject.getString("incidence"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(6);
						cell.setCellValue(jsonDtlsObject.getString("severity"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(7);
						cell.setCellValue(jsonDtlsObject.getString("stages"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(8);
						cell.setCellValue(jsonDtlsObject.getString("showingland"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(9);
						cell.setCellValue(jsonDtlsObject.getString("totalinfectedarea"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(10);
						cell.setCellValue(jsonDtlsObject.getString("controlledarea"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(11);
						cell.setCellValue(jsonDtlsObject.getString("male"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(12);
						cell.setCellValue(jsonDtlsObject.getString("female"));
						cell.setCellStyle(cellStyle);

						cell = row1.createCell(13);
						cell.setCellValue(jsonDtlsObject.getString("totalfamer"));
						cell.setCellStyle(cellStyle);

						JSONArray jsonFungiArr = (JSONArray) jsonDtlsObject.get("fungiceideused");
						int cellcout = 14;
						List<FungicideMaster> fungilist = fungicideDetailsRepository.fetchAllActiveFungicides();
						for (int fungM = 0; fungM < fungilist.size(); fungM++) {
							int fungiarrcount = 1;
							for (int fungi = 0; fungi < jsonFungiArr.length(); fungi++) {
								Boolean countstatus = false;
								JSONObject jsonFungiObject = new JSONObject();
								jsonFungiObject = jsonFungiArr.getJSONObject(fungi);
								if (fungilist.get(fungM).getFungicideId() == jsonFungiObject.getInt("ftypeid")) {
									cell = row1.createCell(cellcout);
									cell.setCellValue(jsonFungiObject.getString("fused"));
									cell.setCellStyle(cellStyle);

									cellcout++;

									countstatus = true;
								} else {
									cell = row1.createCell(cellcout);
									cell.setCellValue("0");
									cell.setCellStyle(cellStyle);
									if (jsonFungiArr.length() == fungiarrcount) {

										cellcout++;
									}
								}
								if (countstatus) {
									break;
								}
								fungiarrcount++;
							}
						}

					}

				}

			}

		} catch (Exception e) {
			LOG.error("ImplementationReportExcel::buildExcelImplementationReportDetails():" + e.getMessage());

		}

		return workbook;
	}

}
