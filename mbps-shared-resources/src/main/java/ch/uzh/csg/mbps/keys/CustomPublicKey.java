package ch.uzh.csg.mbps.keys;

import java.io.Serializable;

//TODO jeton: javadoc
public class CustomPublicKey implements Serializable {
	private static final long serialVersionUID = 6435515801039595212L;
	
	private byte pkiAlgorithm;
	private String publicKey;
	
	public CustomPublicKey() {
	}
	
	public CustomPublicKey(byte pkiAlgorithm, String publicKey) {
		this.pkiAlgorithm = pkiAlgorithm;
		this.publicKey = publicKey;
	}

	public byte getPKIAlgorithm() {
		return pkiAlgorithm;
	}

	public void setPKIAlgorithm(byte pkiAlgorithm) {
		this.pkiAlgorithm = pkiAlgorithm;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

}
