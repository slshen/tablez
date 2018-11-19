package com.github.slshen.tablez.problems;

public class Problem {

	private final long timestamp;
	private final String sourceType, source, description;

	public Problem(String sourceType, String source, String description) {
		this.timestamp = System.currentTimeMillis();
		this.sourceType = sourceType;
		this.source = source;
		this.description = description;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public String getSourceType() {
		return sourceType;
	}

	public String getSource() {
		return source;
	}

	public String getDescription() {
		return description;
	}

}
