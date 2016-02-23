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
public class RefundTO {

    public static class ClientSig {

        private String clientSignatureR;
        private String clientSignatureS;

        public ClientSig clientSignatureR(String clientSignatureR) {
            this.clientSignatureR = clientSignatureR;
            return this;
        }

        public String clientSignatureR() {
            return clientSignatureR;
        }

        public ClientSig clientSignatureS(String clientSignatureS) {
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
    private String refundTransaction;
    private String clientPublicKey;
    private List<ClientSig> clientSignatures;
    
    public RefundTO success(final boolean success) {
        this.success = success;
        return this;
    }
    
    public RefundTO setSuccess() {
        return success(true);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public RefundTO reason(final RefundTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }
    
    public RefundTO.Reason reason() {
        final RefundTO.Reason reason = RefundTO.Reason.get(this.reason);
        return reason == null ? RefundTO.Reason.REASON_NOT_FOUND : reason;
    }
    
    public RefundTO message(final String message) {
        this.message = message;
        return this;
    }
    
    public String message() {
        return message;
    }
    
    public RefundTO refundTransaction(String refundTransaction) {
        this.refundTransaction = refundTransaction;
        return this;
    }
    
    public String refundTransaction() {
        return refundTransaction;
    }
    
    public RefundTO clientSignatureR(List<ClientSig> clientSignatures) {
        this.clientSignatures = clientSignatures;
        return this;
    }
    
    public List<ClientSig> clientSignatures() {
        return clientSignatures;
    }
    
    public RefundTO clientPublicKey(String clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public String clientPublicKey() {
        return clientPublicKey;
    }
}