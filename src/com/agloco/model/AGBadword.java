package com.agloco.model;

import java.io.Serializable;

public class AGBadword implements Serializable {

	private Long badwordId;
	private String category;
	private String companyId;
	private String word;

	public Long getBadwordId() {
		return badwordId;
	}

	public void setBadwordId(Long badwordId) {
		this.badwordId = badwordId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
}
