package ch.uzh.csg.mbps.responseobject;

import java.math.BigDecimal;
import java.text.ParseException;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import org.apache.commons.codec.binary.Base64;

import ch.uzh.csg.mbps.keys.CustomPublicKey;

public class CustomResponseObject {
	private boolean successful;
	private String message;
	private ReadAccountTransferObject rato = null;
	private GetHistoryTransferObject ghto = null;
	private CreateTransactionTransferObject ctto = null;
	private PayOutRulesTransferObject porto = null;
	private byte[] serverPaymentResponse = null;
	private String balance = null;
	private CustomPublicKey serverPublicKey;
	private Type type;
	private int clientVersion;
	
	public enum Type {
		LOGIN, LOGOUT, EXCHANGE_RATE, PAYOUT_RULE, HISTORY_EMAIL, AFTER_LOGIN, SAVE_PUBLIC_KEY, MAIN_ACTIVITY, CREATE_TRANSACTION, PAYOUT_ERROR_BALANCE, PAYOUT_ERROR_ADDRESS, OTHER ;
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

	public CustomPublicKey getServerPublicKey() {
		return serverPublicKey;
	}

	public void setServerPublicKey(CustomPublicKey serverPublicKey) {
		this.serverPublicKey = serverPublicKey;
	}

	public PayOutRulesTransferObject getPayOutRulesTO() {
		return porto;
	}

	public void setPayOutRulesTO(PayOutRulesTransferObject porto) {
		this.porto = porto;
	}

	public byte[] getServerPaymentResponse() {
		return serverPaymentResponse;
	}

	public void setServerPaymentResponse(byte[] serverPaymentResponse) {
		this.serverPaymentResponse = serverPaymentResponse;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public void setClientVersion(int version) {
		this.clientVersion = version;
	}
	
	public int getClientVersion() {
		return clientVersion;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("successful: ");
		sb.append(isSuccessful());
		sb.append(", message: ");
		sb.append(getMessage());
		
		return sb.toString();
	}

	public void decode(String responseString) throws ParseException {
		if(responseString == null) {
			return;
		}
		JSONObject o = (JSONObject) JSONValue.parse(responseString);
		//mandatory
    	String message = toStringOrNull(o.get("message"));
    	String successful = toStringOrNull(o.get("successful"));
    	String type = toStringOrNull(o.get("type"));
    	String version = toStringOrNull(o.get("clientVersion"));
    	//optional
    	String balance = toStringOrNull(o.get("balance"));
    	String serverPaymentResponse = toStringOrNull(o.get("serverPaymentResponse"));
    	
    	String createTransactionTO = toStringOrNull(o.get("createTransactionTO"));
    	String getHistoryTO = toStringOrNull(o.get("getHistoryTO"));
    	String payOutRulesTO = toStringOrNull(o.get("payOutRulesTO"));
    	String readAccountTO = toStringOrNull(o.get("ReadAccountTO"));
    	String serverPublicKey = toStringOrNull(o.get("serverPublicKey"));
    	
    	//mandatory
    	setMessage(message);
    	setSuccessful(Boolean.valueOf(successful));
    	setType(Type.valueOf(type));
    	try {
    		setClientVersion(Integer.parseInt(version));
    	} catch (Exception e) {
			e.printStackTrace();
		}
    
    	//optional
    	setBalance(balance);
    	byte[] decode = Base64.decodeBase64(serverPaymentResponse.getBytes());
    	setServerPaymentResponse(decode);
    	//
    	CreateTransactionTransferObject ctto = new CreateTransactionTransferObject();
    	ctto.decode(createTransactionTO);
    	setCreateTransactionTO(ctto);
    	//
    	GetHistoryTransferObject ghto = new GetHistoryTransferObject();
    	ghto.decode(getHistoryTO);
    	setGetHistoryTO(ghto);
    	//
    	PayOutRulesTransferObject porto = new PayOutRulesTransferObject();
    	porto.decode(payOutRulesTO);
    	setPayOutRulesTO(porto);
    	//
    	ReadAccountTransferObject rato = new ReadAccountTransferObject();
    	rato.decode(readAccountTO);
    	setReadAccountTO(rato);
    	//
    	CustomPublicKey c = new CustomPublicKey();
    	c.decode(serverPublicKey);
    	setServerPublicKey(c);
    }
	
	public static String toStringOrNull(Object o) {
		if(o == null) {
			return null;
		}
		return o.toString();
	}
	
	public static BigDecimal toBigDecimalOrNull(Object o) {
		if(o == null) {
			return null;
		}
		try {
			return new BigDecimal(o.toString());
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}
	
}
