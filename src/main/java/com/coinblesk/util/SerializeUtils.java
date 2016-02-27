/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.RefundP2shTO;
import com.coinblesk.json.TxSig;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.crypto.TransactionSignature;

/**
 *
 * @author Thomas Bocek
 */
public class SerializeUtils {
    
    public static List<TxSig> serializeSignatures(final List<TransactionSignature> signatures) {
        final List<TxSig> retVal = new ArrayList<>(signatures.size());
        for(final TransactionSignature signature:signatures) {
            retVal.add(new TxSig().sigR(
                    signature.r.toString()).sigS(signature.s.toString()));
        }
        return retVal;
    }
    
    public static List<TransactionSignature> deserializeSignatures(final List<TxSig> signatures) {
        final List<TransactionSignature> retVal = new ArrayList<>(signatures.size());
        for(final TxSig txsig:signatures) {
            retVal.add(new TransactionSignature(new BigInteger(txsig.sigR()), new BigInteger(txsig.sigS())));
        }
        return retVal;
    }
    
    public static List<byte[]> serializeOutPoints(final List<TransactionOutPoint> transactionOutPoints) {
        final List<byte[]> retVal = new ArrayList<>(transactionOutPoints.size());
        for(final TransactionOutPoint top:transactionOutPoints) {
            retVal.add(top.unsafeBitcoinSerialize());
        }
        return retVal;
    }
    
    public static List<TransactionOutPoint> deserializeOutPoints(final NetworkParameters params, 
            final List<byte[]> transactionOutPoints) {
        final List<TransactionOutPoint> retVal = new ArrayList<>(transactionOutPoints.size());
        for(final byte[] b:transactionOutPoints) {
            retVal.add(new TransactionOutPoint(params, b, 0));
        }
        return retVal;
    }

}
