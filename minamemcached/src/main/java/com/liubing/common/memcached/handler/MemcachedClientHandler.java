package com.liubing.common.memcached.handler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.context.RequestContext;

public class MemcachedClientHandler extends IoHandlerAdapter {

	private static final Log log = LogFactory
			.getLog(MemcachedClientHandler.class);

	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		log.error(cause.getMessage(), cause);
		session.close();
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {

		RequestContext context = (RequestContext) message;

		context.setSuccess(true);
		context.release();

	}

	public void sessionClosed(IoSession session) throws Exception {

	}

	public void sessionCreated(IoSession session) throws Exception {

		session.setAttribute(MemcachedConstants.KEY_CACHED_LINES,
				new ArrayList<String>());

		IoBuffer buf = IoBuffer.allocate(10240);
		buf.setAutoExpand(true);
		session.setAttribute(MemcachedConstants.KEY_CACHED_IOBUFFER, buf);

		session.setAttribute(MemcachedConstants.KEY_ENCODED_MESSAGE2_MESSAGE,
				new ConcurrentHashMap<IoBuffer, RequestContext>(1000));
	}

}
