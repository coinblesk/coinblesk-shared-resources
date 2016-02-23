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
    KEYS_NOT_FOUND(-3);

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
    private List<byte[]> transactionOutputs;
    private String clientPublicKey;
    private List<TxSig> refundSignatures;
    private byte[] fullRefundTransaction;
    
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
    
    public RefundP2shTO refundTransaction(List<byte[]> transactionOutputs) {
        this.transactionOutputs = transactionOutputs;
        return this;
    }
    
    public List<byte[]> transactionOutputs() {
        return transactionOutputs;
    }
    
    public RefundP2shTO refundSignatures(List<TxSig> refundSignatures) {
        this.refundSignatures = refundSignatures;
        return this;
    }
    
    public List<TxSig> refundSignatures() {
        return refundSignatures;
    }
    
    public RefundP2shTO clientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public String clientPublicKey() {
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
