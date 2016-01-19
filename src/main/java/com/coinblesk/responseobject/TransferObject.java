package com.coinblesk.responseobject;

import com.coinblesk.JsonConverter;



public class TransferObject {
	
	public enum Status {
		REQUEST, REPLY_SUCCESS, REPLY_FAILED;
	}

	private Status status = Status.REQUEST;
	private Integer version = 2;
	private String message = null;
	
	public boolean isSuccessful() {
		return status == Status.REPLY_SUCCESS;
	}

	public void setSuccessful(boolean successful) {
		this.status = successful ? Status.REPLY_SUCCESS : Status.REPLY_FAILED;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}
	
	public String toJson() {
	    return JsonConverter.toJson(this);
	}
	
}