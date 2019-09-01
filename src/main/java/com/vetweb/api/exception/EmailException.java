package com.vetweb.api.exception;

public class EmailException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailException(String errorMessage) {
		super(errorMessage);
	}

}
