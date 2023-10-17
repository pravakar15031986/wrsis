package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SampleTagBean;
import com.csmpl.wrsis.webportal.bean.UserBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportPDF {

	public static final Logger LOG = LoggerFactory.getLogger(ReportPDF.class);

	public ByteArrayInputStream generateReportPdf(List<SampleTagBean> beans) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl No", "Survey No", "Sample ID", "Rust Type", "Survey Date", "Inoculation Date", WrsisPortalConstant.REGION,
				WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA, WrsisPortalConstant.KEBELE, "Laboratory Name", "Race Published Date", "Race Name" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Paragraph date = new Paragraph(WrsisPortalConstant.DATE_SEM + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			

			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths


			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				SampleTagBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSurveyNo());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSampleValue());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getRustType());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSurveyDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getInoculationDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRegion());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getZone());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getWoreda());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getKebel());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getResearchCenter());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRacePublishDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRaceResult());
				table.addCell(cell1);

			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ReportPDF::generateReportPdf():" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());

	}

	public ByteArrayInputStream generateRaceAnalysisStatusPdf(List<SampleTagBean> beans) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl No", "Survey No", "Sample ID", "Rust Type", "Survey Date", "Inoculation Date", WrsisPortalConstant.REGION,
				WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA, WrsisPortalConstant.KEBELE, "Laboratory Name", "Race Published Date", "Race Name", "Status" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Paragraph date = new Paragraph(WrsisPortalConstant.DATE_SEM + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);



			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths


			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				SampleTagBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSurveyNo());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSampleValue());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getRustType());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSurveyDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getInoculationDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRegion());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getZone());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getWoreda());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getKebel());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getResearchCenter());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRacePublishDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRaceResult());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getStatus());
				table.addCell(cell1);

			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders
			

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ReportPDF::generateRaceAnalysisStatusPdf():" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());

	}

	private PdfPCell getPdfPCellOject(String string) {
		PdfPCell cell1 = new PdfPCell(
				new Paragraph(string, FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK)));
		cell1.setBorderColor(BaseColor.BLACK);
		cell1.setPaddingLeft(6);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell1;
	}

	public ByteArrayInputStream buildPathologistReportPdf(List<UserBean> beans) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl#", "Research Center Name", "Pathologist Name", WrsisPortalConstant.GENDER, "Designation", "Role", WrsisPortalConstant.MOBILE,
				WrsisPortalConstant.EMAIL };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Paragraph date = new Paragraph(WrsisPortalConstant.DATE_SEM + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);


			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths
			

			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				UserBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getOrganizationName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getFullName());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getGenderName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDesignation());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRoleName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getMobile());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getEmail());
				table.addCell(cell1);
			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ReportPDF::buildPathologistReportPdf():" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

	public ByteArrayInputStream buildWoredaExpertReportPdf(List<UserBean> beans) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl#", "Development Agent Name", WrsisPortalConstant.GENDER, "Designation", "Role", WrsisPortalConstant.MOBILE, WrsisPortalConstant.EMAIL,
				"Location" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph(WrsisPortalConstant.DATE_SEM + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);



			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths


			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				UserBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getFullName());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getGenderName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDesignation());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRoleName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getMobile());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getEmail());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDemoName());
				table.addCell(cell1);

			}

			// To avoid having the cell border and the content overlap, if you are having


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ReportPDF::buildWoredaExpertReportPdf():" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());

	}

	public ByteArrayInputStream buildDevelopmentAgentReportPdf(List<UserBean> beans) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl#", "Woreda Expert Name", WrsisPortalConstant.GENDER, "Designation", "Role", WrsisPortalConstant.MOBILE, WrsisPortalConstant.EMAIL,
				"Location" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			Paragraph date = new Paragraph(WrsisPortalConstant.DATE_SEM + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);



			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths


			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);
				cell1.setPaddingLeft(10);
				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				UserBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getFullName());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getGenderName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDesignation());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRoleName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getMobile());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getEmail());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDemoName());
				table.addCell(cell1);

			}

			// To avoid having the cell border and the content overlap, if you are having


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ReportPDF::buildDevelopmentAgentReportPdf():" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

}
