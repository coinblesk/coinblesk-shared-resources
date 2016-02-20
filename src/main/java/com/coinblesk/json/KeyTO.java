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
public class KeyTO {
    
    public enum Reason {
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2),
        KEY_ALREADY_EXISTS(-3);
        
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
    //TODO: use something like: https://gist.github.com/orip/3635246
    private String publicKey;
    
    public KeyTO success(final boolean success) {
        this.success = success;
        return this;
    }
    
    public KeyTO setSuccess() {
        return success(true);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public KeyTO reason(final KeyTO.Reason reason) {
        this.reason = reason.nr();
        return this;
    }
    
    public KeyTO.Reason reason() {
        final KeyTO.Reason reason = KeyTO.Reason.get(this.reason);
        return reason == null ? KeyTO.Reason.REASON_NOT_FOUND : reason;
    }
    
    public KeyTO message(final String message) {
        this.message = message;
        return this;
    }
    
    public String message() {
        return message;
    }
    
    public KeyTO publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }
    
    public String publicKey() {
        return publicKey;
    }
}
