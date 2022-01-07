package com.appsdeveloperblog.app.ws.ui.model.response;

public class ErrorRest {
	private ErrorMessage error;

	public ErrorRest() {
	}

	public ErrorRest(ErrorMessage error) {
		this.error = error;
	}

	public ErrorMessage getError() {
		return error;
	}

	public void setError(ErrorMessage error) {
		this.error = error;
	}

}
