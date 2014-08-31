package ch.uzh.csg.mbps.responseobject;

import ch.uzh.csg.mbps.customserialization.exceptions.IllegalArgumentException;
import net.minidev.json.JSONObject;

public class HistoryTransferRequestObject extends TransferObject {

	private Integer txPage;
	private Integer txPayInPage;
	private Integer txPayInUnverifiedPage;
	private Integer txPayOutPage;

	public Integer getTxPage() {
		return txPage;
	}

	public void setTxPage(Integer txPage) {
		this.txPage = txPage;
	}

	public Integer getTxPayInPage() {
		return txPayInPage;
	}

	public void setTxPayInUnverifiedPage(Integer txPayInUnverifiedPage) {
		this.txPayInUnverifiedPage = txPayInUnverifiedPage;
	}
	
	public Integer getTxPayInUnverifiedPage() {
		return txPayInUnverifiedPage;
	}

	public void setTxPayInPage(Integer txPayInPage) {
		this.txPayInPage = txPayInPage;
	}

	public Integer getTxPayOutPage() {
		return txPayOutPage;
	}

	public void setTxPayOutPage(Integer txPayOutPage) {
		this.txPayOutPage = txPayOutPage;
	}

	public boolean isComplete() {
		return txPage != null && txPayInPage != null && txPayOutPage != null && txPayInUnverifiedPage != null;
	}

	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		return decode(o);
	}

	public JSONObject decode(JSONObject o) {
		setTxPage(toIntOrNull(o.get("txPage")));
		setTxPayInPage(toIntOrNull(o.get("txPayInPage")));
		setTxPayInUnverifiedPage(toIntOrNull(o.get("txPayInUnverifiedPage")));
		setTxPayOutPage(toIntOrNull(o.get("txPayOutPage")));
		return o;
	}

	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if (!isComplete()) {
			throw new IllegalArgumentException("HistoryTransferRequestObject not completely set");
		}
		
		jsonObject.put("txPage", txPage);
		jsonObject.put("txPayInPage", txPayInPage);
		jsonObject.put("txPayInUnverifiedPage", txPayInUnverifiedPage);
		jsonObject.put("txPayOutPage", txPayOutPage);
	}
}
