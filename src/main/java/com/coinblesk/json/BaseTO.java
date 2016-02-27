/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

/**
 *
 * @author Thomas Bocek
 */
public class BaseTO<T extends BaseTO> {
    private int type = 0;
    private String message;
    
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
}
