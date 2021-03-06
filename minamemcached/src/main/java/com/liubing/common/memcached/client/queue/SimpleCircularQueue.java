package com.liubing.common.memcached.client.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;

import org.apache.mina.core.session.IoSession;

import com.liubing.common.memcached.client.util.CallBack;

public class SimpleCircularQueue< L extends ReadWriteLock> implements
		SessionQueue {

	private AtomicInteger cur = new AtomicInteger(0);
	private ArrayList<IoSession> queue = new ArrayList<IoSession>(100);

	public SimpleCircularQueue(L lock){
		this.lock = lock;
	}
	private L lock;

	public L getLock() {
		return lock;
	}

	

	public void add(IoSession e) {
		lock.writeLock().lock();
		try {
			queue.add(e);
		} finally {
			lock.writeLock().unlock();
		}

	}

	public IoSession get() {

		lock.readLock().lock();
		try {
			return queue.get(cur.incrementAndGet() % queue.size());
		} finally {
			lock.readLock().unlock();
		}

	}

	public void foreach(CallBack<IoSession> cb) {

		for (Iterator<IoSession> iterator = queue.iterator(); iterator.hasNext();) {
			IoSession t = (IoSession) iterator.next();
			cb.execute(t);

		}
	}

}
