package com.demo.ms.tinyurlservice;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class AppConfiguration extends Configuration {

	@NotEmpty
	private String defaultName = "travelmob";

	@Valid
	@NotNull
	private DataSourceFactory database = new DataSourceFactory();

	@NotNull
	private Map<String, Map<String, String>> viewRendererConfiguration = Collections
			.emptyMap();

	@JsonProperty
	public String getDefaultName() {
		return defaultName;
	}

	@JsonProperty
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	@JsonProperty("database")
	public DataSourceFactory getDataSourceFactory() {
		return database;
	}

	@JsonProperty("database")
	public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
		this.database = dataSourceFactory;
	}

	@JsonProperty("viewRendererConfiguration")
	public Map<String, Map<String, String>> getViewRendererConfiguration() {
		return viewRendererConfiguration;
	}

	@JsonProperty("viewRendererConfiguration")
	public void setViewRendererConfiguration(
			Map<String, Map<String, String>> viewRendererConfiguration) {
		final ImmutableMap.Builder<String, Map<String, String>> builder = ImmutableMap
				.builder();
		for (Map.Entry<String, Map<String, String>> entry : viewRendererConfiguration
				.entrySet()) {
			builder.put(entry.getKey(), ImmutableMap.copyOf(entry.getValue()));
		}
		this.viewRendererConfiguration = builder.build();
	}
}
