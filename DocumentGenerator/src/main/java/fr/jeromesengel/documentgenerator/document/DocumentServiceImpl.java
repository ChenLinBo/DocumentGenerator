package fr.jeromesengel.documentgenerator.document;

import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import fr.jeromesengel.documentgenerator.common.ExportType;
import fr.jeromesengel.documentgenerator.utils.DocxGenerationStrategy;
import fr.jeromesengel.documentgenerator.utils.PdfGenerationStrategy;

@Service
public class DocumentServiceImpl implements DocumentService {

	private static Logger logger = Logger.getLogger(DocumentService.class);
	private DocumentGenerator documentGenerator;

	@Override
	public OutputStream generateDocument(String html, ExportType exportType) {
		documentGenerator = new DocumentGenerator();

		switch (exportType) {
		case PDF:
			documentGenerator.setDocumentGenerationStrategy(new PdfGenerationStrategy());
			break;
		case DOCX:
			documentGenerator.setDocumentGenerationStrategy(new DocxGenerationStrategy());
			break;
		case DOC:
		default:
			logger.error("Type de fichier non reconnu");
			break;
		}

		return documentGenerator.generateDocument(html);
	}
}