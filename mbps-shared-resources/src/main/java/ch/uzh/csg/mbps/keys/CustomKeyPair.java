package ch.uzh.csg.mbps.keys;

import java.io.Serializable;

//TODO jeton: javadoc
public class CustomKeyPair implements Serializable {
	private static final long serialVersionUID = 8331342730288174626L;
	
	private byte pkiAlgorithm;
	private byte keyNumber;
	private String publicKey;
	private String privateKey;
	
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
	
}
