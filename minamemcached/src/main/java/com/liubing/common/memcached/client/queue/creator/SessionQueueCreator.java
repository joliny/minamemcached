package com.liubing.common.memcached.client.queue.creator;

import org.apache.mina.core.service.IoConnector;

import com.liubing.common.memcached.client.queue.SessionQueue;

public interface SessionQueueCreator {

	SessionQueue init(IoConnector connector);

}
