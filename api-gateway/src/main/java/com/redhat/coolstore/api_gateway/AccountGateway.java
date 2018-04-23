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

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.redhat.coolstore.api_gateway.model.Account;

@Component
public class AccountGateway extends RouteBuilder {
	private static final Logger LOG = LoggerFactory.getLogger(AccountGateway.class);
	
	
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

        restConfiguration().component("servlet")
            .bindingMode(RestBindingMode.auto).apiContextPath("/api-docs").contextPath("/api").apiProperty("host", "")
            .apiProperty("api.title", "Microservice API Gateway")
            .apiProperty("api.version", "1.0.0")
            .apiProperty("api.description", "The API of the gateway");
            //.apiProperty("api.contact.name", "Red Hat Developers")
            //.apiProperty("api.contact.email", "developers@redhat.com")
            //.apiProperty("api.contact.url", "https://developers.redhat.com");


        rest("/v1/accounts").description("Account Service")
            .produces(MediaType.APPLICATION_JSON_VALUE)
            
            
            .get("/{id}").description("Get account details")
            .param().name("id").type(RestParamType.path).description("The account number").dataType("string").endParam()
            .outType(Account.class)
            .route().id("getAccountRoute")
            .hystrix().id("Cart Service (Get Cart)")
                .hystrixConfiguration()
	    			.executionTimeoutInMilliseconds(hystrixExecutionTimeout)
	    			.groupKey(hystrixGroupKey)
	    			.circuitBreakerEnabled(hystrixCircuitBreakerEnabled)
	    		.end()
                .removeHeaders("CamelHttp*")
                .setBody(simple("null"))
                .setHeader(Exchange.HTTP_METHOD, HttpMethods.GET)
                .setHeader(Exchange.HTTP_URI, simple("http://{{env:ACCOUNT_ENDPOINT:cart:8080}}/api/cart/${header.cartId}"))
                .to("http4://DUMMY")
            .onFallback()
                //.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(Response.Status.SERVICE_UNAVAILABLE.getStatusCode()))
    			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
    			.to("direct:defaultFallback")
            .end()
            .setHeader("CamelJacksonUnmarshalType", simple(Account.class.getName()))
            .unmarshal().json(JsonLibrary.Jackson, Account.class)
        .endRest();
        
        
        rest("/v1/accounts").description("Account Service")
            .post().description("Add new account")
				.param().name("accountNo").type(RestParamType.path).description("The account number").dataType("int").endParam()
				.param().name("balance").type(RestParamType.path).description("Starting balance").dataType("double").endParam()
                .outType(Account.class)
                .route().id("createNewAccountRoute")
                .hystrix().id("Account Service (Add Rating)")
                    .hystrixConfiguration()
		    			.executionTimeoutInMilliseconds(hystrixExecutionTimeout)
		    			.groupKey(hystrixGroupKey)
		    			.circuitBreakerEnabled(hystrixCircuitBreakerEnabled)
		    		.end()
                    .removeHeaders("CamelHttp*")
                    .setBody(simple("null"))
                    .setHeader(Exchange.HTTP_METHOD, HttpMethods.POST)
                    .setHeader(Exchange.HTTP_URI, simple("http://{{env:ACCOUNT_ENDPOINT:rating:8080}}/api/v1/accounts"))
                    .to("http4://DUMMY")
                .onFallback()
        			.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        			.to("direct:createNewAccountFallback")
                .end()
                .setHeader("CamelJacksonUnmarshalType", simple(Account.class.getName()))
                .unmarshal().json(JsonLibrary.Jackson, Account.class)
            .endRest();

        // Provide a response
        from("direct:createNewAccountFallback").routeId("createNewAccountFallback").description("Create New Account Fallback response")
        	.process(exchange -> exchange.getIn().setBody(new Account()))
        .marshal().json(JsonLibrary.Jackson);
           

    }

    
}
