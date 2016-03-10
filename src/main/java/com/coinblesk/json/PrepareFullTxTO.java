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
public class PrepareFullTxTO extends BaseTO<PrepareFullTxTO> {

    private byte[] unsignedTransaction; //output and input
    private List<TxSig> signatures; //output

    
    @Override
    public boolean isInputSet() {
        return super.isInputSet() && unsignedTransaction != null;
    }
    
    @Override
    public boolean isOutputSet() {
        return super.isOutputSet() && unsignedTransaction != null && signatures != null;
    }

    public PrepareFullTxTO unsignedTransaction(byte[] unsignedTransaction) {
        this.unsignedTransaction = unsignedTransaction;
        return this;
    }

    public byte[] unsignedTransaction() {
        return unsignedTransaction;
    }

    public PrepareFullTxTO signatures(List<TxSig> signatures) {
        this.signatures = signatures;
        return this;
    }

    public List<TxSig> signatures() {
        return signatures;
    }
}
