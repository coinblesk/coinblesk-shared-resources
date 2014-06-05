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
	
	public enum Type {
		LOGIN, LOGOUT, EXCHANGE_RATE, PAYOUT_RULE, HISTORY_EMAIL, OTHER;
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

	public ReadAccountTransferObject getRato() {
		return rato;
	}

	public void setRato(ReadAccountTransferObject rato) {
		this.rato = rato;
	}

	public GetHistoryTransferObject getGhto() {
		return ghto;
	}

	public void setGhto(GetHistoryTransferObject ghto) {
		this.ghto = ghto;
	}

	public CreateTransactionTransferObject getCtto() {
		return ctto;
	}

	public void setCtto(CreateTransactionTransferObject ctto) {
		this.ctto = ctto;
	}

	public String getEncodedServerPublicKey() {
		return encodedServerPublicKey;
	}

	public void setEncodedServerPublicKey(String encodedServerPublicKey) {
		this.encodedServerPublicKey = encodedServerPublicKey;
	}

	public PayOutRulesTransferObject getPorto() {
		return porto;
	}

	public void setPorto(PayOutRulesTransferObject porto) {
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
