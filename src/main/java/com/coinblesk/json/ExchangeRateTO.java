/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;



import com.coinblesk.util.Currency;
import java.util.HashMap;
import java.util.Map;


public class ExchangeRateTO extends BaseTO<KeyTO> {
    
    private Map<Currency, String> exchangeRates;
    
    public ExchangeRateTO() {
        this.exchangeRates = new HashMap<Currency, String>();
    }

    public Map<Currency, String> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(Map<Currency, String> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public String getExchangeRate(Currency currency) {
        return exchangeRates.get(currency);
    }

    public void setExchangeRate(Currency currency, String exchangeRate) {
        exchangeRates.put(currency, exchangeRate);
    }
    
    
    
}

