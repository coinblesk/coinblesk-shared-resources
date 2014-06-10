package ch.uzh.csg.mbps.keys;

//TODO jeton: javadoc
public class CustomKeyPair {
	
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
