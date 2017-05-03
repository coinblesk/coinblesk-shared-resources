package com.coinblesk.util;

public class MissingFieldException extends RuntimeException {

	public MissingFieldException(String fieldName) {
		super("Field " + fieldName + " is missing");
	}

}
