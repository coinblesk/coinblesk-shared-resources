/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

public class ExchangeRateTO extends BaseTO<KeyTO> {

    private String name;
    private String rate;

    public String name() {
        return name;
    }

    public ExchangeRateTO name(String name) {
        this.name = name;
        return this;
    }

    public String rate() {
        return rate;
    }

    public ExchangeRateTO rate(String rate) {
        this.rate = rate;
        return this;
    }

}
