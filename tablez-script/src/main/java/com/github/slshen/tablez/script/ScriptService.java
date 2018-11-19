package com.github.slshen.tablez.script;

import javax.script.ScriptEngineManager;

import org.springframework.stereotype.Component;

@Component
public class ScriptService {

	private ScriptEngineManager manager = new ScriptEngineManager();

	public ScriptService() {
	}

}
