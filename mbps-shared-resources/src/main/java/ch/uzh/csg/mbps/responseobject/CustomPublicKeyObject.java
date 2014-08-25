package ch.uzh.csg.mbps.responseobject;

import ch.uzh.csg.mbps.keys.CustomPublicKey;
import net.minidev.json.JSONObject;

public class CustomPublicKeyObject extends TransferObject {
	
	private CustomPublicKey customPublicKey;
	
	public CustomPublicKey getCustomPublicKey() {
	    return customPublicKey;
    }

	public void setCustomPublicKey(CustomPublicKey customPublicKey) {
	    this.customPublicKey = customPublicKey;
    }

	

	public void encodeThis(JSONObject jsonObject) throws Exception {
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

	
}
