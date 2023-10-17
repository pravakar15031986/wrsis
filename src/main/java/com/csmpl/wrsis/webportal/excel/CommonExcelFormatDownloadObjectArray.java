package com.csmpl.wrsis.webportal.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class CommonExcelFormatDownloadObjectArray extends AbstractXlsView {

	public static final Logger LOG = LoggerFactory.getLogger(CommonExcelFormatDownloadObjectArray.class);

	@Override
	@SuppressWarnings("unchecked")
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			Map<String, Object> map = new HashMap<>();
			map = (Map<String, Object>) model.get("VIEW_DATA_LIST");
			List<Object[]> viewDataList = (List<Object[]>) map.get("ExcelContent");
			String reportName = (String) map.get("ReportName");
			String[] columns = (String[]) map.get("Columns");

			HSSFSheet realSheet = ((HSSFWorkbook) workbook).createSheet(reportName);

			realSheet.setDefaultColumnWidth(12);
			HSSFRow row = realSheet.createRow(0);

			HSSFCell cell = null;

			cell = row.createCell(0);
			cell.setCellValue("Sl. No.");

			for (int i = 1; i <= columns.length; i++) {

				cell = row.createCell(i);
				cell.setCellValue(columns[i - 1]);
			}

			Integer i = 1;

			for (Object[] arr : viewDataList) {
				row = realSheet.createRow(i);

				cell = row.createCell(0);
				cell.setCellValue(i);
				for (int j = 1; j <= arr.length; j++) {
					cell = row.createCell(j);
					cell.setCellValue(String.valueOf(arr[j - 1]));
				}
				i++;

			}
		}

		catch (Exception e) {

			LOG.error("CommonExcelFormatDownloadObjectArray::buildExcelDocument():" + e.getMessage());
		}

	}

}
