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
public class PrepareHalfSignTO {

    public enum Reason {
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2),
        KEYS_NOT_FOUND(-3),
        ADDRESS_EMPTY(-4),
        ADDRESS_UNKNOWN(-5),
        NOT_ENOUGH_COINS(-6);

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
    //
    private byte[] halfSignedTransaction;
    private byte[] clientPublicKey;
    private Long amountToSpend;
    private byte[] p2shAddress;
    private List<RefundP2shTO.TxSig> signatures;

    public PrepareHalfSignTO success(final boolean success) {
        this.success = success;
        return this;
    }

    public PrepareHalfSignTO setSuccess() {
        return success(true);
    }

    public boolean isSuccess() {
        return success;
    }

    public PrepareHalfSignTO reason(final PrepareHalfSignTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }

    public PrepareHalfSignTO.Reason reason() {
        final PrepareHalfSignTO.Reason reason = PrepareHalfSignTO.Reason.get(this.reason);
        return reason == null ? PrepareHalfSignTO.Reason.REASON_NOT_FOUND : reason;
    }

    public PrepareHalfSignTO message(final String message) {
        this.message = message;
        return this;
    }

    public String message() {
        return message;
    }

    public PrepareHalfSignTO halfSignedTransaction(byte[] halfSignedTransaction) {
        this.halfSignedTransaction = halfSignedTransaction;
        return this;
    }

    public byte[] halfSignedTransaction() {
        return halfSignedTransaction;
    }

    public PrepareHalfSignTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
    
    public PrepareHalfSignTO amountToSpend(Long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }
    
    public Long amountToSpend() {
        return amountToSpend;
    }
    
    public PrepareHalfSignTO p2shAddress(byte[] p2shAddress) {
        this.p2shAddress = p2shAddress;
        return this;
    }
    
    public byte[] p2shAddress() {
        return p2shAddress;
    }
    
    public PrepareHalfSignTO signatures(List<RefundP2shTO.TxSig> signatures) {
        this.signatures = signatures;
        return this;
    }
    
    public List<RefundP2shTO.TxSig> signatures() {
        return signatures;
    }
}
