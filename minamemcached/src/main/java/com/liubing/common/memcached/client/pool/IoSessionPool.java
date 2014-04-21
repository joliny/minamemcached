package com.liubing.common.memcached.client.pool;

public interface IoSessionPool<C, R> {

	void init();

	void close();

	R send(final C commond, boolean sync);

}
