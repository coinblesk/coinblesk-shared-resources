/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import org.bitcoinj.core.Address;

/**
 *
 * @author Thomas Bocek
 */
public class CompleteSignTO extends BaseTO<CompleteSignTO> {

    private byte[] fullSignedTransaction;
    private String p2shAddressTo;

    public CompleteSignTO fullSignedTransaction(byte[] fullSignedTransaction) {
        this.fullSignedTransaction = fullSignedTransaction;
        return this;
    }

    public byte[] fullSignedTransaction() {
        return fullSignedTransaction;
    }
    
    public CompleteSignTO p2shAddressTo(String p2shAddressTo) {
        this.p2shAddressTo = p2shAddressTo;
        return this;
    }

    public String p2shAddressTo() {
        return p2shAddressTo;
    }
}
