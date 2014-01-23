package fr.jeromesengel.documentgenerator.document;

import java.io.File;

import fr.jeromesengel.documentgenerator.common.ExportType;

public interface DocumentService {
	File generateDocument(String html, String filename, ExportType exportType);
}