package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TokenDTO {

	@NotNull
	private String token;

}
