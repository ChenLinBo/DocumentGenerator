package fr.jeromesengel.documentgenerator.document;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import fr.jeromesengel.documentgenerator.common.ExportType;
import fr.jeromesengel.documentgenerator.utils.PdfPageEvent;

@Service
public class DocumentServiceImpl implements DocumentService {

	private Logger logger = Logger.getLogger(DocumentService.class);

	@Override
	public File generateDocument(String html, String filename, ExportType exportType) {
		switch (exportType) {
		case PDF:
			return this.generatePdf(html, filename);
		case DOC:
		case DOCX:
		default:
			return null;
		}
	}

	private String transformHtml(String html) {
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

	public File generatePdf(String html, String filename) {
		File file = null;
		OutputStream outputStream = null;
		Document document = null;

		try {
			file = new File(filename);
			outputStream = new FileOutputStream(file);
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
			try {
				outputStream.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}

		return file;
	}

	public File generateDoc(String html, String filename) {
		// Not implemented yet
		return null;
	}
}