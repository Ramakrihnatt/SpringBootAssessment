package com.service.exception;

public class NoDataFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 8140673428541586499L;

	public NoDataFoundException() {
	}

	public NoDataFoundException(String message) {
		super(message);
	}
}