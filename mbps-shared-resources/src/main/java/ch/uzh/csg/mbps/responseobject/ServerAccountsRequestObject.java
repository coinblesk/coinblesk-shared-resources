package ch.uzh.csg.mbps.responseobject;

import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.customserialization.exceptions.IllegalArgumentException;

public class ServerAccountsRequestObject extends TransferObject {

	private Integer urlPage;
	
	public Integer getUrlPage() {
		return urlPage;
	}

	public void setUrlPage(Integer urlPage) {
		this.urlPage = urlPage;
	}
	
	public boolean isComplete() {
		return urlPage != null;
	}

	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		return decode(o);
	}

	public JSONObject decode(JSONObject o) {
		setUrlPage(toIntOrNull(o.get("urlPage")));
		return o;
	}

	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if (!isComplete()) {
			throw new IllegalArgumentException("ServerAccountsRequestObject not completely set");
		}
		
		jsonObject.put("urlPage", urlPage);
	}
}