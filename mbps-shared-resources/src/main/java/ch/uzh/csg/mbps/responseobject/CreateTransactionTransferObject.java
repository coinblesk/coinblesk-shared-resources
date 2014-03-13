package ch.uzh.csg.mbps.responseobject;

import java.io.Serializable;
import java.security.SignedObject;

import org.codehaus.jackson.annotate.JsonIgnore;

import ch.uzh.csg.mbps.util.KeyHandler;

public class CreateTransactionTransferObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sellerBase64RawSignedObject;
	private String buyerBase64RawSignedObject;
	
	public CreateTransactionTransferObject() {
	}
	
	public CreateTransactionTransferObject(String sellerSignedObject, String buyerSignedObjet) {
		this.sellerBase64RawSignedObject = sellerSignedObject;
		this.buyerBase64RawSignedObject = buyerSignedObjet;
	}
	
	public CreateTransactionTransferObject(SignedObject one, SignedObject two) throws Exception {
		this.sellerBase64RawSignedObject = KeyHandler.encodeSignedObject(one);
		this.buyerBase64RawSignedObject = KeyHandler.encodeSignedObject(two);
	}
	
	public String getSellerBase64RawSignedObject() {
		return sellerBase64RawSignedObject;
	}

	public void setSellerBase64RawSignedObject(String sellerBase64RawSignedObject) {
		this.sellerBase64RawSignedObject = sellerBase64RawSignedObject;
	}

	public String getBuyerBase64RawSignedObject() {
		return buyerBase64RawSignedObject;
	}

	public void setBuyerBase64RawSignedObject(String buyerBase64RawSignedObject) {
		this.buyerBase64RawSignedObject = buyerBase64RawSignedObject;
	}
	
	
	@JsonIgnore
	public SignedObject getSellerSignedObject() throws Exception {
		return KeyHandler.decodeSignedObject(sellerBase64RawSignedObject);
	}
	
	@JsonIgnore
	public SignedObject getBuyerSignedObject() throws Exception {
		return KeyHandler.decodeSignedObject(buyerBase64RawSignedObject);
	}
}
