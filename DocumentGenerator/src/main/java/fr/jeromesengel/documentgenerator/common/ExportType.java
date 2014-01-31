package fr.jeromesengel.documentgenerator.common;

public enum ExportType {

	PDF("pdf", "application/pdf"), //
	DOC("doc", "application/msword"), //
	DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

	private String extension;
	private String contentType;

	private ExportType(String extension, String contentType) {
		this.extension = extension;
		this.contentType = contentType;
	}

	public String getExtension() {
		return extension;
	}

	public String getContentType() {
		return contentType;
	}
}