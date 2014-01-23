package fr.jeromesengel.documentgenerator.common;

public enum Page {

	DOCUMENT("document.jsp");

	private String view;

	private Page(String view) {
		this.view = view;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
}