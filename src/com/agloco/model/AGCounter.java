package com.agloco.model;

import java.io.Serializable;

/**
 * 
 * @author terry_zhao
 *
 */
public class AGCounter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1934981836847302233L;
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
