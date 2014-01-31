package fr.jeromesengel.documentgenerator.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfGenerationStrategy implements DocumentGenerationStrategy {

	private static Logger logger = Logger.getLogger(PdfGenerationStrategy.class);

	@Override
	public String transformHtml(String html) {
		// On entoure le code HTML par une balise <div> afin d'éviter l'erreur "the document has no pages"
		// Cette erreur se produit lorsque : StringUtils.isEmpty(html) == true
		html = "<div>" + html + "</div>";

		// L'espacement des paragraphes ne semble pas être géré naturellement par iText, donc on le code en dur
		html = html.replaceAll("<p style=\"", "<p style=\"margin: 10px 0; ");
		html = html.replaceAll("<p>", "<p style=\"margin: 10px 0;\">");

		// Gestion des pagebreak générés par l'éditeur TinyMCE
		html = html.replaceAll("<p style=\"(.*)\"><!-- pagebreak --></p>", "<p style=\"page-break-after:always; $1\"><!-- pagebreak --></p>");

		return html;
	}

	@Override
	public OutputStream generateDocument(String html) {
		OutputStream outputStream = null;
		Document document = null;

		try {
			outputStream = new ByteArrayOutputStream();
			document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));
			writer.setPageEmpty(true);
			writer.setPageEvent(new PdfPageEvent("DEC133017DGDS", "Ippon Technologies", 1));

			document.open();
			document.newPage();

			InputStream inputStream = new ByteArrayInputStream(this.transformHtml(html).getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);

		} catch (Exception e) {
			logger.error(e);

		} finally {
			document.close();
			IOUtils.closeQuietly(outputStream);
		}

		return outputStream;
	}
}