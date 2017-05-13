package com.coinblesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GeneralErrorDTO {
	private Date timestamp;
	private int status;
	private String error;
	private String exception;
	private String message;
	private String method;
	private String path;
}