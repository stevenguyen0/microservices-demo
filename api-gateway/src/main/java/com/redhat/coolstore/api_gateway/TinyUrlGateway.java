/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.coolstore.api_gateway;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.redhat.coolstore.api_gateway.model.Url;

@Component
public class TinyUrlGateway extends RouteBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(TinyUrlGateway.class);

	@Value("${hystrix.executionTimeout}")
	private int hystrixExecutionTimeout;

	@Value("${hystrix.groupKey}")
	private String hystrixGroupKey;

	@Value("${hystrix.circuitBreakerEnabled}")
	private boolean hystrixCircuitBreakerEnabled;

	@Autowired
	private Environment env;

	@Override
	public void configure() throws Exception {
		try {
			getContext().setTracing(Boolean.parseBoolean(env.getProperty("ENABLE_TRACER", "false")));
		} catch (Exception e) {
			LOG.error("Failed to parse the ENABLE_TRACER value: {}", env.getProperty("ENABLE_TRACER", "false"));
		}

		restConfiguration().component("servlet").bindingMode(RestBindingMode.auto).apiContextPath("/api-docs")
				.contextPath("/api").apiProperty("host", "").apiProperty("api.title", "Microservice API Gateway")
				.apiProperty("api.version", "1.0.0").apiProperty("api.description", "The API of the gateway");
		// .apiProperty("api.contact.name", "Red Hat Developers")
		// .apiProperty("api.contact.email", "developers@redhat.com")
		// .apiProperty("api.contact.url", "https://developers.redhat.com");

		rest("/v1/tinyurls").description("List of tiny urls").produces(MediaType.APPLICATION_JSON_VALUE).get("/")
				.description("List of tiny urls").outType(Url.class).route().id("getUrlsRoute").to("http4://DUMMY")
				.endRest();

		rest("/v1/tinyurls").description("Create new tiny url").produces(MediaType.APPLICATION_JSON_VALUE)
				.post().description("Add new tiny url").param().name("actualUrl").type(RestParamType.path)
				.description("The actual url").dataType("string").endParam().outType(Url.class).to("http4://DUMMY")
				;

	}

}
