package com.agloco.model;

import java.io.Serializable;
import java.util.Set;
import java.util.LinkedHashSet;

public class AGDictionary implements Serializable {

	private Long dictionaryId;
	private String code;
	private String value;
	private Double order;
	private Integer active;

	private Set details = new LinkedHashSet();

	public Long getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getOrder() {
		return order;
	}

	public void setOrder(Double order) {
		this.order = order;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Set getDetails() {
		return details;
	}

	public void setDetails(Set details) {
		this.details = details;
	}

}
