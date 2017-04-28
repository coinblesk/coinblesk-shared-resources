package com.coinblesk.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ForexDTO {

	private String currencyFrom;
	private String currencyTo;
	private BigDecimal rate;
	private Date updatedAt;

}
