package com.liubing.common.memcached.client.queue.creator;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;


import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.client.command.MemcachedCommand;
import com.liubing.common.memcached.client.queue.SimpleCircularQueue;
import com.liubing.common.memcached.client.util.SessionConstants;
import com.liubing.common.memcached.lock.EmptyReadWriteLock;

public class SimpleSessionCreator		extends
		AbstractSessionCreator {

	private static final Log log = LogFactory.getLog(SimpleSessionCreator.class);
	public SimpleCircularQueue< EmptyReadWriteLock> init(
			IoConnector connector) {
		SimpleCircularQueue< EmptyReadWriteLock> sessions = 
				new SimpleCircularQueue< EmptyReadWriteLock>(new EmptyReadWriteLock());
		
		for (int i = 0; i < getSize(); i++) {
			if(log.isDebugEnabled()){
				log.debug(String.format("host=%s,port=%d", getHost(), getPort()));
			}
			ConnectFuture future = connector.connect(new InetSocketAddress(
					getHost(), getPort()));
			future.awaitUninterruptibly();
			future.getSession().setAttribute(SessionConstants.ATTRIBUTE_ORDER,
					isOrder());
			future.getSession().setAttribute(
					SessionConstants.ATTRIBUTE_REQUEST_LENGTH_MAX,
					getRequestMaxLength());
			future.getSession().setAttribute(
					SessionConstants.ATTRIBUTE_REPONSE_LENGTH_MAX,
					getReponseMaxLength());
			future.getSession().setAttribute(MemcachedConstants.KEY_COMMAND_QUEUE,
					new LinkedBlockingQueue<MemcachedCommand<?>>());

			sessions.add(future.getSession());
		}

		return sessions;
	}



}
