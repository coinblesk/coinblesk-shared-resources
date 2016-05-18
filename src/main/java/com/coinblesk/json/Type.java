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
		SUCCESS_BUT_ADDRESS_ALREADY_EXISTS(5),
        SUCCESS_BUT_KEY_ALREADY_EXISTS(4),
        SUCCESS_BUT_NO_INSTANT_PAYMENT(3),
        SUCCESS_BUT_EMAIL_ALREADY_EXISTS_NOT_ACTIVATED(2),
        SUCCESS_BUT_EMAIL_ALREADY_EXISTS_ACTIVATED(1),
        SUCCESS(0),
        //below 0, indicates error, above 1 success
        SERVER_ERROR(-1),
        REASON_NOT_FOUND(-2),
        KEYS_NOT_FOUND(-3),
        ADDRESS_EMPTY(-4),
        NOT_ENOUGH_COINS(-5),
        NO_EMAIL(-6),
        INVALID_EMAIL(-7),
        PASSWORD_TOO_SHORT(-8),
        INVALID_EMAIL_TOKEN(-9),
        NO_ACCOUNT(-10),
        ACCOUNT_ERROR(-11),
        BURNED_OUTPUTS(-12),
        SIGNATURE_ERROR(-13),
        TIME_MISMATCH(-14),
        INPUT_MISMATCH(-15),
        JSON_SIGNATURE_ERROR(-16),
        CONCURRENCY_ERROR(-17),
        TX_ERROR(-18),
        LOCKTIME_ERROR(-19);
        
        private final int type;
        // Reverse-lookup map for getting a type by int
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
        
        public boolean isSuccess() {
        	return type >= 0;
        }
        
        public boolean isError() {
        	return !isSuccess();
        }
}
