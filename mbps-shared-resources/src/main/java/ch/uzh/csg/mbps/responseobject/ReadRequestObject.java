package ch.uzh.csg.mbps.responseobject;

import net.minidev.json.JSONObject;

public class ReadRequestObject extends TransferObject {
	
	private CustomPublicKeyObject customPublicKey;
	private UserAccountObject userAccount;

	public CustomPublicKeyObject getCustomPublicKey() {
	    return customPublicKey;
    }

	public void setCustomPublicKey(CustomPublicKeyObject customPublicKey) {
	    this.customPublicKey = customPublicKey;
    }

	public UserAccountObject getUserAccount() {
	    return userAccount;
    }

	public void setUserAccount(UserAccountObject userAccount) {
	    this.userAccount = userAccount;
    }
	
	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if(customPublicKey != null) {
			JSONObject jsonObject2 = new JSONObject();
			customPublicKey.encodeThis(jsonObject2);
			jsonObject.put("customPublicKey", jsonObject2);
		}
		if(userAccount !=null) {
			JSONObject jsonObject2 = new JSONObject();
			userAccount.encodeThis(jsonObject2);
			jsonObject.put("userAccount", jsonObject2);
		}
	}
	
	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		
		JSONObject o2 = toJSONObjectOrNull(o.get("customPublicKey"));
		if(o2!=null) {
			CustomPublicKeyObject customPublicKeyObject = new CustomPublicKeyObject();
			customPublicKeyObject.decode(o2);
			setCustomPublicKey(customPublicKeyObject);
		}
		
		JSONObject o3 = toJSONObjectOrNull(o.get("userAccount"));
		if(o3!=null) {
			UserAccountObject userAccount = new UserAccountObject();
			userAccount.decode(o3);
			setUserAccount(userAccount);
		}
 		
		return o;
	}
}
