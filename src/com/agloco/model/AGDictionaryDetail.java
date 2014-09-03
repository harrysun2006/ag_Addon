package com.agloco.model;

import java.io.Serializable;

public class AGDictionaryDetail implements Serializable {

	private Long dictionaryDetailId;
	private Long dictionaryId;
	private String language;
	private String country;
	private String text;

	public Long getDictionaryDetailId() {
		return dictionaryDetailId;
	}

	public void setDictionaryDetailId(Long dictionaryDetailId) {
		this.dictionaryDetailId = dictionaryDetailId;
	}

	public Long getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(Long dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String locale) {
		this.country = locale;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
