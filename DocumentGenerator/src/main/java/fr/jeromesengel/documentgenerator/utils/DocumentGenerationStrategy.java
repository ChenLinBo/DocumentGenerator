package fr.jeromesengel.documentgenerator.utils;

import java.io.OutputStream;

public interface DocumentGenerationStrategy {
	public String transformHtml(String html);

	public OutputStream generateDocument(String html);
}