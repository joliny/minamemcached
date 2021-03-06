package com.liubing.common.memcached.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.context.RequestContext;

public class SessionUtil {

	private SessionUtil() {

	}

	@SuppressWarnings("unchecked")
	public static ConcurrentHashMap<IoBuffer, RequestContext> getEncodedMessage2Message(
			IoSession session) {
		ConcurrentHashMap<IoBuffer, RequestContext> encodedMessage2Message = (ConcurrentHashMap<IoBuffer, RequestContext>) session
				.getAttribute(MemcachedConstants.KEY_ENCODED_MESSAGE2_MESSAGE);
		return encodedMessage2Message;
	}

	@SuppressWarnings("unchecked")
	public static LinkedBlockingQueue<RequestContext> getCommandQueue(IoSession session) {
		return (LinkedBlockingQueue<RequestContext>) session
				.getAttribute(MemcachedConstants.KEY_COMMAND_QUEUE);
	}

	public static IoBuffer getCachedIoBuffer(IoSession session) {
		return (IoBuffer) session.getAttribute(MemcachedConstants.KEY_CACHED_IOBUFFER);
	}
}
