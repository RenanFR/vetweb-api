package com.vetweb.api.advice;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.vetweb.api.exception.EmailException;
import com.vetweb.api.pojo.ApiErrorDTO;

@ControllerAdvice
public class ErrorHandlerAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(EmailException.class)
	protected ResponseEntity<Object> handleEmailException(EmailException emailException, WebRequest request) {
		ApiErrorDTO apiError = new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), emailException.getMessage(), Collections.emptyList());
		return handleExceptionInternal(emailException, apiError, new HttpHeaders(), HttpStatus.resolve(apiError.getStatus()), request);
	}

}
