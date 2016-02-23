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
public class CompleteSign {

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

    public CompleteSign success(final boolean success) {
        this.success = success;
        return this;
    }

    public CompleteSign setSuccess() {
        return success(true);
    }

    public boolean isSuccess() {
        return success;
    }

    public CompleteSign reason(final CompleteSign.Reason reason) {
        this.reason = reason.nr();
        return this;
    }

    public CompleteSign.Reason reason() {
        final CompleteSign.Reason reason = CompleteSign.Reason.get(this.reason);
        return reason == null ? CompleteSign.Reason.REASON_NOT_FOUND : reason;
    }

    public CompleteSign message(final String message) {
        this.message = message;
        return this;
    }

    public String message() {
        return message;
    }

    public CompleteSign fullSignedTransaction(byte[] fullSignedTransaction) {
        this.fullSignedTransaction = fullSignedTransaction;
        return this;
    }

    public byte[] fullSignedTransaction() {
        return fullSignedTransaction;
    }

    public CompleteSign clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
}
