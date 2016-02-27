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
public class RefundP2shTO extends BaseTO<RefundP2shTO> {

    private List<byte[]> refundClientOutpoints;
    private byte[] clientPublicKey;
    private byte[] merchantPublicKey;
    
    private List<TxSig> refundSignaturesClient;
    private List<TxSig> refundSignaturesMerchant;
    private List<TxSig> refundSignaturesClientServer;
    private byte[] fullRefundTransactionClient;
    private byte[] fullRefundTransactionMerchant;
    private byte[] unsignedTransaction;
    private byte[] unsignedRefundMerchantTransaction;
    
    public RefundP2shTO refundClientOutpoints(List<byte[]> refundClientOutpoints) {
        this.refundClientOutpoints = refundClientOutpoints;
        return this;
    }
    
    public List<byte[]> refundClientOutpoints() {
        return refundClientOutpoints;
    }
    
    public RefundP2shTO refundSignaturesClient(List<TxSig> refundSignaturesClient) {
        this.refundSignaturesClient = refundSignaturesClient;
        return this;
    }
    
    public List<TxSig> refundSignaturesClient() {
        return refundSignaturesClient;
    }
    
    public RefundP2shTO refundSignaturesMerchant(List<TxSig> refundSignaturesMerchant) {
        this.refundSignaturesMerchant = refundSignaturesMerchant;
        return this;
    }
    
    public List<TxSig> refundSignaturesMerchant() {
        return refundSignaturesMerchant;
    }
    
    public RefundP2shTO refundSignaturesClientServer(List<TxSig> refundSignaturesClientServer) {
        this.refundSignaturesClientServer = refundSignaturesClientServer;
        return this;
    }
    
    public List<TxSig> refundSignaturesClientServer() {
        return refundSignaturesClientServer;
    }
    
    public RefundP2shTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
    
    public RefundP2shTO merchantPublicKey(byte[] merchantPublicKey) {
        this.merchantPublicKey = merchantPublicKey;
        return this;
    }
    
    public byte[] merchantPublicKey() {
        return merchantPublicKey;
    }
    
    public RefundP2shTO fullRefundTransactionClient(byte[] fullRefundTransactionClient) {
        this.fullRefundTransactionClient = fullRefundTransactionClient;
        return this;
    }

    public byte[] fullRefundTransactionClient() {
        return fullRefundTransactionClient;
    }
    
    public RefundP2shTO fullRefundTransactionMerchant(byte[] fullRefundTransactionMerchant) {
        this.fullRefundTransactionMerchant = fullRefundTransactionMerchant;
        return this;
    }

    public byte[] fullRefundTransactionMerchant() {
        return fullRefundTransactionMerchant;
    }
    
    public RefundP2shTO unsignedTransaction(byte[] unsignedTransaction) {
        this.unsignedTransaction = unsignedTransaction;
        return this;
    }

    public byte[] unsignedTransaction() {
        return unsignedTransaction;
    }
    
    public RefundP2shTO unsignedRefundMerchantTransaction(byte[] unsignedRefundMerchantTransaction) {
        this.unsignedRefundMerchantTransaction = unsignedRefundMerchantTransaction;
        return this;
    }

    public byte[] unsignedRefundMerchantTransaction() {
        return unsignedRefundMerchantTransaction;
    }
    
    
}
