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
public class SignTO extends BaseTO<SignTO> {

    //choice 1
    private byte[] transaction; //input
    
    //choice 2
    private long amountToSpend; //input 
    private String p2shAddressTo; //input
    private List<Pair<byte[],Long>> outpointsCoinPair; //input
    
    private List<TxSig> serverSignatures; //output
    
    public SignTO transaction(byte[] transaction) {
        this.transaction = transaction;
        return this;
    }
    
    public byte[] transaction() {
        return transaction;
    }
    
    public SignTO serverSignatures(List<TxSig> serverSignatures) {
        this.serverSignatures = serverSignatures;
        return this;
    }
    
    public List<TxSig> serverSignatures() {
        return serverSignatures;
    }
    
    public SignTO amountToSpend(long amountToSpend) {
        this.amountToSpend = amountToSpend;
        return this;
    }

    public long amountToSpend() {
        return amountToSpend;
    }
    
    public SignTO p2shAddressTo(String p2shAddressTo) {
        this.p2shAddressTo = p2shAddressTo;
        return this;
    }

    public String p2shAddressTo() {
        return p2shAddressTo;
    }
    
    public SignTO outpointsCoinPair(List<Pair<byte[],Long>> outpointsCoinPair) {
        this.outpointsCoinPair = outpointsCoinPair;
        return this;
    }
    
    public List<Pair<byte[],Long>> outpointsCoinPair() {
        return outpointsCoinPair;
    }
}
