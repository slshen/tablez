package com.github.slshen.tablez;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {

	private JsonUtil() {
	}

	private static ObjectMapper mapper = new ObjectMapper();

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}
}
