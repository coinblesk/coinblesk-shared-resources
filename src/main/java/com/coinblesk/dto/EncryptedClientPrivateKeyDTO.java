package com.coinblesk.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EncryptedClientPrivateKeyDTO {

	@NotNull
	private String encryptedClientPrivateKey;

}
