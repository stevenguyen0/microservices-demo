package com.demo.ms.api.gateway.model;

public class Url {
	private long id;
	private String shortName;
	private String actualUrl;
	public Url() {
	}
	public Url(String actualUrl) {
		this.actualUrl = actualUrl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getActualUrl() {
		return actualUrl;
	}
	public void setActualUrl(String actualUrl) {
		this.actualUrl = actualUrl;
	}
}
