package com.service.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> notFound(UserNotFoundException userNotFoundException) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ErrorMessage(userNotFoundException.getMessage(), LocalDateTime.now(), HttpStatus.NOT_FOUND.name()));
	}

	@ExceptionHandler
	public ResponseEntity<ErrorMessage> noDataFound(NoDataFoundException noDataFoundException) {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
				new ErrorMessage(noDataFoundException.getMessage(), LocalDateTime.now(), HttpStatus.NO_CONTENT.name()));
	}
}