package com.github.slshen.tablez.util;

import java.util.concurrent.locks.Lock;

public class HeldLock implements AutoCloseable {
	private final Lock lock;

	public HeldLock(Lock lock) {
		this.lock = lock;
	}

	@Override
	public void close() {
		lock.unlock();
	}

	public static HeldLock lockInterruptibly(Lock lock) {
		HeldLock result = new HeldLock(lock);
		try {
			lock.lockInterruptibly();
			return result;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static HeldLock lock(Lock lock) {
		HeldLock result = new HeldLock(lock);
		lock.lock();
		return result;
	}
}
