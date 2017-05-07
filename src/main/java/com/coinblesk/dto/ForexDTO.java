package com.coinblesk.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.coinblesk.enumerator.ForexCurrency;

import lombok.Data;

@Data
public class ForexDTO {

	private ForexCurrency currencyFrom;
	private ForexCurrency currencyTo;
	private BigDecimal rate;
	private Date updatedAt;

}
