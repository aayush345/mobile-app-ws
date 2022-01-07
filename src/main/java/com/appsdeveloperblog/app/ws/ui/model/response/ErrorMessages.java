package com.appsdeveloperblog.app.ws.ui.model.response;

public enum ErrorMessages {

	RECORD_ALREADY_EXISTS("Record with this email-id already exists."),
	RECORD_NOT_FOUND("Record with this user-id does not exist."),
	MISSING_MANDATORY_FIELD("Please provide value for mandatory fields.");

	private String errorMessage;

	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
