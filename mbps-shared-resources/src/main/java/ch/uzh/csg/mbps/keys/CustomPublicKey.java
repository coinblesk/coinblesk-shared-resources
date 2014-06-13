package ch.uzh.csg.mbps.keys;

import java.io.Serializable;

//TODO jeton: javadoc
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
	
}
