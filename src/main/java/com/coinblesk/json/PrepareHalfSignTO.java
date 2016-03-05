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

    private long amountToSpend; //input
    private String p2shAddressTo; //input
    private byte[] bloomFilter; //input //output optional
    //
    private byte[] unsignedTransaction; //output
    private List<TxSig> signatures; //output

    
    @Override
    public boolean isInputSet() {
        return super.isInputSet() && amountToSpend !=0 && p2shAddressTo != null;
    }
    
    @Override
    public boolean isOutputSet() {
        return super.isOutputSet() && unsignedTransaction != null && signatures != null;
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
    
    public PrepareHalfSignTO bloomFilter(byte[] bloomFilter) {
        this.bloomFilter = bloomFilter;
        return this;
    }

    public byte[] bloomFilter() {
        return bloomFilter;
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
