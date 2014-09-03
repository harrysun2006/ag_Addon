package com.amaxgs.addon.model;

import java.util.Date;

public class VBViewbar {

	private String viewbarId;
	private String osVersion;
	private Date createDate;
	private Date releaseDate;
	private Date expireDate;
	private String rootPath;
	private Long downloadCount;
	private Long downloadSCount;
	private String description;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(Long downloadCount) {
		this.downloadCount = downloadCount;
	}
	
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getViewbarId() {
		return viewbarId;
	}
	public void setViewbarId(String viewbarId) {
		this.viewbarId = viewbarId;
	}
	public Long getDownloadSCount() {
		return downloadSCount;
	}
	public void setDownloadSCount(Long downloadSCount) {
		this.downloadSCount = downloadSCount;
	}
	
}
