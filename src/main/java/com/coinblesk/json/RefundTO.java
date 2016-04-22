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

import com.coinblesk.util.Pair;
import java.util.List;

/**
 *
 * @author Thomas Bocek
 */
public class RefundTO extends BaseTO<RefundTO> {

    //choice 1
    private byte[] refundTransaction; //input
    
    //choice 2
    private long lockTimeSeconds; //input 
    private String refundSendTo; //input
    private List<Pair<byte[],Long>> outpointsCoinPair; //input
    
    private List<TxSig> clientSignatures; //input
    
    private List<TxSig> serverSignatures; //output
    
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
    
    public RefundTO serverSignatures(List<TxSig> serverSignatures) {
        this.serverSignatures = serverSignatures;
        return this;
    }
    
    public List<TxSig> serverSignatures() {
        return serverSignatures;
    }
    
    public RefundTO lockTimeSeconds(long lockTimeSeconds) {
        this.lockTimeSeconds = lockTimeSeconds;
        return this;
    }

    public long lockTimeSeconds() {
        return lockTimeSeconds;
    }
    
    public RefundTO refundSendTo(String refundSendTo) {
        this.refundSendTo = refundSendTo;
        return this;
    }

    public String refundSendTo() {
        return refundSendTo;
    }
    
    public RefundTO outpointsCoinPair(List<Pair<byte[],Long>> outpointsCoinPair) {
        this.outpointsCoinPair = outpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> outpointsCoinPair() {
        return outpointsCoinPair;
    }
}
