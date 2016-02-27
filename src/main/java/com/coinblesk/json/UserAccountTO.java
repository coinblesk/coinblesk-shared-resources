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
public class UserAccountTO extends BaseTO<UserAccountTO> {
    private String email;
    private String password;
    
    public UserAccountTO password(String password) {
        this.password = password;
        return this;
    }
    
    public String password() {
        return password;
    }
    
    public UserAccountTO email(String email) {
        this.email = email;
        return this;
    }
    
    public String email() {
        return email;
    } 
}
