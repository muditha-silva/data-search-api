package com.maersk.search.api.exception;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.maersk.search.api.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = IllegalArgumentException.class)
	private ResponseEntity<ErrorResponse> handleException(IllegalArgumentException e) {

		LOG.warn(e.getMessage());

		var errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(value = ResourceNotFoundException.class)
	private ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e) {

		LOG.warn(e.getMessage());

		var errorResponse = new ErrorResponse(e.getMessage(), LocalDateTime.now());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}

}