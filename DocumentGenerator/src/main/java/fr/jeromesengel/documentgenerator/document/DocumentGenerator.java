package fr.jeromesengel.documentgenerator.document;

import java.io.OutputStream;

import fr.jeromesengel.documentgenerator.utils.DocumentGenerationStrategy;

public class DocumentGenerator {

	private DocumentGenerationStrategy documentGenerationStrategy;

	public DocumentGenerator() {
	}

	public DocumentGenerator(DocumentGenerationStrategy documentGenerationStrategy) {
		this.documentGenerationStrategy = documentGenerationStrategy;
	}

	public OutputStream generateDocument(String html) {
		if (documentGenerationStrategy != null) {
			return documentGenerationStrategy.generateDocument(html);
		} else {
			return null;
		}
	}

	public DocumentGenerationStrategy getDocumentGenerationStrategy() {
		return documentGenerationStrategy;
	}

	public void setDocumentGenerationStrategy(DocumentGenerationStrategy documentGenerationStrategy) {
		this.documentGenerationStrategy = documentGenerationStrategy;
	}
}