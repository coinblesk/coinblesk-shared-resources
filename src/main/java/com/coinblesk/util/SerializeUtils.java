/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.RefundP2shTO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.crypto.TransactionSignature;

/**
 *
 * @author draft
 */
public class SerializeUtils {
    
    public static List<RefundP2shTO.TxSig> serializeSignatures(final List<TransactionSignature> signatures) {
        final List<RefundP2shTO.TxSig> retVal = new ArrayList<>(signatures.size());
        for(TransactionSignature signature:signatures) {
            retVal.add(new RefundP2shTO.TxSig().clientSignatureR(
                    signature.r.toString()).clientSignatureS(signature.s.toString()));
        }
        return retVal;
    }
    
    public static List<TransactionOutPoint> deserializeOutPoints(NetworkParameters params, List<byte[]> transactionOutPoints) {
        final List<TransactionOutPoint> retVal = new ArrayList<>(transactionOutPoints.size());
        for(final byte[] b:transactionOutPoints) {
            retVal.add(new TransactionOutPoint(params, b, 0));
        }
        return retVal;
    }
    
    public static List<TransactionSignature> deserializeSignatures(List<RefundP2shTO.TxSig> signatures) {
        final List<TransactionSignature> retVal = new ArrayList<>(signatures.size());
        for(RefundP2shTO.TxSig txsig:signatures) {
            retVal.add(new TransactionSignature(new BigInteger(txsig.clientSignatureR()), new BigInteger(txsig.clientSignatureS())));
        }
        return retVal;
    }
}
