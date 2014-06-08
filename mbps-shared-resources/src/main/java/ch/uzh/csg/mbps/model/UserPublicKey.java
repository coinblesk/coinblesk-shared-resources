package ch.uzh.csg.mbps.model;

//TODO jeton: javadoc
public class UserPublicKey {
	
	private byte pkiAlgorithm;
	private String publicKey;
	
	public UserPublicKey() {
	}
	
	public UserPublicKey(byte pkiAlgorithm, String publicKey) {
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
