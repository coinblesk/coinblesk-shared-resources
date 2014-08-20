package ch.uzh.csg.mbps.keys;

import java.io.Serializable;

import ch.uzh.csg.mbps.customserialization.PKIAlgorithm;

/**
 * This class represents a stored (in a database or locally) custom public key,
 * consisting of a key number, a {@link PKIAlgorithm}, and a base64 encoded
 * public key.
 * 
 * @author Jeton Memeti
 * 
 */
public class CustomPublicKey implements Serializable {
	private static final long serialVersionUID = 6435515801039595212L;
	
	private byte keyNumber;
	private byte pkiAlgorithm;
	private String publicKey;
	
	public CustomPublicKey() {
	}
	
	public CustomPublicKey(byte keyNumber, byte pkiAlgorithm, String publicKey) {
		this.keyNumber = keyNumber;
		this.pkiAlgorithm = pkiAlgorithm;
		this.publicKey = publicKey;
	}

	public byte getKeyNumber() {
		return keyNumber;
	}

	public void setKeyNumber(byte keyNumber) {
		this.keyNumber = keyNumber;
	}

	public byte getPkiAlgorithm() {
		return pkiAlgorithm;
	}

	public void setPkiAlgorithm(byte pkiAlgorithm) {
		this.pkiAlgorithm = pkiAlgorithm;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void decode(String serverPublicKey) {
	    // TODO Auto-generated method stub
	    
    }
	
}
