package com.coinblesk.dto;

import java.util.List;

import lombok.Data;

@Data
public class AccountDetailsDTO {

	private AccountDTO account;
	private List<TimeLockedAddressDTO> timeLockedAddresses;

}
