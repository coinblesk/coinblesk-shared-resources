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

    private byte[] clientPublicKey; //input
    private long amountToSpend; //input
    private String p2shAddressTo; //input
    //
    private byte[] unsignedTransaction; //output
    private List<TxSig> signatures; //output

    public PrepareHalfSignTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public boolean isInputSet() {
        return super.isInputSet() && clientPublicKey != null && amountToSpend !=0 && p2shAddressTo != null;
    }
    
    public boolean isOutputSet() {
        return super.isOutputSet() && unsignedTransaction != null && signatures != null;
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
