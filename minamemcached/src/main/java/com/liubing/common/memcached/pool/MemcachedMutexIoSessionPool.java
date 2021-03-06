package com.liubing.common.memcached.pool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.liubing.common.memcached.client.command.MemcachedCommand;
import com.liubing.common.memcached.client.pool.AbstractIoSessionPool;
import com.liubing.common.memcached.context.RequestContextHolder;
import com.liubing.common.memcached.reponse.MemcachedResponse;

public class MemcachedMutexIoSessionPool extends
		AbstractIoSessionPool<MemcachedCommand<?>, MemcachedResponse<?>> {

	private static final Log log = LogFactory
			.getLog(MemcachedMutexIoSessionPool.class);
	
	public MemcachedMutexIoSessionPool(){
		if(this.getConnector() == null){
			this.setConnector(new NioSocketConnector());
		}
	}

	@SuppressWarnings("rawtypes")
	public MemcachedResponse send(MemcachedCommand message, boolean sync) {
		IoSession session = selectSession();
		RequestContextHolder holder = new RequestContextHolder();
		holder.getContext().setCommand(message);

		if (sync) {
			session.write(holder);
			try {
				holder.getContext().await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			if (!holder.getContext().isSuccess()) {
				if (log.isInfoEnabled())
					log.info(String.format("time out"));
			}
			return holder.getContext().getReponse();
		} else {
			session.write(holder);
			return null;
		}
	}
}
