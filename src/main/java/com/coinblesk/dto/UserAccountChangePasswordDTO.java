package com.coinblesk.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserAccountChangePasswordDTO {

	@NotNull
	private String newPassword;

}
