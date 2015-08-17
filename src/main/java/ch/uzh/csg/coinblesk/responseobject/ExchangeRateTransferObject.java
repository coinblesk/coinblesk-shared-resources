package ch.uzh.csg.coinblesk.responseobject;

import java.util.HashMap;
import java.util.Map;

import ch.uzh.csg.coinblesk.Currency;


public class ExchangeRateTransferObject extends TransferObject {
    
    private Map<Currency, String> exchangeRates;
    
    public ExchangeRateTransferObject() {
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
