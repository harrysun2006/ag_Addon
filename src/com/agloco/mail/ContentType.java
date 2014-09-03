package com.agloco.mail;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentType implements Serializable {

	private static final long serialVersionUID = -6812211472618846797L;
	private Part.Type type;
	private Map list;
	private final static Pattern CONTENT_TYPE_PATTERN = Pattern.compile("(?:\\s*(\\w+)\\s*=\\s*\"*([^\";\\r\\n]*)\"*;*)|(?:\\s*[\\w-]+\\s*:\\s*)?([^;\\s\\r\\n]+)");

	public ContentType() {
	}

	public ContentType(String s, String s1, Map parameters) {
		type = new Part.Type(s + "/" + s1);
		list = parameters;
	}

	public ContentType(String s) {
		if(s == null) return;
		Matcher m = CONTENT_TYPE_PATTERN.matcher(s);
		while(m.find()) {
			if(m.group(1) != null && m.group(2) != null) {
				if(list == null) list = new Hashtable();
				list.put(m.group(1), m.group(2));
			}
			if(m.group(3) != null && type == null) type = new Part.Type(m.group(3));
		}
	}

	public String getPrimaryType() {
		return (type == null) ? null : type.getPrimaryType();
	}

	public String getSubType() {
		return (type == null) ? null : type.getSubType();
	}

	public String getBaseType() {
		return (type == null) ? null : type.toString();
	}

	public String getParameter(String s) {
		return (list == null) ? null : (String) list.get(s);
	}

	public Map getParameterList() {
		return list;
	}

	public Part.Type getType() {
		return this.type;
	}

	public void setType(Part.Type type) {
		this.type = type;
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

	public boolean match(ContentType ct) {
		String primaryType = type.getPrimaryType();
		String subType = type.getSubType();
		if (!primaryType.equalsIgnoreCase(ct.getPrimaryType())) return false;
		String s = ct.getSubType();
		if (subType.charAt(0) == '*' || s.charAt(0) == '*') return true;
		return subType.equalsIgnoreCase(s);
	}

	public boolean match(String s) {
		return match(new ContentType(s));
	}

}