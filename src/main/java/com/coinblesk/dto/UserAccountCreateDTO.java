package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountCreateDTO {

	@NotNull
	private String email;

	@NotNull
	private String password;

}
