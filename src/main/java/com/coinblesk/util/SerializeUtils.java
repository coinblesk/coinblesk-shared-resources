/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.BaseTO;
import com.coinblesk.json.TxSig;
import com.coinblesk.json.Type;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Thomas Bocek
 */
public class SerializeUtils {

    private final static Logger LOG = LoggerFactory.getLogger(SerializeUtils.class);
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static final Gson GSON;

    static {
        GSON = new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(byte[].class,
            new ByteArrayToBase64TypeAdapter()).create();
    }
    
    private final static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        Base64 b = new Base64(Integer.MAX_VALUE);

        @Override
        public JsonElement serialize(byte[] src, java.lang.reflect.Type typeOfSrc,
                JsonSerializationContext context) {
            return new JsonPrimitive(b.encodeToString(src));
        }

        @Override
        public byte[] deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            return b.decode(json.getAsString());
        }
        
        
    }

    public static <K extends BaseTO> K sign(K k, ECKey ecKey)  {
        k.messageSig(null);
        String json = GSON.toJson(k);
        Sha256Hash hash = canonicalizeJSONHash(json);
        LOG.debug("json sign serialized to: [{}]=hash:{}", json, hash);
        ECKey.ECDSASignature sig = ecKey.sign(hash);
        k.messageSig(new TxSig().sigR(sig.r.toString()).sigS(sig.s.toString()));
        return k;
    }
    
    public static Sha256Hash canonicalizeJSONHash(String json) {
        String lines[] = json.split("\\n");
        List<String> tmpLines = new ArrayList<>(lines.length);
        for(String line: lines) {
            line = line.trim();
            if(line.endsWith(",")) {
                line = line.substring(0, line.length() - 1);
            }
            tmpLines.add(line);
        }
        Collections.sort(tmpLines);
        StringBuilder sb = new StringBuilder();
        for(String line: tmpLines) {
            sb.append(line);
        }
        return Sha256Hash.wrap(Sha256Hash.hash(sb.toString().getBytes()));
    }

    public static <K extends BaseTO> boolean verifySig(K k, ECKey ecKey) {
        TxSig check = k.messageSig();
        ECKey.ECDSASignature sig = new ECKey.ECDSASignature(new BigInteger(check.sigR()), new BigInteger(
                check.sigS()));
        k.messageSig(null);
        String json = GSON.toJson(k);
        
        Sha256Hash hash = canonicalizeJSONHash(json);
        LOG.debug("json verify serialized to: [{}]=hash:{}", json, hash);
        return ecKey.verify(hash, sig);
    }

    public static boolean verifyTxSignatures(Transaction tx, List<TransactionSignature> sigs,
            Script redeemScript, ECKey serverPubKey) {
        final int len = tx.getInputs().size();
        if (sigs.size() != len) {
            return false;
        }
        for (int i = 0; i < len; i++) {
            final Sha256Hash sighash = tx.hashForSignature(i, redeemScript, Transaction.SigHash.ALL, false);
            TransactionSignature sig = sigs.get(i);
            if (!serverPubKey.verify(sighash, sig)) {
                return false;
            }
        }
        return true;
    }

    public static boolean verifyRefund(Transaction refund, Transaction... inputTxs) {
        return verifyRefund(refund, Arrays.asList(inputTxs));
    }

    public static boolean verifyRefund(Transaction refund, List<Transaction> inputTxs) {
        Map<Sha256Hash, Transaction> tmp = new HashMap<>();
        for (Transaction t : inputTxs) {
            tmp.put(t.getHash(), t);
        }
        return verifyRefund(refund, tmp);
    }
    public static boolean verifyRefund(Transaction refund, Map<Sha256Hash, Transaction> tmp) {
        try {
            for (TransactionInput refundInput : refund.getInputs()) {
                Transaction previous = tmp.get(refundInput.getOutpoint().getHash());
                if (previous == null) {
                    LOG.debug("no parent found for {}", refundInput.getOutpoint());
                    return false;
                }
                refundInput.verify(previous.getOutput(refundInput.getOutpoint().getIndex()));
            }
        } catch (VerificationException ve) {
            LOG.debug("verification exception", ve);
            return false;
        }
        return true;
    }

    public static List<TxSig> serializeSignatures(final List<TransactionSignature> signatures) {
        final List<TxSig> retVal = new ArrayList<>(signatures.size());
        for (final TransactionSignature signature : signatures) {
            retVal.add(new TxSig().sigR(
                    signature.r.toString()).sigS(signature.s.toString()));
        }
        return retVal;
    }

    public static List<TransactionSignature> deserializeSignatures(final List<TxSig> signatures) {
        final List<TransactionSignature> retVal = new ArrayList<>(signatures.size());
        for (final TxSig txsig : signatures) {
            retVal.add(new TransactionSignature(new BigInteger(txsig.sigR()), new BigInteger(txsig.sigS())));
        }
        return retVal;
    }

    public static List<byte[]> serializeOutPoints(final List<TransactionOutPoint> transactionOutPoints) {
        final List<byte[]> retVal = new ArrayList<>(transactionOutPoints.size());
        for (final TransactionOutPoint top : transactionOutPoints) {
            retVal.add(top.unsafeBitcoinSerialize());
        }
        return retVal;
    }

    public static List<TransactionOutPoint> deserializeOutPoints(final NetworkParameters params,
            final List<byte[]> transactionOutPoints) {
        final List<TransactionOutPoint> retVal = new ArrayList<>(transactionOutPoints.size());
        for (final byte[] b : transactionOutPoints) {
            retVal.add(new TransactionOutPoint(params, b, 0));
        }
        return retVal;
    }

    public static List<Pair<byte[], Long>> serializeOutPointsCoin(
            final List<Pair<TransactionOutPoint, Coin>> transactionOutPoints) {
        final List<Pair<byte[], Long>> retVal = new ArrayList<>(transactionOutPoints.size());
        for (final Pair<TransactionOutPoint, Coin> p : transactionOutPoints) {
            retVal.add(new Pair<>(p.element0().unsafeBitcoinSerialize(), p.element1().value));
        }
        return retVal;
    }

    public static List<Pair<TransactionOutPoint, Coin>> deserializeOutPointsCoin(
            final NetworkParameters params,
            final List<Pair<byte[], Long>> transactionOutPoints) {
        final List<Pair<TransactionOutPoint, Coin>> retVal = new ArrayList<>(transactionOutPoints.size());
        for (final Pair<byte[], Long> p : transactionOutPoints) {
            retVal.add(new Pair<>(
                    new TransactionOutPoint(params, p.element0(), 0), Coin.valueOf(p.element1())));
        }
        return retVal;
    }

    public static String bytesToHex(final byte[] bytes) {
        final int len = Math.min(8, bytes.length);
        final char[] hexChars = new char[len * 2];
        for (int j = 0; j < len; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    public static String bytesToHexFull(final byte[] bytes) {
        final int len = bytes.length;
        final char[] hexChars = new char[len * 2];
        for (int j = 0; j < len; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
