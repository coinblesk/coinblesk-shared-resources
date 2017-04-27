package com.coinblesk.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserAccountAdminDTO {

	private String email;
	private long balance;
	private boolean deleted;
	private String userRole;
	private Date creationDate;

}
