package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
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

public class SurveyorDetailsPDF {

	public static final Logger LOG = LoggerFactory.getLogger(SurveyorDetailsPDF.class);

	public ByteArrayInputStream generateSurveyorDetailsPdf(List<UserBean> beans) throws JSONException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
	

		ArrayList<String> cells = new ArrayList<>();
		cells.add("Sl No");
		cells.add("Research Center Name");
		cells.add("Surveyor Name");
		cells.add("Gender");
		cells.add("Designation");
		cells.add("Role");
		cells.add("Mobile");
		cells.add("Email");

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Surveyor Details",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);

			PdfPTable table = new PdfPTable(cells.size()); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			for (int i = 0; i < cells.size(); i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells.get(i),
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

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("SurveyorDetailsPDF::generateSurveyorDetailsPdf():" + e.getMessage());
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

}
