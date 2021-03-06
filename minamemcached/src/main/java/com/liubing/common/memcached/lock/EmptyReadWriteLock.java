package com.liubing.common.memcached.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class EmptyReadWriteLock implements ReadWriteLock {

	private static class EmptyLock implements Lock {

		public void lock() {

		}

		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();

		}

		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}

		public boolean tryLock() {
			throw new UnsupportedOperationException();
		}

		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		public void unlock() {

		}

	}

	private Lock readLock = new EmptyLock();
	private Lock writeLock = new EmptyLock();

	public Lock readLock() {

		return readLock;
	}

	public Lock writeLock() {

		return writeLock;
	}

}
