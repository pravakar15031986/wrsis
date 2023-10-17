package com.csmpl.wrsis.webportal.excel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

public class ReactiontoRustReportExcel {

	private static final Logger LOG = LoggerFactory.getLogger(ReactiontoRustReportExcel.class);

	public HSSFWorkbook buildExcelReactiontoRustReportDetails(String jsonData)// String jsonData
	{
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// sheet creation
			Sheet sheet = workbook.createSheet("Reaction to Rust Report");
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

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			Cell mainCell = headerRow.createCell(0);
			mainCell.setCellValue("Variety");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			mainCell = headerRow.createCell(1);
			mainCell.setCellValue("TNF");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));
			mainCell = headerRow.createCell(2);
			mainCell.setCellValue("Stem Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
			mainCell = headerRow.createCell(5);
			mainCell.setCellValue("Leaf Rust");
			mainCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 10));
			mainCell = headerRow.createCell(8);
			mainCell.setCellValue("Yellow Rust");
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

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
			subCell = subrow.createCell(2);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(4);
			subCell.setCellValue(WrsisPortalConstant.INCIDENCE);
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
			subCell = subrow.createCell(5);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(7);
			subCell.setCellValue(WrsisPortalConstant.INCIDENCE);
			subCell.setCellStyle(headerCellStyle);

			sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));
			subCell = subrow.createCell(8);
			subCell.setCellValue(WrsisPortalConstant.SEVERITY);
			subCell.setCellStyle(headerCellStyle);

			subCell = subrow.createCell(10);
			subCell.setCellValue(WrsisPortalConstant.INCIDENCE);
			subCell.setCellStyle(headerCellStyle);

			// sub headers
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
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(5);
			subsubCell.setCellValue(WrsisPortalConstant.RANGE);
			subsubCell.setCellStyle(headerCellStyle);

			subsubCell = subsubrow.createCell(6);
			subsubCell.setCellValue("Mean");
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
			subsubCell.setCellValue("Mean");
			subsubCell.setCellStyle(headerCellStyle);

			JSONArray jsonArray = new JSONArray(jsonData);
			int count = 3;
			for (int i = 0; i < jsonArray.length(); i++)//
			{

				JSONObject jsonVariObject = new JSONObject();
				jsonVariObject = jsonArray.getJSONObject(i);
				Row row1 = sheet.createRow(i + count);
				Cell cell = row1.createCell(0);
				if (jsonVariObject.getString("varietyname") != null) {
					cell.setCellValue(jsonVariObject.getString("varietyname"));
					cell.setCellStyle(cellStyle);
				} else {
					cell.setCellValue("");
				}

				cell = row1.createCell(1);
				if (jsonVariObject.getString("tnf") != null) {
					cell.setCellValue(Integer.parseInt((jsonVariObject.getString("tnf"))));
					cell.setCellStyle(cellStyle);
				} else {
					cell.setCellValue(Integer.parseInt((jsonVariObject.getString("0"))));
				}

				JSONArray jsonRustArr = (JSONArray) jsonVariObject.get("rustinfos");
				JSONObject stemRustObject = new JSONObject();
				stemRustObject = jsonRustArr.getJSONObject(0);
				if (!stemRustObject.getString(WrsisPortalConstant.SEV_RANGE).contentEquals("0-"))// stem
				{
					cell = row1.createCell(2);
					cell.setCellValue(stemRustObject.getString(WrsisPortalConstant.SEV_RANGE));

				} else {
					cell = row1.createCell(2);
					cell.setCellValue("0-0");
				}

				Object stemMean = stemRustObject.getString("mean");
				if (!stemMean.toString().contains("null")) {
					cell = row1.createCell(3);
					cell.setCellValue(stemRustObject.getString("mean"));
				} else {
					cell = row1.createCell(3);
					cell.setCellValue("0");
				}

				cell = row1.createCell(4);
				Double smin = 0.0;
				smin = Double.parseDouble(stemRustObject.getString(WrsisPortalConstant.YELCOUNT)) * 100
						/ Integer.parseInt((jsonVariObject.getString("tnf")));
				NumberFormat formatter = new DecimalFormat("#0.00");
				cell.setCellValue(formatter.format(smin));
				cell.setCellStyle(cellStyle);

				JSONObject leafRustObject = new JSONObject();
				leafRustObject = jsonRustArr.getJSONObject(1);
				if (!leafRustObject.getString(WrsisPortalConstant.SEV_RANGE).contentEquals("0-"))// leaf
				{
					cell = row1.createCell(5);
					cell.setCellValue(leafRustObject.getString(WrsisPortalConstant.SEV_RANGE));

				} else {
					cell = row1.createCell(5);
					cell.setCellValue("0-0");
				}

				Object leafMean = leafRustObject.getString("mean");
				if (!leafMean.toString().contains("null")) {
					cell = row1.createCell(6);
					cell.setCellValue(leafRustObject.getString("mean"));
				} else {
					cell = row1.createCell(6);
					cell.setCellValue("0");
				}

				cell = row1.createCell(7);
				Double lmin = 0.0;
				lmin = Double.parseDouble(leafRustObject.getString(WrsisPortalConstant.YELCOUNT)) * 100
						/ Integer.parseInt((jsonVariObject.getString("tnf")));
				NumberFormat formatter1 = new DecimalFormat("#0.00");
				cell.setCellValue(formatter1.format(lmin));
				cell.setCellStyle(cellStyle);

				JSONObject yellowRustObject = new JSONObject();
				yellowRustObject = jsonRustArr.getJSONObject(2);
				if (!yellowRustObject.getString(WrsisPortalConstant.SEV_RANGE).contentEquals("0-"))// yellow
				{
					cell = row1.createCell(8);
					cell.setCellValue(yellowRustObject.getString(WrsisPortalConstant.SEV_RANGE));

				} else {
					cell = row1.createCell(8);
					cell.setCellValue("0-0");
				}

				Object yellowMean = yellowRustObject.getString("mean");
				if (!yellowMean.toString().contains("null")) {
					cell = row1.createCell(9);
					cell.setCellValue(yellowRustObject.getString("mean"));
				} else {
					cell = row1.createCell(9);
					cell.setCellValue("0");
				}

				cell = row1.createCell(10);
				Double ymin = 0.0;
				ymin = Double.parseDouble(yellowRustObject.getString(WrsisPortalConstant.YELCOUNT)) * 100
						/ Integer.parseInt((jsonVariObject.getString("tnf")));
				NumberFormat formatter2 = new DecimalFormat("#0.00");
				cell.setCellValue(formatter2.format(ymin));
				cell.setCellStyle(cellStyle);

			}

		} catch (Exception e) {
			LOG.error("ReactiontoRustReportExcel::buildExcelReactiontoRustReportDetails():" + e.getMessage());
		}

		return workbook;
	}

}
