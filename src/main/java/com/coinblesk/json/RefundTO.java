/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.json;

import java.util.List;

/**
 *
 * @author Thomas Bocek
 */
public class RefundTO extends BaseTO<RefundTO> {

    private byte[] clientPublicKey;
    private byte[] refundTransaction;
    private List<TxSig> clientSignatures;
    
    public RefundTO clientPublicKey(byte[] clientPublicKey) {
        this.clientPublicKey = clientPublicKey;
        return this;
    }
    
    public byte[] clientPublicKey() {
        return clientPublicKey;
    }
    
    public RefundTO refundTransaction(byte[] refundTransaction) {
        this.refundTransaction = refundTransaction;
        return this;
    }
    
    public byte[] refundTransaction() {
        return refundTransaction;
    }
    
    public RefundTO clientSignatures(List<TxSig> clientSignatures) {
        this.clientSignatures = clientSignatures;
        return this;
    }
    
    public List<TxSig> clientSignatures() {
        return clientSignatures;
    }
}
