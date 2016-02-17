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
public class BalanceTO {
    
    public enum Reason {
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2);
        
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
    private String balance;
    
    public BalanceTO success(final boolean success) {
        this.success = success;
        return this;
    }
    
    public BalanceTO setSuccess() {
        return success(true);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public BalanceTO reason(final BalanceTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }
    
    public BalanceTO.Reason reason() {
        final BalanceTO.Reason reason = BalanceTO.Reason.get(this.reason);
        return reason == null ? BalanceTO.Reason.REASON_NOT_FOUND : reason;
    }
    
    public BalanceTO message(final String message) {
        this.message = message;
        return this;
    }
    
    public String message() {
        return message;
    }
    
    public BalanceTO balance(String balance) {
        this.balance = balance;
        return this;
    }
    
    public String balance() {
        return balance;
    }
}
