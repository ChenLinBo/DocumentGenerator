package fr.jeromesengel.documentgenerator.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;

public class DocxGenerationStrategy implements DocumentGenerationStrategy {

	private static Logger logger = Logger.getLogger(DocxGenerationStrategy.class);

	@Override
	public String transformHtml(String html) {
		// On entoure le code HTML par une balise <div> afin d'éviter l'erreur :
		// "Le balisage du document suivant l'élément racine doit avoir un format correct"
		html = "<div>" + html + "</div>";

		// Le caractère &nbsp; ne semble pas être géré
		html = html.replaceAll("&nbsp;", "");

		return html;
	}

	@Override
	public OutputStream generateDocument(String html) {
		OutputStream outputStream = null;

		try {
			outputStream = new ByteArrayOutputStream();
			// Create an empty docx package
			WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

			NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
			wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
			ndp.unmarshalDefaultNumbering();

			XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
			xHTMLImporter.setHyperlinkStyle("Hyperlink");

			// Convert the XHTML, and add it into the empty docx we made
			List<Object> objects = xHTMLImporter.convert(this.transformHtml(html), null);
			wordMLPackage.getMainDocumentPart().getContent().addAll(objects);

			// On effectue des modifications car la transformation XHTML -> DOCX ne peut pas tout faire
			Docx4jGenerationHelper docx4jGenerationHelper = new Docx4jGenerationHelper(wordMLPackage, Context.getWmlObjectFactory());
			docx4jGenerationHelper.createFooterReference(docx4jGenerationHelper.createFooterPart());
			
			logger.debug(XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true));

			Save saver = new Save(wordMLPackage);
			saver.save(outputStream);

		} catch (Exception e) {
			logger.error(e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

		return outputStream;
	}
}