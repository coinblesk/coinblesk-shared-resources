package com.coinblesk.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UnspentTransactionOutputsDTO {

	@NotNull
	private Long sum;

	@NotNull
	private List<String> list;

}
