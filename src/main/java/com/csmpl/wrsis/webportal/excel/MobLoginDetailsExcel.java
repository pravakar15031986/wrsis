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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;

public class MobLoginDetailsExcel {
	private static final Logger LOG = LoggerFactory.getLogger(MobLoginDetailsExcel.class);

	public HSSFWorkbook buildingMobLogindDetails(List<IPTrackBean> beansList) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			Sheet sheet = workbook.createSheet("Details");
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 6000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 6000);
			sheet.setColumnWidth(8, 6000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 6000);
			sheet.setColumnWidth(11, 6000);
			sheet.setColumnWidth(12, 6000);
			sheet.setColumnWidth(13, 6000);
			sheet.setColumnWidth(14, 6000);

			Font hearderFont = workbook.createFont();
			hearderFont.setBold(true);
			hearderFont.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(hearderFont);
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
			columnCell.setCellValue("Org Name");

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
			columnCell.setCellValue("Role Name");

			columnCell = columnRow.createCell(6);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("R.C. Name");

			columnCell = columnRow.createCell(7);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("IP Address");

			columnCell = columnRow.createCell(8);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("OS Name");

			columnCell = columnRow.createCell(9);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("Device Id");

			columnCell = columnRow.createCell(10);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("Subscriber Id");

			columnCell = columnRow.createCell(11);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("IMEI No.");

			columnCell = columnRow.createCell(12);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("Login Date");

			columnCell = columnRow.createCell(13);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("Login Status");

			columnCell = columnRow.createCell(14);
			columnCell.setCellStyle(headerCellStyle);
			columnCell.setCellValue("Logout Date");

			for (int i = 0; i < beansList.size(); i++) {
				IPTrackBean bean = beansList.get(i);
				Row row = sheet.createRow(i + 1);
				Cell cell = row.createCell(0);
				cell.setCellValue(i + 1);

				cell = row.createCell(1);
				cell.setCellValue(bean.getOrganizationName());

				cell = row.createCell(2);
				cell.setCellValue(bean.getUserId());

				cell = row.createCell(3);
				cell.setCellValue(bean.getUserName());

				cell = row.createCell(4);
				cell.setCellValue(bean.getDesignation());

				cell = row.createCell(5);
				cell.setCellValue(bean.getRole());

				cell = row.createCell(6);
				cell.setCellValue(bean.getResearchcenterName());

				cell = row.createCell(7);
				cell.setCellValue(bean.getIpAddress());

				cell = row.createCell(8);
				cell.setCellValue(bean.getOsName());

				cell = row.createCell(9);
				cell.setCellValue(bean.getDeviceId());

				cell = row.createCell(10);
				cell.setCellValue(bean.getSubscriberId());

				cell = row.createCell(11);
				cell.setCellValue(bean.getImeiNo());

				cell = row.createCell(12);
				cell.setCellValue(bean.getLoginTime());

				cell = row.createCell(13);
				cell.setCellValue(bean.getStatus());

				cell = row.createCell(14);
				cell.setCellValue(bean.getLogoutTime());

			}

		} catch (Exception e) {
			LOG.error("ImplementationReportExcel::buildingMobLogindDetails():" + e.getMessage());

		}

		return workbook;
	}
}
