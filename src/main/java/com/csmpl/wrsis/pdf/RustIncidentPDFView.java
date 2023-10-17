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
import com.csmpl.wrsis.webportal.bean.RustIncidentEntityBean;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class RustIncidentPDFView {

	public static final Logger LOG = LoggerFactory.getLogger(RustIncidentPDFView.class);

	public ByteArrayInputStream generateRustIncidentPdf(List<RustIncidentEntityBean> beans) throws JSONException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		ArrayList<String> cells = new ArrayList<>();
		cells.add("Sl No");
		cells.add("Incident Date");
		cells.add(WrsisPortalConstant.REGION);
		cells.add(WrsisPortalConstant.ZONE);
		cells.add(" Woreda");
		cells.add(" Kebele");
		cells.add(" Year");
		cells.add(" Season");
		cells.add(WrsisPortalConstant.LONGITUDE);
		cells.add(WrsisPortalConstant.LATITUDE);
		cells.add("Incident Submitted By");
		List<String> questionJsa = new ArrayList<>();
		for (int i = 0; i < beans.size(); i++) {
			RustIncidentEntityBean rbean = beans.get(i);
			org.json.JSONArray jsa = null;

			try {
				jsa = new org.json.JSONArray(rbean.getQueostions());
			} catch (Exception e) {
				continue;
			}
			for (int j = 0; j < jsa.length(); j++) {
				if (!questionJsa.contains(jsa.getJSONObject(j).getString("question"))) {

					questionJsa.add(jsa.getJSONObject(j).getString("question"));
				}
			}
		}

		for (int i = 0; i < questionJsa.size(); i++) {
			cells.add(questionJsa.get(i));

		}
		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Rust Incident",
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
				RustIncidentEntityBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getIncidendDate());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRegionName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getZoneName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getWoredaName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getKebeleName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getYear());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSeasonName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getLongitude());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getLatitude());
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getUserFullName());
				table.addCell(cell1);
				try {
					for (int m = 0; m < sdb.getQuestionsData().size(); m++) {
						cell1 = getPdfPCellOject(sdb.getQuestionsData().get(m));
						table.addCell(cell1);

					}
				} catch (Exception e) {
					LOG.error("RustIncidentPDFView::generateRustIncidentPdf():" + e.getMessage());
					for (int ti = 0; ti < questionJsa.size(); ti++) {
						cell1 = getPdfPCellOject("--");
						table.addCell(cell1);

					}

				}
			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("RustIncidentPDFView::generateRustIncidentPdf():" + e.getMessage());
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
