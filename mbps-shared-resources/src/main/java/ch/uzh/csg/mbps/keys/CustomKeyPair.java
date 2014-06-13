package ch.uzh.csg.mbps.keys;

import java.io.Serializable;

import ch.uzh.csg.mbps.customserialization.PKIAlgorithm;

/**
 * This class represents a stored (in a database or locally) custom key pair,
 * consisting of a key number, a {@link PKIAlgorithm}, and base64 encoded public
 * and private key.
 * 
 * @author Jeton Memeti
 * 
 */
public class CustomKeyPair implements Serializable {
	private static final long serialVersionUID = 8331342730288174626L;
	
	private byte pkiAlgorithm;
	private byte keyNumber;
	private String publicKey;
	private String privateKey;
	
	public CustomKeyPair() {
	}
	
	public CustomKeyPair(byte pkiAlgorithm, byte keyNumber, String publicKey, String privateKey) {
		this.pkiAlgorithm = pkiAlgorithm;
		this.keyNumber = keyNumber;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public byte getPkiAlgorithm() {
		return pkiAlgorithm;
	}

	public byte getKeyNumber() {
		return keyNumber;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPkiAlgorithm(byte pkiAlgorithm) {
		this.pkiAlgorithm = pkiAlgorithm;
	}

	public void setKeyNumber(byte keyNumber) {
		this.keyNumber = keyNumber;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
}
