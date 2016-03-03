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
public class RefundP2shTO extends BaseTO<RefundP2shTO> {

    private List<Pair<byte[],Long>> refundClientOutpointsCoinPair; //input
    private byte[] bloomFilter; //input optional
    private List<TxSig> refundSignaturesClient; //input
    //
    private List<TxSig> refundSignaturesServer; //output
    private byte[] fullRefundTransaction; //output
    
    @Override
    public boolean isInputSet() {
        return super.isInputSet() && refundClientOutpointsCoinPair != null && refundSignaturesClient != null;
    }
    
    @Override
    public boolean isOutputSet() {
        return super.isOutputSet() && refundSignaturesServer != null && fullRefundTransaction != null;
    }
    
    public RefundP2shTO refundClientOutpointsCoinPair(List<Pair<byte[],Long>> refundClientOutpointsCoinPair) {
        this.refundClientOutpointsCoinPair = refundClientOutpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> refundClientOutpointsCoinPair() {
        return refundClientOutpointsCoinPair;
    }
    
    public RefundP2shTO bloomFilter(byte[] bloomFilter) {
        this.bloomFilter = bloomFilter;
        return this;
    }

    public byte[] bloomFilter() {
        return bloomFilter;
    }
    
    public RefundP2shTO refundSignaturesClient(List<TxSig> refundSignaturesClient) {
        this.refundSignaturesClient = refundSignaturesClient;
        return this;
    }
    
    public List<TxSig> refundSignaturesClient() {
        return refundSignaturesClient;
    }
    
    public RefundP2shTO refundSignaturesServer(List<TxSig> refundSignaturesServer) {
        this.refundSignaturesServer = refundSignaturesServer;
        return this;
    }
    
    public List<TxSig> refundSignaturesServer() {
        return refundSignaturesServer;
    }
    
    public RefundP2shTO fullRefundTransaction(byte[] fullRefundTransaction) {
        this.fullRefundTransaction = fullRefundTransaction;
        return this;
    }

    public byte[] fullRefundTransaction() {
        return fullRefundTransaction;
    }
}
