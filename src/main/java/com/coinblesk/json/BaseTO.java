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

/**
 *
 * @author Thomas Bocek
 */
public class BaseTO<T extends BaseTO> { 
    private int type = 0; //input/output - always valid
    private String message; //output
    private TxSig messageSig; //input/output
    private long currentDate; //input for now
    private byte[] clientPublicKey; //input
    
    public boolean isInputSet() {
        return messageSig != null && currentDate > 0;
    }

    public boolean isOutputSet() {
        return messageSig != null;
    }
    
    public T setSuccess() {
        return type(Type.SUCCESS);
    }
    
    public boolean isSuccess() {
        return type().nr() >= 0;
    }
    
    public T type(final Type type) {
        this.type = type.nr();
        return (T)this;
    }
    
    public Type type() {
        final Type type = Type.get(this.type);
        return type == null ? Type.REASON_NOT_FOUND : type;
    }
    
    public T message(final String message) {
        this.message = message;
        return (T)this;
    }
    
    public String message() {
        return message;
    }
    
    public T messageSig(final TxSig messageSig) {
        this.messageSig = messageSig;
        return (T)this;
    }
    
    public TxSig messageSig() {
        return messageSig;
    }
    
    public T currentDate(final long currentDate) {
        this.currentDate = currentDate;
        return (T)this;
    }
    
    public long currentDate() {
        return currentDate;
    }
    
    public T clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return (T)this;
    }

    public byte[] clientPublicKey() {
        return clientPublicKey;
    }

    
}
