/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

/**
 *
 * @author Thomas Bocek
 */
public class BalanceTO extends BaseTO<BalanceTO> {

    private long balance;

    public BalanceTO balance(long balance) {
        this.balance = balance;
        return this;
    }

    public long balance() {
        return balance;
    }
}
