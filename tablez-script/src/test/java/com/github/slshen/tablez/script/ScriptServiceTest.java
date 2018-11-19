package com.github.slshen.tablez.script;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class ScriptServiceTest {

	@Test
	public void testBasic() {

		ScriptEngineManager m = new ScriptEngineManager();
		for (ScriptEngineFactory f : m.getEngineFactories()) {
			System.out.println(f.getEngineName() + " - " + f.getLanguageName() + " - " + f.getEngineVersion() + " - "
					+ f.getExtensions());
		}

	}

}
