package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.IvrDataReportBean;
import com.csmpl.wrsis.webportal.entity.Question;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class IvrPDF {

	public static final Logger LOG = LoggerFactory.getLogger(IvrPDF.class);

	public ByteArrayInputStream generateIVRPdf(List<IvrDataReportBean> beans, List<Question> qsList) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ArrayList<String> cells = new ArrayList<>();
		cells.add(WrsisPortalConstant.SLNO);
		cells.add("Uploaded Date");
		cells.add("Phone No.");
		cells.add("Call Date Time");
		cells.add(WrsisPortalConstant.REGION);
		cells.add(WrsisPortalConstant.ZONE);
		cells.add(WrsisPortalConstant.WOREDA);

		
		

		for (int i = 0; i < qsList.size(); i++) {
			int j = i + 1;
			cells.add("Question " + j);

		}
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("IVR Data",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);

			PdfPTable table = new PdfPTable(cells.size()); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			// Set Column widths


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
				IvrDataReportBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getUploadedDateView());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getPhnNo());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getCallDateView());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRegion());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getZone());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getWoreda());
				table.addCell(cell1);
				for (int j = 0; j < sdb.getIvrQuestionsData().size(); j++) {
					cell1 = getPdfPCellOject(sdb.getIvrQuestionsData().get(j));
					table.addCell(cell1);
				}

			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("IvrPDF::generateIVRPdf():" + e.getMessage());
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
