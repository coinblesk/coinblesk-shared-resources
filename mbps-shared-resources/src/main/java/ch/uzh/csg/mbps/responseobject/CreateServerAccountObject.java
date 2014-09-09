package ch.uzh.csg.mbps.responseobject;

import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.keys.CustomPublicKey;

public class CreateServerAccountObject extends ServerAccountObject {

	private CustomPublicKey customPublicKey;
	
	public CreateServerAccountObject(){
		
	}
	
	public CreateServerAccountObject(String url ,String email){
		super(url,email);
	}
	
	public CustomPublicKey getCustomPublicKey() {
	    return customPublicKey;
    }

	public void setCustomPublicKey(CustomPublicKey customPublicKey) {
	    this.customPublicKey = customPublicKey;
    }
	
	public void encodeThis(JSONObject jsonObject) {
		if(customPublicKey != null) {
			jsonObject.put("keyNumber", customPublicKey.getKeyNumber());
			jsonObject.put("pkiAlgorithm", customPublicKey.getPkiAlgorithm());
			jsonObject.put("publicKey", customPublicKey.getPublicKey());
		}
	}
	
	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		encodeThis(jsonObject);
	}
	
	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		return decode(o);
	}
	
	public JSONObject decode(JSONObject o) {
		String keyNumber = toStringOrNull(o.get("keyNumber"));
		String pkiAlgorithm = toStringOrNull(o.get("pkiAlgorithm"));
		String publicKey = toStringOrNull(o.get("publicKey"));
		
		CustomPublicKey customPublicKey = new CustomPublicKey();
		if(keyNumber!=null) {
			try {
				customPublicKey.setKeyNumber(Byte.parseByte(keyNumber));
			} catch (NumberFormatException nfe) {
				customPublicKey.setKeyNumber((byte)-1);
			}
		} else {
			customPublicKey.setKeyNumber((byte)-2);
		}
		
		if(pkiAlgorithm!=null) {
			try {
				customPublicKey.setPkiAlgorithm(Byte.parseByte(pkiAlgorithm));
			} catch (NumberFormatException nfe) {
				customPublicKey.setPkiAlgorithm((byte)-1);
			}
		} else {
			customPublicKey.setPkiAlgorithm((byte)-2);
		}
		
		customPublicKey.setPublicKey(publicKey);
		setCustomPublicKey(customPublicKey);
		return o;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("id: ");
		sb.append(getId());
		sb.append(", url: ");
		sb.append(getUrl());
		sb.append(", email: ");
		sb.append(getEmail());
		sb.append(", number of keys: ");
		sb.append(getNOfKeys());
		sb.append(", balance limit: ");
		sb.append(getBalanceLimit());
		sb.append(", user balance limit: ");
		sb.append(getUserBalanceLimit());
		sb.append(", trust level: ");
		sb.append(getTrustLevel());
		sb.append("CPK: ");
		if(customPublicKey != null){
			sb.append("key number: ");			
			sb.append(customPublicKey.getKeyNumber());
			sb.append(", algorithm: ");
			sb.append(customPublicKey.getPkiAlgorithm());
			sb.append(", public key: ");
			sb.append(customPublicKey.getPublicKey());
		}
		return sb.toString();
	}
}