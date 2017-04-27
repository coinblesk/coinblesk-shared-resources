package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountCreateVerifyDTO {

	@NotNull
	private String email;

	@NotNull
	private String token;

}
