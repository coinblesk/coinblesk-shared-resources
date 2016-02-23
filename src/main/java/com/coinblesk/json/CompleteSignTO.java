/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author draft
 */
public class CompleteSignTO {

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
    //
    private byte[] fullSignedTransaction;
    private byte[] clientPublicKey;

    public CompleteSignTO success(final boolean success) {
        this.success = success;
        return this;
    }

    public CompleteSignTO setSuccess() {
        return success(true);
    }

    public boolean isSuccess() {
        return success;
    }

    public CompleteSignTO reason(final CompleteSignTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }

    public CompleteSignTO.Reason reason() {
        final CompleteSignTO.Reason reason = CompleteSignTO.Reason.get(this.reason);
        return reason == null ? CompleteSignTO.Reason.REASON_NOT_FOUND : reason;
    }

    public CompleteSignTO message(final String message) {
        this.message = message;
        return this;
    }

    public String message() {
        return message;
    }

    public CompleteSignTO fullSignedTransaction(byte[] fullSignedTransaction) {
        this.fullSignedTransaction = fullSignedTransaction;
        return this;
    }

    public byte[] fullSignedTransaction() {
        return fullSignedTransaction;
    }

    public CompleteSignTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
}
