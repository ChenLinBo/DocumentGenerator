package fr.jeromesengel.documentgenerator.document;

import java.io.OutputStream;

import fr.jeromesengel.documentgenerator.common.ExportType;

public interface DocumentService {
	OutputStream generateDocument(String html, ExportType exportType);
}