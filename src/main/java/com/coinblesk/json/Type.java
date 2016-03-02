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
 * @author Thomas Bocek
 */
public enum Type {
        NO_INSTANT_PAYMENT(4),
        EMAIL_ALREADY_EXISTS_NOT_ACTIVATED(3),
        EMAIL_ALREADY_EXISTS_ACTIVATED(2),
        SUCCESS(1),
        REQUEST(0),
        //below 0, indicates error, above 1 success
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2),
        KEYS_NOT_FOUND(-3),
        KEY_ALREADY_EXISTS(-4),
        ADDRESS_EMPTY(-5),
        ADDRESS_UNKNOWN(-6),
        NOT_ENOUGH_COINS(-7),
        NO_EMAIL(-8),
        INVALID_EMAIL(-9),
        PASSWORD_TOO_SHORT(-10),
        INVALID_EMAIL_TOKEN(-11),
        NO_ACCOUNT(-12),
        ACCOUNT_ERROR(-13),
        NO_HASH_KEY(-14),
        DOUBLE_SPENDING(-15),
        BURNED_OUTPUTS(-16),
        SIGNATURE_ERROR(-17),
        REPLAY_ATTACK(-18),
        OLD_TIME(-19),
        INPUT_MISMATCH(-20);

        
        private final int type;
        // Reverse-lookup map for getting a day from an abbreviation
        private static final Map<Integer, Type> lookup = new HashMap<Integer, Type>();

        static {
            for (final Type reason : Type.values()) {
                lookup.put(reason.nr(), reason);
            }
        }
        
        private Type(final int type) {
            this.type = type;
        }
        
        public int nr() {
            return type;
        }
        
        public static Type get(final int nr) {
            return lookup.get(nr);
        }
}
