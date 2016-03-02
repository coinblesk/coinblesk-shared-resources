/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import java.util.Date;

/**
 *
 * @author Thomas Bocek
 */
public class BaseTO<T extends BaseTO> {
    private int type = 0; //input/output - always valid
    private String message; //output
    private TxSig messageSig; //input/output
    private Date currentDate; //input for now
    
    public boolean isInputSet() {
        return messageSig != null && currentDate != null;
    }

    public boolean isOutputSet() {
        return messageSig != null;
    }
    
    public T setSuccess() {
        return type(Type.SUCCESS);
    }
    
    public boolean isSuccess() {
        return type() == Type.SUCCESS;
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
    
    public T currentDate(final Date currentDate) {
        this.currentDate = currentDate;
        return (T)this;
    }
    
    public Date currentDate() {
        return currentDate;
    }

    
}
