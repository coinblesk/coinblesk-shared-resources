package com.coinblesk.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString(exclude = "password")
public class LoginDTO {

	@NotNull
	private String email;

	@NotNull
	private String password;

}
