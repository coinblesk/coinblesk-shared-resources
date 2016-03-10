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
public class VerifyTO extends BaseTO<VerifyTO> {

    private byte[] fullSignedTransaction; //input
    
    public VerifyTO fullSignedTransaction(byte[] fullSignedTransaction) {
        this.fullSignedTransaction = fullSignedTransaction;
        return this;
    }

    public byte[] fullSignedTransaction() {
        return fullSignedTransaction;
    }
}
