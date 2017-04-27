package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountForgotVerifyDTO {

	@NotNull
	private String email;

	@NotNull
	private String token;

	@NotNull
	private String newPassword;

}
