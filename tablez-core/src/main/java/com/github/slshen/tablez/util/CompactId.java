package com.github.slshen.tablez.util;

import java.util.concurrent.atomic.AtomicLong;

public abstract class CompactId {
	private static char[] base32 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7' };

	private CompactId() {
	}

	public static String nextId(AtomicLong counter) {
		long value;
		while (((value = counter.incrementAndGet()) & 0xc000_0000_0000_0000L) != 0) {
			counter.compareAndSet(value, 0);
		}
		return toString(value);
	}

	public static String toString(long value) {
		char[] buf = new char[10];
		for (int i = 0; i < 10; i++) {
			buf[i] = base32[((int) value) & 0x1f];
			value = value >> 5;
		}
		return new String(buf);
	}

}
