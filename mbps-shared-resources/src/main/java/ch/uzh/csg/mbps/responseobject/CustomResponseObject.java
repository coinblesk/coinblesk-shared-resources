package ch.uzh.csg.mbps.responseobject;

import ch.uzh.csg.mbps.customserialization.ServerPaymentResponse;

public class CustomResponseObject {
	private boolean successful;
	private String message;
	private ReadAccountTransferObject rato = null;
	private GetHistoryTransferObject ghto = null;
	private CreateTransactionTransferObject ctto = null;
	private String encodedServerPublicKey = null;
	private PayOutRulesTransferObject porto = null;
	private ServerPaymentResponse spr = null;
	private Type type;
	
	//TODO jeton: refactor! use type to have information on how to cast Object (boolean successful, Type type, Object object.
	
	public enum Type {
		LOGIN, LOGOUT, EXCHANGE_RATE, PAYOUT_RULE, HISTORY_EMAIL, PUBLIC_KEY_SAVED, OTHER;
	}
	
	public CustomResponseObject() {
	}
	
	public CustomResponseObject(boolean successful, String message){
		this.successful = successful;
		this.message = message;
		this.type = Type.OTHER;
	}
	
	public CustomResponseObject(boolean successful, String message, Type type){
		this.successful = successful;
		this.message = message;
		this.type = type;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReadAccountTransferObject getReadAccountTO() {
		return rato;
	}

	public void setReadAccountTO(ReadAccountTransferObject rato) {
		this.rato = rato;
	}

	public GetHistoryTransferObject getGetHistoryTO() {
		return ghto;
	}

	public void setGetHistoryTO(GetHistoryTransferObject ghto) {
		this.ghto = ghto;
	}

	public CreateTransactionTransferObject getCreateTransactionTO() {
		return ctto;
	}

	public void setCreateTransactionTO(CreateTransactionTransferObject ctto) {
		this.ctto = ctto;
	}

	public String getEncodedServerPublicKey() {
		return encodedServerPublicKey;
	}

	public void setEncodedServerPublicKey(String encodedServerPublicKey) {
		this.encodedServerPublicKey = encodedServerPublicKey;
	}

	public PayOutRulesTransferObject getPayOutRulesTO() {
		return porto;
	}

	public void setPayOutRulesTO(PayOutRulesTransferObject porto) {
		this.porto = porto;
	}

	public ServerPaymentResponse getServerPaymentResponse() {
		return spr;
	}

	public void setServerPaymentResponse(ServerPaymentResponse spr) {
		this.spr = spr;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("successful: ");
		sb.append(isSuccessful());
		sb.append(", message: ");
		sb.append(getMessage());
		
		return sb.toString();
	}
	
}
