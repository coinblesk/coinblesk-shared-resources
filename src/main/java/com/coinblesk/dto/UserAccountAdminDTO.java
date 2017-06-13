package com.coinblesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserAccountAdminDTO {

	private String email;
	private long balance;
	private boolean deleted;
	private String userRole;
	private Date creationDate;
	private String accountPublicKeyClient;

}
