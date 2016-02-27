/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import java.util.List;

/**
 *
 * @author Thomas Bocek
 */
public class PrepareHalfSignTO extends BaseTO<PrepareHalfSignTO> {

    private byte[] clientPublicKey;
    private long amountToSpend;
    private String p2shAddressTo;
    //
    private byte[] unsignedTransaction;
    private List<TxSig> signatures;

    public PrepareHalfSignTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }

    public PrepareHalfSignTO amountToSpend(long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }

    public long amountToSpend() {
        return amountToSpend;
    }

    public PrepareHalfSignTO p2shAddressTo(String p2shAddressTo) {
        this.p2shAddressTo = p2shAddressTo;
        return this;
    }

    public String p2shAddressTo() {
        return p2shAddressTo;
    }

    public PrepareHalfSignTO unsignedTransaction(byte[] unsignedTransaction) {
        this.unsignedTransaction = unsignedTransaction;
        return this;
    }

    public byte[] unsignedTransaction() {
        return unsignedTransaction;
    }

    public PrepareHalfSignTO signatures(List<TxSig> signatures) {
        this.signatures = signatures;
        return this;
    }

    public List<TxSig> signatures() {
        return signatures;
    }
}
