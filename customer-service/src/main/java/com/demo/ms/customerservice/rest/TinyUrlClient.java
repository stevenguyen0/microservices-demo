package com.demo.ms.customerservice.rest;

import java.util.List;

import com.demo.ms.customerservice.model.TinyUrl;
import com.demo.ms.customerservice.model.TinyUrlResource;

import feign.Headers;
import feign.RequestLine;

public interface TinyUrlClient {

    @RequestLine("GET")
    List<TinyUrlResource> findAll();
 
    @RequestLine("POST")
    @Headers("Content-Type: application/text")
    void create(TinyUrl url);
}
