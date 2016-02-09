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
public class StatusTO {
    
    public enum Reason {
        EMAIL_ALREADY_EXISTS_NOT_ACTIVATED(2),
        EMAIL_ALREADY_EXISTS_ACTIVATED(1),
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2),
        NO_EMAIL(-3),
        INVALID_EMAIL(-4),
        PASSWORD_TOO_SHORT(-5),
        INVALID_EMAIL_TOKEN(-6);
        
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
    
    public StatusTO success(final boolean success) {
        this.success = success;
        return this;
    }
    
    public StatusTO setSuccess() {
        return success(true);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public StatusTO reason(final Reason reason) {
        this.reason = reason.nr();
        return this;
    }
    
    public Reason reason() {
        final Reason reason = Reason.get(this.reason);
        return reason == null ? Reason.REASON_NOT_FOUND : reason;
    }
    
    public StatusTO message(final String message) {
        this.message = message;
        return this;
    }
    
    public String message() {
        return message;
    }
}
