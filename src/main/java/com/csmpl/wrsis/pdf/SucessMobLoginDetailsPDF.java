package com.csmpl.wrsis.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csmpl.adminconsole.webportal.bean.IPTrackBean;
import com.csmpl.adminconsole.webportal.util.WrsisPortalConstant;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SucessMobLoginDetailsPDF {

	private static final Logger LOG = LoggerFactory.getLogger(SucessMobLoginDetailsPDF.class);

	public ByteArrayInputStream generateMobLoginDetails(List<IPTrackBean> beans) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] cells = { "Sl No", "Org Name", "User ID", "User Name", "Designation", "Role Name", "R.C. Name",
				"IP Address", "OS Name", "Device Id", "Subscriber Id", "IMEI No.", "Login Date", "Status",
				"Logout Date" };

		Document document = new Document();
		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();



			Paragraph date = new Paragraph("Date : " + new SimpleDateFormat(WrsisPortalConstant.DATE_FORMAT_DDMMMYYYY).format(new Date()),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK));
			date.setAlignment(Element.ALIGN_RIGHT);
			document.add(date);

			Paragraph head = new Paragraph("Mobile Login Details",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD, BaseColor.BLACK));
			head.setAlignment(Element.ALIGN_CENTER);

			document.add(head);

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
				IPTrackBean sdb = beans.get(i);
				PdfPCell cell1 = getPdfPCellOject(Integer.toString(i + 1));
				table.addCell(cell1);
				cell1 = getPdfPCellOject(sdb.getOrganizationName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getUserId());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getUserName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDesignation());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getRole());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getResearchcenterName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getIpAddress());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getOsName());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getDeviceId());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getSubscriberId());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getImeiNo());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getLoginTime());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getStatus());
				table.addCell(cell1);

				cell1 = getPdfPCellOject(sdb.getLogoutTime());
				table.addCell(cell1);

			}

			// To avoid having the cell border and the content overlap, if you are having
			// thick cell borders


			document.add(table);

			document.close();
			writer.close();
		} catch (Exception e) {
			LOG.error("SucessMobLoginDetailsPDF::generateMobLoginDetails():" + e.getMessage());
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
