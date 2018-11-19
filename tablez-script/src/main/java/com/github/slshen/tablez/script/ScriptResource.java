package com.github.slshen.tablez.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slshen.tablez.JsonUtil;
import com.github.slshen.tablez.event.AbstractEventSource;
import com.github.slshen.tablez.problems.Problem;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

public class ScriptResource extends AbstractEventSource {

	private static final Charset UTF8 = Charset.forName("UTF8");
	private final String path;
	private final Resource resource;
	private static final Pattern headerPattern = Pattern.compile("^(#|//)\\s*@(\\w+)(.+)");

	public ScriptResource(String path, Resource resource) {
		this.path = path;
		this.resource = resource;
	}

	public String getPath() {
		return path;
	}

	public Resource getResource() {
		return resource;
	}

	public BufferedReader open() throws IOException {
		return new BufferedReader(new InputStreamReader(resource.getInputStream(), UTF8));
	}

	public Multimap<String, JsonNode> getHeader(ScriptResource resource) throws IOException {
		Multimap<String, JsonNode> result = MultimapBuilder.hashKeys().arrayListValues().build();
		int n = 20;
		ObjectMapper mapper = JsonUtil.getObjectMapper();
		try (BufferedReader reader = open()) {
			String line;
			while (n > 0 && ((line = reader.readLine()) != null)) {
				--n;
				Matcher m = headerPattern.matcher(line);
				if (m.find()) {
					String name = m.group(2);
					try {
						JsonNode value = mapper.readTree(m.group(3));
						result.put(name, value);
					} catch (IOException e) {
						post(new Problem("script", path, e.toString()));
					}
				}
			}
		}
		return result;
	}

}
