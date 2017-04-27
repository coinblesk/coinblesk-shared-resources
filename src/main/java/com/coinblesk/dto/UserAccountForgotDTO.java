package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountForgotDTO {

	@NotNull
	private String email;

}
