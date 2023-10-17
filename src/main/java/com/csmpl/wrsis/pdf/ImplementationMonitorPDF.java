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

public class ImplementationMonitorPDF {

	public static final Logger LOG = LoggerFactory.getLogger(ImplementationMonitorPDF.class);

	public ByteArrayInputStream generateImplementationMonitorPdf(List<SurveyImplementationBean> beans)
			throws JSONException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();


		ArrayList<String> cells = new ArrayList<>();
		cells.add("Sl No");
		cells.add("Monitor Ref No.");
		cells.add("Monitor Created Date");
		cells.add("Recommendationa No.");
		cells.add(WrsisPortalConstant.REGION);
		cells.add(WrsisPortalConstant.ZONE);
		cells.add(WrsisPortalConstant.WOREDA);
		cells.add("No of PAs Affected");
		cells.add("Total Area Infected(ha)");
		cells.add("Total Area Controlled(ha)");
		cells.add("Total Area Controlled(%)");
		cells.add("Total Fungicides Used (kg(lit))");
		cells.add(WrsisPortalConstant.MALE);
		cells.add(WrsisPortalConstant.FEMALE);
		cells.add("M/F Total");

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Monitor Implementation",
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
				SurveyImplementationBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getMonitorno());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getMonitordate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRecomno());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRegion());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getZone());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getWoreda());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sdb.getNopaeffected()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getTotalAreaInfested());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getTotalControlledha());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getTotalControlledpercent());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getTotalFungicidesUsed());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sdb.getMalefarmer()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sdb.getFemalefarmer()));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(String.valueOf(sdb.getTotalfarmer()));
				table.addCell(cell1);

			}

			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("ImplementationMonitorPDF::generateImplementationMonitorPdf():" + e.getMessage());
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
