/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import com.coinblesk.util.Pair;
import java.util.List;

/**
 *
 * @author Thomas Bocek
 */
public class SignTO extends BaseTO<SignTO> {

    //choice 1
    private byte[] transaction; //input
    
    //choice 2
    private long amountToSpend; //input 
    private String p2shAddressTo; //input
    private byte[] bloomFilter; //input optional
    
    //choice 3
    private List<Pair<byte[],Long>> refundClientOutpointsCoinPair; //input
    //private byte[] bloomFilter; //optional, defined above
    private List<TxSig> refundSignaturesClient; //input //optional
    
    
    private List<TxSig> serverSignatures; //output
    
    public SignTO transaction(byte[] transaction) {
        this.transaction = transaction;
        return this;
    }
    
    public byte[] transaction() {
        return transaction;
    }
    
    public SignTO serverSignatures(List<TxSig> serverSignatures) {
        this.serverSignatures = serverSignatures;
        return this;
    }
    
    public List<TxSig> serverSignatures() {
        return serverSignatures;
    }
    
    public SignTO amountToSpend(long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }

    public long amountToSpend() {
        return amountToSpend;
    }
    
     public SignTO p2shAddressTo(String p2shAddressTo) {
        this.p2shAddressTo = p2shAddressTo;
        return this;
    }

    public String p2shAddressTo() {
        return p2shAddressTo;
    }
    
    public SignTO bloomFilter(byte[] bloomFilter) {
        this.bloomFilter = bloomFilter;
        return this;
    }

    public byte[] bloomFilter() {
        return bloomFilter;
    }
    
    public SignTO refundSignaturesClient(List<TxSig> refundSignaturesClient) {
        this.refundSignaturesClient = refundSignaturesClient;
        return this;
    }
    
    public List<TxSig> refundSignaturesClient() {
        return refundSignaturesClient;
    }
    
    public SignTO refundClientOutpointsCoinPair(List<Pair<byte[],Long>> refundClientOutpointsCoinPair) {
        this.refundClientOutpointsCoinPair = refundClientOutpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> refundClientOutpointsCoinPair() {
        return refundClientOutpointsCoinPair;
    }
}
