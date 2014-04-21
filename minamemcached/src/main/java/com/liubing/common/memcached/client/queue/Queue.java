package com.liubing.common.memcached.client.queue;

import com.liubing.common.memcached.client.util.CallBack;

public interface Queue <T> {
	T get();

	void add(T e);

	void foreach(CallBack<T> cb);

}
