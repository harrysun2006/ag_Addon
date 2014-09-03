package com.agloco.mail;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentDisposition implements Serializable {

	private static final long serialVersionUID = -9183832293740238424L;
	private Part.Type type;
	private Map list;
	private final static Pattern CONTENT_DISPOSITION_PATTERN = Pattern.compile("(?:\\s*(\\w+)\\s*=\\s*\"*([^\";\\r\\n]*)\"*;*)|(?:\\s*[\\w-]+\\s*:\\s*)?([^;\\s\\r\\n]+)");

	public ContentDisposition() {
	}

	public ContentDisposition(String s, Map parameters) {
		type = Part.Type.valueOf(s);
		list = parameters;
	}

	public ContentDisposition(String s) {
		if(s == null) return;
		Matcher m = CONTENT_DISPOSITION_PATTERN.matcher(s);
		while(m.find()) {
			if(m.group(1) != null && m.group(2) != null) {
				if(list == null) list = new Hashtable();
				list.put(m.group(1), m.group(2));
			}
			if(m.group(3) != null && type == null) type = Part.Type.valueOf(m.group(3));
		}
	}

	public String getDisposition() {
		return type.toString();
	}

	public String getParameter(String s) {
		return (list == null) ? null : (String) list.get(s);
	}

	public Map getParameterList() {
		return list;
	}

	public void setDisposition(String s) {
		type = Part.Type.valueOf(s);
	}

	public void setParameter(String name, String value) {
		if (list == null) list = new Hashtable();
		list.put(name, value);
	}

	public void setParameterList(Map parameters) {
		list = parameters;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (type != null) sb.append(type).append("; ");
		if (list != null) {
			String key, value;
			for(Iterator it = list.keySet().iterator(); it.hasNext(); ) {
				key = (String) it.next();
				value = (String) list.get(key);
				sb.append(key).append("=").append(value).append(";");
			}
		}
		return sb.toString();
	}

}
