package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class RustPrevalenceReportPDF {

	public static final Logger LOG = LoggerFactory.getLogger(RustPrevalenceReportPDF.class);

	public ByteArrayInputStream generateRustPrevalenceReportPdf(String dataResp) throws JSONException {

		

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ArrayList<String> cells = new ArrayList<>();
		cells.add(WrsisPortalConstant.REGION);
		cells.add(WrsisPortalConstant.ZONE);
		cells.add("No. of fields assessed");

		ArrayList<String> cells1 = new ArrayList<>();
		cells1.add("Yellow Rust");
		cells.addAll(3, cells1);

		ArrayList<String> cells2 = new ArrayList<>();
		cells2.add("Stem Rust");
		cells.addAll(4, cells2);

		ArrayList<String> cells3 = new ArrayList<>();
		cells3.add("Leaf Rust");
		cells.addAll(5, cells3);

		ArrayList<String> cellsYellow = new ArrayList<>();
		cellsYellow.add(WrsisPortalConstant.INFECTED_FIELD1);
		cellsYellow.add(WrsisPortalConstant.PREV_PERCENTAGE);


		ArrayList<String> cellsStem = new ArrayList<>();
		cellsStem.add(WrsisPortalConstant.INFECTED_FIELD1);
		cellsStem.add(WrsisPortalConstant.PREV_PERCENTAGE);
		

		ArrayList<String> cellsLeaf = new ArrayList<>();
		cellsLeaf.add(WrsisPortalConstant.INFECTED_FIELD1);
		cellsLeaf.add(WrsisPortalConstant.PREV_PERCENTAGE);
		

		String[] myArr = new String[2];
		myArr[0] = WrsisPortalConstant.INFECTED_FIELD1;
		myArr[1] = WrsisPortalConstant.PREV_PERCENTAGE;

		

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
		

			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Rust Prevalence Report",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);
			PdfPTable table = new PdfPTable(9);
			
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(2f); // Space before table
			table.setSpacingAfter(2f); // Space after table
			

			PdfPCell cell = new PdfPCell(new Phrase(WrsisPortalConstant.REGION));
			
			cell.setRowspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(WrsisPortalConstant.ZONE));
			
			cell.setRowspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("No. of fields assessed"));
			
			cell.setRowspan(2);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("Yellow Rust"));
			cell.setColspan(2);
			
			table.addCell(cell);
			table.addCell(WrsisPortalConstant.INFECTED_FIELD1);
			table.addCell(WrsisPortalConstant.PREV_PERCENTAGE);

			cell = new PdfPCell(new Phrase("Stem Rust"));
			cell.setColspan(2);
			
			table.addCell(cell);
			table.addCell(WrsisPortalConstant.INFECTED_FIELD1);
			table.addCell(WrsisPortalConstant.PREV_PERCENTAGE);

			cell = new PdfPCell(new Phrase("Leaf Rust"));
			cell.setColspan(2);
			
			table.addCell(cell);
			table.addCell(WrsisPortalConstant.INFECTED_FIELD1);
			table.addCell(WrsisPortalConstant.PREV_PERCENTAGE);

	

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("RustPrevalenceReportPDF::generateRustPrevalenceReportPdf():" + e.getMessage());
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
