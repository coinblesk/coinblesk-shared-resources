package ch.uzh.csg.mbps.responseobject;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.annotate.JsonIgnore;

import ch.uzh.csg.mbps.customserialization.DecoderFactory;
import ch.uzh.csg.mbps.customserialization.ServerPaymentRequest;
import ch.uzh.csg.mbps.customserialization.ServerPaymentResponse;
import ch.uzh.csg.mbps.customserialization.exceptions.IllegalArgumentException;
import ch.uzh.csg.mbps.customserialization.exceptions.NotSignedException;
import ch.uzh.csg.mbps.customserialization.exceptions.SerializationException;

public class CreateTransactionTransferObject implements Serializable {
	private static final long serialVersionUID = 5151699286970960013L;
	
	private String payloadBase64;
	
	public CreateTransactionTransferObject() {
	}
	
	public CreateTransactionTransferObject(ServerPaymentRequest spr) throws IllegalArgumentException, NotSignedException {
		if (spr == null)
			throw new IllegalArgumentException("The payload cannot be null.");
		
		byte[] encodeBase64 = Base64.encodeBase64(spr.encode());
		this.payloadBase64 = new String(encodeBase64);
	}
	
	public CreateTransactionTransferObject(ServerPaymentResponse spr) throws IllegalArgumentException, NotSignedException {
		if (spr == null)
			throw new IllegalArgumentException("The payload cannot be null.");
		
		byte[] encodeBase64 = Base64.encodeBase64(spr.encode());
		this.payloadBase64 = new String(encodeBase64);
	}
	
	public String getPayloadBase64() {
		return payloadBase64;
	}
	
	public void setPayload(String payloadBase64) {
		this.payloadBase64 = payloadBase64;
	}
	
	@JsonIgnore
	public ServerPaymentRequest getServerPaymentRequest() throws IllegalArgumentException, SerializationException {
		byte[] decode = Base64.decodeBase64(payloadBase64.getBytes());
		return DecoderFactory.decode(ServerPaymentRequest.class, decode);
	}
	
	@JsonIgnore
	public ServerPaymentResponse getServerPaymentResponse() throws IllegalArgumentException, SerializationException {
		byte[] decode = Base64.decodeBase64(payloadBase64.getBytes());
		return DecoderFactory.decode(ServerPaymentResponse.class, decode);
	}
	
}
