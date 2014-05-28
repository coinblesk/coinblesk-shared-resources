package ch.uzh.csg.mbps.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignedObject;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

import ch.uzh.csg.mbps.model.Transaction;

//TODO jeton: to be replaced by ch.uzh.csg.mbps.security.KeyHandler
public class KeyHandler {
	private static final String ALGORITHM = "RSA";
	private static final String HASH_ALGORITHM = "SHA1withRSA";
	
	public static KeyPair generateKeys() throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		return keyGen.generateKeyPair();
	}

	public static String encodePrivateKey(PrivateKey privateKey) {
		byte[] privateEncoded = Base64.encodeBase64(privateKey.getEncoded());
		return new String(privateEncoded);
	}

	public static String encodePublicKey(PublicKey publicKey) {
		byte[] publicEncoded = Base64.encodeBase64(publicKey.getEncoded());
		return new String(publicEncoded);
	}

	public static PublicKey decodePublicKey(String publicKeyEncoded) throws Exception {
		byte[] decoded = Base64.decodeBase64(publicKeyEncoded.getBytes());
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoded);
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return keyFactory.generatePublic(publicKeySpec);
	}

	public static PrivateKey decodePrivateKey(String privateKeyEncoded) throws Exception {
		byte[] decoded = Base64.decodeBase64(privateKeyEncoded.getBytes());
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoded);
		
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		return keyFactory.generatePrivate(privateKeySpec);
	}
	
	public static String encodeSignedObject(SignedObject object) throws Exception {
		byte[] encoded = Base64.encodeBase64(Serializer.serialize(object));
		return new String(encoded);
	}
	
	public static SignedObject decodeSignedObject(String encoded) throws Exception {
		byte[] decoded = Base64.decodeBase64(encoded.getBytes());
		return Serializer.deserialize(decoded);
	}

	public static SignedObject signTransaction(Transaction unsignedObject, String privateKeyEncoded) throws Exception {
		PrivateKey privateKey = decodePrivateKey(privateKeyEncoded);
		return signTransaction(unsignedObject, privateKey);
	}
	
	public static SignedObject signTransaction(Transaction unsignedObject, PrivateKey privateKey) throws Exception {
		Signature signature = Signature.getInstance(HASH_ALGORITHM);
		return new SignedObject(unsignedObject, privateKey, signature);
	}

	public static boolean verifyObject(SignedObject signedObject, String publicKeyEncoded) throws Exception {
		PublicKey publicKey = decodePublicKey(publicKeyEncoded);
		return verifyObject(signedObject, publicKey);
	}
	
	public static boolean verifyObject(SignedObject signedObject, PublicKey publicKey) throws Exception {
		Signature signature = Signature.getInstance(HASH_ALGORITHM);
		return signedObject.verify(publicKey, signature);
	}

	public static Transaction retrieveTransaction(SignedObject signedObject) throws Exception {
		return (Transaction) signedObject.getObject();
	}
	
}
