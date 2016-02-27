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
public class CompleteSignTO extends BaseTO<CompleteSignTO> {

    private byte[] fullSignedTransaction;
    private byte[] clientPublicKey;
    private byte[] merchantPublicKey;

    public CompleteSignTO fullSignedTransaction(byte[] fullSignedTransaction) {
        this.fullSignedTransaction = fullSignedTransaction;
        return this;
    }

    public byte[] fullSignedTransaction() {
        return fullSignedTransaction;
    }

    public CompleteSignTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
    
    public CompleteSignTO merchantPublicKey(byte[] merchantPublicKey) {
        this.merchantPublicKey = merchantPublicKey;
        return this;
    }

    public byte[] merchantPublicKey() {
        return merchantPublicKey;
    }
}
