package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RustIncidenceSeverityReportPdf {

	public static final Logger LOG = LoggerFactory.getLogger(RustIncidenceSeverityReportPdf.class);

	public ByteArrayInputStream generateRustIncidenceSeverityReportPdf(String dataResp) throws JSONException {

		

		ByteArrayOutputStream out = new ByteArrayOutputStream();




		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Rust Rust Incidence Severity",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);
			PdfPTable table = new PdfPTable(5);


			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(2f); // Space before table
			table.setSpacingAfter(2f); // Space after table



			PdfPCell cell = new PdfPCell(new Phrase(WrsisPortalConstant.REGION));


			cell.setRowspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(WrsisPortalConstant.ZONE));
			
			cell.setRowspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Yellow Rust"));
			
			cell.setRowspan(2);
			PdfPCell cell1 = new PdfPCell(new Phrase("Incidence"));
			table.addCell(cell1);
			table.addCell("Incidence");
			table.addCell("Severity");

			

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("RustIncidenceSeverityReportPdf::generateRustIncidenceSeverityReportPdf():" + e.getMessage());
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
