package com.demo.ms.customerservice.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.demo.ms.customerservice.model.TinyUrl;
import com.demo.ms.customerservice.model.TinyUrlResource;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

public class TinyUrlClientTest {
	
	TinyUrlClient client;
	
	@Before
	public void setUp() {
		client = Feign.builder()
				  .client(new OkHttpClient())
				  .encoder(new GsonEncoder())
				  .decoder(new GsonDecoder())
				  .logger(new Slf4jLogger(TinyUrlClient.class))
				  .logLevel(Logger.Level.FULL)
				  .target(TinyUrlClient.class, "http://localhost:8080/api/v1/tinyurls");
	}

	//@Test
	public void getTinyUrlListTest() throws Exception {
		List<TinyUrl> urls = client.findAll().stream().map(TinyUrlResource::getTinyUrl).collect(Collectors.toList());
	}

}
