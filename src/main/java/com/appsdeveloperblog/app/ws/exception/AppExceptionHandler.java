package com.appsdeveloperblog.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessage;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorRest;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value = { UserServiceException.class })
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex, WebRequest request) {

		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		// return new ResponseEntity<Object>(ex/* .getMessage() */, new HttpHeaders(),
		// HttpStatus.BAD_REQUEST);
		ErrorRest errorRest = new ErrorRest(errorMessage);

		return new ResponseEntity<Object>(errorRest, new HttpHeaders(), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleOtherException(Exception ex, WebRequest request) {

		ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
		// return new ResponseEntity<Object>(ex/* .getMessage() */, new HttpHeaders(),
		// HttpStatus.BAD_REQUEST);
		ErrorRest errorRest = new ErrorRest(errorMessage);

		return new ResponseEntity<Object>(errorRest, new HttpHeaders(), HttpStatus.BAD_REQUEST);

	}
}
