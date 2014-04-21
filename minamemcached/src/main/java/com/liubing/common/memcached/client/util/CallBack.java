package com.liubing.common.memcached.client.util;

public interface CallBack<T> {
	void execute(T t);
}
