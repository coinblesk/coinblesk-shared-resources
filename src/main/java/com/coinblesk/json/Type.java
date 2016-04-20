/*
 * Copyright 2016 The Coinblesk team and the CSG Group at University of Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.coinblesk.json;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thomas Bocek
 */
public enum Type {
        SUCCESS_FILTERED(5),
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
        TIME_MISMATCH(-19),
        INPUT_MISMATCH(-20),
        INVALID_LOCKTIME(-21),
        INVALID_TX(-22),
        JSON_SIGNATURE_ERROR(-23),
        CONCURRENCY_ERROR(-24),
        TX_ERROR(-25);
        
        private final int type;
        // Reverse-lookup map for getting a day from an abbreviation
        private static final Map<Integer, Type> lookup = new HashMap<>();

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
