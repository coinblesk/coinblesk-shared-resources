package com.coinblesk.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UserAccountCreateWithTokenDTO {

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	@Pattern(regexp = "[0-9a-fA-F]{66}", message = "publicKey must be 33 bytes long in hex format (string of length 66)")
	private String clientPublicKey;

	@NotNull
	private String clientPrivateKeyEncrypted;

	@NotNull
	private Long lockTime;

	@NotNull
	// necessary if unregistered user wants to claim a received
	// fund and therefore registers with his special token
	private String unregisteredToken;

}
