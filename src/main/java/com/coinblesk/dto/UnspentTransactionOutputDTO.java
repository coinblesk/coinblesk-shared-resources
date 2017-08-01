package com.coinblesk.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UnspentTransactionOutputDTO {

	@NotNull
	private Long value;

	@NotNull
	private Integer index;

	@NotNull
	private String transaction;

}
