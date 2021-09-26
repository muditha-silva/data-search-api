package com.maersk.search.api.exception;

public class InternalServerErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6708623914592947803L;

	public InternalServerErrorException(String message) {
		super(message);
	}

}