package fr.jeromesengel.documentgenerator.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderAndFooter extends PdfPageEventHelper {
	private PdfTemplate totalPageNumber;
	private String footerText;

	public HeaderAndFooter(String footerText) {
		super();
		this.footerText = footerText;
	}

	/**
	 * Creates the PdfTemplate that will hold the total number of pages.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		totalPageNumber = writer.getDirectContent().createTemplate(30, 16);
	}

	/**
	 * Adds the footer.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setTotalWidth(527);
			table.setLockedWidth(true);
			table.setWidths(new float[] { 10f, 2f, 1f });

			PdfPCell cell1 = new PdfPCell(new Paragraph(footerText));
			cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell1.setBorder(Rectangle.NO_BORDER);

			PdfPCell cell2 = new PdfPCell(new Paragraph("Page " + writer.getPageNumber() + " / "));
			cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell2.setBorder(Rectangle.NO_BORDER);

			PdfPCell cell3 = new PdfPCell(Image.getInstance(totalPageNumber));
			cell3.setBorder(Rectangle.NO_BORDER);

			table.addCell(cell1);
			table.addCell(cell2);
			table.addCell(cell3);

			table.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());

		} catch (Exception e) {
		}
	}

	/**
	 * Fills out the total number of pages before the document is closed.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(totalPageNumber, Element.ALIGN_LEFT, new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);
	}
}