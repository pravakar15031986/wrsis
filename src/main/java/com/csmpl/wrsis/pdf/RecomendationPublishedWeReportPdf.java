package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.csmpl.wrsis.webportal.bean.SurveyImplementationBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RecomendationPublishedWeReportPdf {

	public static final Logger LOG = LoggerFactory.getLogger(RecomendationPublishedWeReportPdf.class);

	public ByteArrayInputStream generateRecomendationPublishedSurveyDataDetailsPDF(
			List<SurveyImplementationBean> beans) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl No", "Monitor Ref No.", "Monitor Date", "Recommendation No.", WrsisPortalConstant.REGION, WrsisPortalConstant.ZONE, WrsisPortalConstant.WOREDA,
				"No of PAs Affected", "Total Area Infected(ha)", "Total Area Controlled(ha)",
				"Total Area Controlled(%)", "Total Fungicides Used (kg(lit))", "Male worker", "Female worker",
				"Total worker" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Recomendation  Data",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);

			PdfPTable table = new PdfPTable(cells.length); // 3 columns.
			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(10f); // Space before table
			table.setSpacingAfter(10f); // Space after table

			for (int i = 0; i < cells.length; i++) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(cells[i],
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK)));
				cell1.setBorderColor(BaseColor.BLACK);

				cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell1.setBorderWidth(2);

				table.addCell(cell1);
			}

			for (int i = 0; i < beans.size(); i++) {
				SurveyImplementationBean sib = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				

				cell1 = getPdfPCellOject(sib.getMonitorno());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getMonitordate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getRecomno());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getRegion());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getZone());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getWoreda());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sib.getNopaeffected()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getTotalAreaInfested());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getTotalControlledha());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getTotalControlledpercent());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sib.getTotalFungicidesUsed());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sib.getMalefarmer()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sib.getFemalefarmer()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sib.getTotalfarmer()));
				table.addCell(cell1);

			}

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("RecomendationPublishedWeReportPdf::generateRecomendationPublishedSurveyDataDetailsPDF():"
					+ e.getMessage());
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
