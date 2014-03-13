package ch.uzh.csg.mbps.responseobject;

import java.util.ArrayList;

import ch.uzh.csg.mbps.model.PayOutRule;

public class PayOutRulesTransferObject {
	private ArrayList<PayOutRule> payOutRulesList;

	public PayOutRulesTransferObject(){
		payOutRulesList = new ArrayList<PayOutRule>();
	}
	
	public ArrayList<PayOutRule> getPayOutRulesList() {
		return payOutRulesList;
	}

	public void setPayOutRulesList(ArrayList<PayOutRule> list) {
		this.payOutRulesList = list;
	}
	
	public void setPayOutRule(PayOutRule rule){
		this.payOutRulesList.add(rule);
	}

}
