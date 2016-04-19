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
public class VerifyTO extends BaseTO<VerifyTO> {

    //choice 1
    private byte[] transaction; //input
    
    //choice 2
    private long amountToSpend; //input 
    private String p2shAddressTo; //input
    private List<Pair<byte[],Long>> outpointsCoinPair; //input
    
    private List<TxSig> clientSignatures; //input
    private List<TxSig> serverSignatures; //input
    
    //used from above
    //private byte[] transaction; //output
    
    public VerifyTO transaction(byte[] transaction) {
        this.transaction = transaction;
        return this;
    }
    
    public byte[] transaction() {
        return transaction;
    }
    
    public VerifyTO serverSignatures(List<TxSig> serverSignatures) {
        this.serverSignatures = serverSignatures;
        return this;
    }
    
    public List<TxSig> serverSignatures() {
        return serverSignatures;
    }
    
    public VerifyTO clientSignatures(List<TxSig> clientSignatures) {
        this.clientSignatures = clientSignatures;
        return this;
    }
    
    public List<TxSig> clientSignatures() {
        return clientSignatures;
    }
    
    public VerifyTO amountToSpend(long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }

    public long amountToSpend() {
        return amountToSpend;
    }
    
    public VerifyTO p2shAddressTo(String p2shAddressTo) {
        this.p2shAddressTo = p2shAddressTo;
        return this;
    }

    public String p2shAddressTo() {
        return p2shAddressTo;
    }
    
    public VerifyTO outpointsCoinPair(List<Pair<byte[],Long>> outpointsCoinPair) {
        this.outpointsCoinPair = outpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> outpointsCoinPair() {
        return outpointsCoinPair;
    }
}
