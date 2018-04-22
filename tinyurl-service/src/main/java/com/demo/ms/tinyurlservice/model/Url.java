package com.demo.ms.tinyurlservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "url")
@NamedQueries(
	    {
	        @NamedQuery(
	            name = "com.demo.ms.tinyurlservice.model.Url.findAll",
	            query = "SELECT u FROM Url u"
	        )
	    })
public class Url {
	@Id 
	@GeneratedValue
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
