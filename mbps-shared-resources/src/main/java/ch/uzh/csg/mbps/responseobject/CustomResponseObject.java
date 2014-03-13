package ch.uzh.csg.mbps.responseobject;

public class CustomResponseObject {
	private boolean successful;
	private String message;
	private ReadAccountTransferObject rato = null;
	private GetHistoryTransferObject ghto = null;
	private CreateTransactionTransferObject ctto = null;
	private String encodedServerPublicKey = null;
	private PayOutRulesTransferObject porto = null;
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
		setSuccessful(successful);
		setMessage(message);
		setType(type);
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
	
	public GetHistoryTransferObject getGetHistoryTO() {
		return ghto;
	}
	
	public CreateTransactionTransferObject getCreateTransactionTO() {
		return ctto;
	}
	
	public void setReadAccountTO(ReadAccountTransferObject rato) {
		this.rato = rato;
	}
	
	public void setGetHistoryTO(GetHistoryTransferObject ghto) {
		this.ghto = ghto;
	}
	
	public void setCreateTransactionTO(CreateTransactionTransferObject ctto) {
		this.ctto = ctto;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type){
		this.type = type;
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("successful: ");
		sb.append(isSuccessful());
		sb.append(", message: ");
		sb.append(getMessage());
		
		return sb.toString();
	}
	
}
