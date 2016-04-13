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
public class RefundTO extends BaseTO<RefundTO> {

    //choice 1
    private byte[] refundTransaction; //input
    
    //choice 2
    private long lockTime; //input 
    private String refundSendTo; //input
    private List<Pair<byte[],Long>> outpointsCoinPair; //input
    
    private List<TxSig> clientSignatures; //input
    
    private List<TxSig> serverSignatures; //output
    
    public RefundTO refundTransaction(byte[] refundTransaction) {
        this.refundTransaction = refundTransaction;
        return this;
    }
    
    public byte[] refundTransaction() {
        return refundTransaction;
    }
    
    public RefundTO clientSignatures(List<TxSig> clientSignatures) {
        this.clientSignatures = clientSignatures;
        return this;
    }
    
    public List<TxSig> clientSignatures() {
        return clientSignatures;
    }
    
    public RefundTO serverSignatures(List<TxSig> serverSignatures) {
        this.serverSignatures = serverSignatures;
        return this;
    }
    
    public List<TxSig> serverSignatures() {
        return serverSignatures;
    }
    
    public RefundTO lockTime(long lockTime) {
        this.lockTime = lockTime;
        return this;
    }

    public long lockTime() {
        return lockTime;
    }
    
    public RefundTO refundSendTo(String refundSendTo) {
        this.refundSendTo = refundSendTo;
        return this;
    }

    public String refundSendTo() {
        return refundSendTo;
    }
    
    public RefundTO outpointsCoinPair(List<Pair<byte[],Long>> outpointsCoinPair) {
        this.outpointsCoinPair = outpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> outpointsCoinPair() {
        return outpointsCoinPair;
    }
}
