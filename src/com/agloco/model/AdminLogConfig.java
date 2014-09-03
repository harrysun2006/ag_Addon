package com.agloco.model;

import java.io.Serializable;

public class AdminLogConfig implements Serializable
{
	
	public final static String ACTIVE   = "A";
	public final static String INACTIVE = "I";

	private Long id;
	private String className;
	private String method;
	private String script = "";
	private String active = ACTIVE;

	public String getScript()
	{
		return script;
	}

	public void setScript(String script)
	{
		this.script = script;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
