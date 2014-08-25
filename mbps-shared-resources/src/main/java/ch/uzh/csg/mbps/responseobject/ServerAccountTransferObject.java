package ch.uzh.csg.mbps.responseobject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import ch.uzh.csg.mbps.model.ServerAccount;

public class ServerAccountTransferObject extends TransferObject {
	private List<ServerAccount> serverAccountList;
	private Long numberofSA;
	
	public ServerAccountTransferObject(){
	}
	
	public ServerAccountTransferObject(List<ServerAccount> serverAccounts, Long numberofSA) {
		this.serverAccountList = serverAccounts;
		this.numberofSA = numberofSA;
	}
	
	public List<ServerAccount> getServerAccountList() {
		return serverAccountList;
	}

	public void setServerAccountList(List<ServerAccount> list) {
		this.serverAccountList = list;
	}
	
	public Long getNumberOfSA(){
		return numberofSA;
	}
	
	public void setNumberOfSA(Long nofSA){
		this.numberofSA = nofSA;
	}
	
	@Override
	public JSONObject decode(String responseString) throws Exception {
	    if(responseString == null) {
	    	return null;
	    }
	    super.decode(responseString);
		JSONObject o = (JSONObject) JSONValue.parse(responseString);
		decode(o);
		return o;
    }

	private void decode(JSONObject o) throws ParseException {
		setNumberOfSA(toLongOrNull(o.get("numberofSA")));
		
		JSONArray array = toJSONArrayOrNull(o.get("serverAccountList"));
		ArrayList<ServerAccount> serverAccount = new ArrayList<ServerAccount>();
		if(array!=null){	
			for(Object o2:array) {
				JSONObject o3 = (JSONObject) o2;
				ServerAccount sa = new ServerAccount();
				sa.decode(o3);
				serverAccount.add(sa);
			}
		}
		setServerAccountList(serverAccount);
	}
	
	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		encodeThis(jsonObject);
	}
	
	public void encodeThis(JSONObject jsonObject) throws Exception {
		if(numberofSA!=null) {
			jsonObject.put("numberofSA", numberofSA);
		}
		
		if(serverAccountList!=null){			
			JSONArray array = new JSONArray();
			for(ServerAccount sa: serverAccountList) {
				JSONObject o = new JSONObject();
				sa.encode(o);
				array.add(o);
			}
			jsonObject.put("serverAccountList", array); 
		}
    }
}
