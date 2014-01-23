package fr.jeromesengel.documentgenerator.utils;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfPageEvent extends PdfPageEventHelper {

	private Logger logger = Logger.getLogger(PdfPageEvent.class);

	/** Footer properties */
	private PdfTemplate totalPageNumber;
	private String footerText;

	/** WaterMark properties */
	private Font font;
	private PdfGState state;
	private String waterMarkText = "";
	private int waterMarkLocation = 1;

	public PdfPageEvent(String footerText, String waterMarkText, int waterMarkLocation) {
		super();
		this.footerText = footerText;
		this.waterMarkText = waterMarkText;
		this.waterMarkLocation = waterMarkLocation;

		state = new PdfGState();
		state.setFillOpacity(0.10f);
		state.setStrokeOpacity(0.10f);

		font = new Font(FontFamily.HELVETICA, 60);
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
	 * Method called at the end of each page. Apply the watermark and write the page number in the footer
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {
		this.writeWaterMark(writer, document);
		this.writePageNumber(writer, document);
	}

	private void writeWaterMark(PdfWriter writer, Document document) {
		float x = document.getPageSize().getWidth() / 2;
		float y = document.getPageSize().getHeight() / 2;
		float fontWidth = font.getCalculatedSize();
		BaseFont baseFront = font.getCalculatedBaseFont(false);
		if (baseFront != null) {
			fontWidth = baseFront.getWidthPoint(waterMarkText, font.getCalculatedSize());
		}

		switch (waterMarkLocation) {
		case 0:
			y = document.getPageSize().getTop() - document.topMargin() - (fontWidth / 1.3f);
			break;
		case 1:
			y = document.getPageSize().getHeight() / 2 - (fontWidth / 3);
			break;
		case 2:
			y = document.getPageSize().getBottom() + document.bottomMargin();
			break;
		}

		PdfContentByte contentByte = writer.getDirectContentUnder();
		contentByte.saveState();
		contentByte.setGState(state);

		if (waterMarkText != null && waterMarkText.trim().length() > 0) {
			ColumnText.showTextAligned(contentByte, Element.ALIGN_BOTTOM, new Phrase(waterMarkText, font), x - (fontWidth / 3), y, 45);
		}

		contentByte.restoreState();
	}

	private void writePageNumber(PdfWriter writer, Document document) {
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
			logger.error(e);
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