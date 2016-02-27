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
public class TxSig {

    private String sigR;
    private String sigS;

    public TxSig sigR(String sigR) {
        this.sigR = sigR;
        return this;
    }

    public String sigR() {
        return sigR;
    }

    public TxSig sigS(String sigS) {
        this.sigS = sigS;
        return this;
    }

    public String sigS() {
        return sigS;
    }
}
