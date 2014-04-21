package com.liubing.common.memcached.client.queue;

import org.apache.mina.core.session.IoSession;

import com.liubing.common.memcached.client.util.CallBack;

public interface SessionQueue extends Queue<IoSession> {

	IoSession get();

	void add(IoSession e);

	void foreach(CallBack<IoSession> cb);

}
