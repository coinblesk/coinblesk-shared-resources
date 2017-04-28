package com.coinblesk.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ForexDTO {

	private String currencyFrom;
	private String currencyTo;
	private BigDecimal rate;

}
