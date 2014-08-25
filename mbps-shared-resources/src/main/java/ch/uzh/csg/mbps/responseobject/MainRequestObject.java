package ch.uzh.csg.mbps.responseobject;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minidev.json.JSONObject;

public class MainRequestObject extends TransactionObject {
	private BigDecimal exchangeRate;
	private String otherCurrency;
	private UserAccountObject userAccountObject;
	private GetHistoryTransferObject getHistoryTransferObject;

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate, String otherCurrency) {
		this.exchangeRate = exchangeRate;
		this.otherCurrency = otherCurrency;
	}

	public UserAccountObject getUserAccount() {
		return userAccountObject;
	}

	public void setUserAccount(UserAccountObject userAccountObject) {
		this.userAccountObject = userAccountObject;
	}

	public GetHistoryTransferObject getGetHistoryTransferObject() {
		return getHistoryTransferObject;
	}

	public void setGetHistoryTransferObject(GetHistoryTransferObject getHistoryTransferObject) {
		this.getHistoryTransferObject = getHistoryTransferObject;
	}

	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if (exchangeRate != null && getOtherCurrency() != null) {
			jsonObject.put("exchangeRate", exchangeRate + "BTC|" + otherCurrency);
		}
		if (userAccountObject != null) {
			JSONObject jsonObject2 = new JSONObject();
			userAccountObject.encodeThis(jsonObject2);
			jsonObject.put("userAccount", jsonObject2);
		}
		if (getHistoryTransferObject != null) {
			JSONObject jsonObject2 = new JSONObject();
			getHistoryTransferObject.encodeThis(jsonObject2);
			jsonObject.put("getHistoryTransfer", jsonObject2);
		}
	}

	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);

		String exchangeRate = toStringOrNull(o.get("exchangeRate"));
		if (exchangeRate != null) {

			Pattern pattern = Pattern.compile("[A-Za-z]");
			Matcher matcher = pattern.matcher(exchangeRate);
			if (matcher.find()) {
				int start = matcher.start();
				if (start >= 0) {
					String number = exchangeRate.substring(0, start);
					String currencies = exchangeRate.substring(start);
					String[] cur = currencies.split("[|]");
					if (cur.length == 2) {
						// TODO: scale
						try {
							setExchangeRate(new BigDecimal(number), cur[1]);
						} catch (NumberFormatException nfe) {
							setExchangeRate(BigDecimal.ZERO, "ERR");
						}
					}
				}
			}

		}

		JSONObject o2 = toJSONObjectOrNull(o.get("userAccount"));
		if (o2 != null) {
			UserAccountObject userAccount = new UserAccountObject();
			userAccount.decode(o2);
			setUserAccount(userAccount);
		}

		JSONObject o3 = toJSONObjectOrNull(o.get("getHistoryTransfer"));
		if (o3 != null) {
			GetHistoryTransferObject getHistoryTransferObject = new GetHistoryTransferObject();
			getHistoryTransferObject.decode(o3);
			setGetHistoryTransferObject(getHistoryTransferObject);
		}

		return o;
	}

	public String getOtherCurrency() {
	    return otherCurrency;
    }

	public void setOtherCurrency(String otherCurrency) {
	    this.otherCurrency = otherCurrency;
    }
}
