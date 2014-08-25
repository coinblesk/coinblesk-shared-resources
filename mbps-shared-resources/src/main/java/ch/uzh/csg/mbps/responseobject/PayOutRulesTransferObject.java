package ch.uzh.csg.mbps.responseobject;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.model.PayOutRule;

public class PayOutRulesTransferObject extends TransferObject {
	private List<PayOutRule> payOutRulesList;
	
	public List<PayOutRule> getPayOutRulesList() {
		return payOutRulesList;
	}

	public void setPayOutRulesList(List<PayOutRule> list) {
		this.payOutRulesList = list;
	}

	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if(payOutRulesList != null) {
			JSONArray array = new JSONArray();
			for(PayOutRule payOutRule: payOutRulesList) {
				JSONObject o = new JSONObject();
				payOutRule.encode(o);
				array.add(o);
			}
			jsonObject.put("payOutRulesList", array);
		}
	}
		
	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		JSONArray a = toJSONArrayOrNull(o.get("payOutRulesList"));
		if(a!=null) {
			List<PayOutRule> payOutRulesList = new ArrayList<PayOutRule>();
			for(Object o2:a) {
				if(o2 instanceof JSONObject) {
					PayOutRule payOutRule = new PayOutRule();
					payOutRule.decode((JSONObject) o2);
					payOutRulesList.add(payOutRule);
				}
			}
			setPayOutRulesList(payOutRulesList);
		}
		return o;
	}

	

}
