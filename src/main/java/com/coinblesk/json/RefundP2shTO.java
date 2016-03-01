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

    private List<Pair<byte[],Long>> refundClientOutpointsCoinPair;
    private byte[] clientPublicKey;
    
    private List<TxSig> refundSignaturesClient;
    private List<TxSig> refundSignaturesServer;
    private byte[] fullRefundTransaction;
    
    public RefundP2shTO refundClientOutpointsCoinPair(List<Pair<byte[],Long>> refundClientOutpointsCoinPair) {
        this.refundClientOutpointsCoinPair = refundClientOutpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> refundClientOutpointsCoinPair() {
        return refundClientOutpointsCoinPair;
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
    
    public RefundP2shTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
    
    public RefundP2shTO fullRefundTransaction(byte[] fullRefundTransaction) {
        this.fullRefundTransaction = fullRefundTransaction;
        return this;
    }

    public byte[] fullRefundTransaction() {
        return fullRefundTransaction;
    }
}
