package com.github.slshen.tlu.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.github.slshen.tablez.util.CompactId;

public class CompactIdTest {
	
	@Test
	public void testBasic() {
		Assertions.assertThat(CompactId.toString(0)).isEqualTo("AAAAAAAAAA");
		Assertions.assertThat(CompactId.toString(1)).isEqualTo("BAAAAAAAAA");
		Assertions.assertThat(CompactId.toString(-1)).isEqualTo("7777777777");
		System.out.println(CompactId.toString(System.currentTimeMillis()));
	}

}
