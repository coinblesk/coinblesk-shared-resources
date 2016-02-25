/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author draft
 */
public class RefundP2shTO {

    public static class TxSig {

        private String clientSignatureR;
        private String clientSignatureS;

        public TxSig clientSignatureR(String clientSignatureR) {
            this.clientSignatureR = clientSignatureR;
            return this;
        }

        public String clientSignatureR() {
            return clientSignatureR;
        }

        public TxSig clientSignatureS(String clientSignatureS) {
            this.clientSignatureS = clientSignatureS;
            return this;
        }

        public String clientSignatureS() {
            return clientSignatureS;
        }
    }


public enum Reason {
    SERVER_ERROR(-1),
    REASON_NOT_FOUND(-2),
    KEYS_NOT_FOUND(-3),
    NOT_ENOUGH_FUNDS(-4);

    private final int reason;
    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<Integer, Reason> lookup = new HashMap<Integer, Reason>();

    static {
        for (final Reason reason : Reason.values()) {
            lookup.put(reason.nr(), reason);
        }
    }

    private Reason(final int reason) {
        this.reason = reason;
    }

    public int nr() {
        return reason;
    }

    public static Reason get(final int nr) {
        return lookup.get(nr);
    }
}

private boolean success = false;
    private int reason = 0;
    private String message;
    private List<byte[]> transactionOutpoints;
    private byte[] clientPublicKey;
    private byte[] merchantPublicKey;
    private List<TxSig> refundSignaturesClient;
    private List<TxSig> refundSignaturesMerchant;
    private List<TxSig> refundSignaturesServer;
    private byte[] fullRefundTransactionClient;
    private byte[] fullRefundTransactionMerchant;
    private byte[] halfSignedTransaction;
    
    public RefundP2shTO success(final boolean success) {
        this.success = success;
        return this;
    }
    
    public RefundP2shTO setSuccess() {
        return success(true);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public RefundP2shTO reason(final RefundP2shTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }
    
    public RefundP2shTO.Reason reason() {
        final RefundP2shTO.Reason reason = RefundP2shTO.Reason.get(this.reason);
        return reason == null ? RefundP2shTO.Reason.REASON_NOT_FOUND : reason;
    }
    
    public RefundP2shTO message(final String message) {
        this.message = message;
        return this;
    }
    
    public String message() {
        return message;
    }
    
    public RefundP2shTO transactionOutpoints(List<byte[]> transactionOutpoints) {
        this.transactionOutpoints = transactionOutpoints;
        return this;
    }
    
    public List<byte[]> transactionOutpoints() {
        return transactionOutpoints;
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
    
    public RefundP2shTO halfSignedTransaction(byte[] halfSignedTransaction) {
        this.halfSignedTransaction = halfSignedTransaction;
        return this;
    }

    public byte[] halfSignedTransaction() {
        return halfSignedTransaction;
    }
}
