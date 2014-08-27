package ch.uzh.csg.mbps.responseobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class TransferObject {
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public enum Status {
		REQUEST, REPLY_SUCCESS, REPLY_FAILED, UNAUTHORIZED;
	}

	private Status status = Status.REQUEST;
	private Integer version = 1;
	private String message = null;

	public boolean isSuccessful() {
		return status == Status.REPLY_SUCCESS;
	}

	public void setSuccessful(boolean successful) {
		this.status = successful ? Status.REPLY_SUCCESS : Status.REPLY_FAILED;
	}
	
	public void setUnauthorized() {
		this.status = Status.UNAUTHORIZED;
	}
	
	public boolean isUnauthorized() {
		return status == Status.UNAUTHORIZED;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = (JSONObject) JSONValue.parse(responseString);

		String message = toStringOrNull(o.get("message"));
		String status = toStringOrNull(o.get("status"));
		String version = toStringOrNull(o.get("version"));

		if (status != null) {
			setStatus(Status.valueOf(status));
		}
		try {
			setVersion(Integer.parseInt(version));
		} catch (Exception e) {
			e.printStackTrace();
			setVersion(-1);
		}

		setMessage(message);

		return o;
	}

	public void encode(JSONObject jsonObject) throws Exception {
		// mandatory
		jsonObject.put("status", status.toString());
		jsonObject.put("version", version);
		// optional
		if (message != null) {
			jsonObject.put("message", message);
		}
	}

	public static String toStringOrNull(Object o) {
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	public static BigDecimal toBigDecimalOrNull(Object o) {
		if (o == null) {
			return null;
		}
		try {
			String s = o.toString();
			int stop = s.indexOf("BTC");
			if(stop <= 0) {
				return null;
			}
			String s1 = s.substring(0, stop);
			BigDecimal v = new BigDecimal(s1);
			return v.setScale(8, RoundingMode.HALF_UP);			
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}

	public static JSONObject toJSONObjectOrNull(Object o) {
		if (o == null) {
			return null;
		}
		if (!(o instanceof JSONObject)) {
			return null;
		}
		return (JSONObject) o;
	}

	public static JSONArray toJSONArrayOrNull(Object o) {
		if (o == null) {
			return null;
		}
		if (!(o instanceof JSONArray)) {
			return null;
		}
		return (JSONArray) o;
	}

	public static Long toLongOrNull(Object o) {
		if (o == null) {
			return null;
		}
		String s1 = o.toString();
		try {
			return Long.parseLong(s1);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}

	public static Integer toIntOrNull(Object o) {
		if (o == null) {
			return null;
		}
		String s1 = o.toString();
		try {
			return Integer.parseInt(s1);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return null;
		}
	}

	public static Date toDateOrNull(Object o) {
		if (o == null) {
			return null;
		}
		String s1 = o.toString();
		try {
	        return DATE_FORMAT.parse(s1);
        } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
        }
    }
	
	public static String encodeToString(Date timestamp) {
		return DATE_FORMAT.format(timestamp);
    }
	
}
