package com.demo.ms.tinyurlservice.business;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.demo.ms.tinyurlservice.business.StringShortener;

public class StringShortenerTest {
	@Test
	public void testGenerateUrlSimple() {
		String encodedUrl = StringShortener.encode(1000003443);
		assertEquals("Simple test case failed", "bfP4JX", encodedUrl);
	}
	@Test
	public void testGenerateUrlAllNumbers() {
		//now more aggressive test
		long max = 200 * 1000L;
		for(long id = 1; id <= max; id++) {
			assertEquals("Your algorithm broke dude", StringShortener.decode(StringShortener.encode(id)), id);
		}
	}
	@Test
	public void testGenerateUrlCollision() {
		//it should not generate the same string
		long max = 200 * 1000L;
		Set<String> allIds = new HashSet<String>();
		for(long id = 1; id <= max; id++) {
			allIds.add(StringShortener.encode(id));
		}
		assertEquals("Your algorithm broke dude", max, allIds.size());
	}
}
