package com.liubing.common.memcached.filter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.liubing.common.memcached.context.RequestContext;
import com.liubing.common.memcached.util.SessionUtil;

public class AppendFilter extends IoFilterAdapter {

	private static final Log log = LogFactory.getLog(AppendFilter.class);

	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		Object obj = writeRequest.getMessage();
		if (obj instanceof IoBuffer) {
			IoBuffer buf = (IoBuffer) obj;
			if (buf.limit() != 0) {
				ConcurrentHashMap<IoBuffer, RequestContext> encodedMessage2Message = SessionUtil
						.getEncodedMessage2Message(session);

				LinkedBlockingQueue<RequestContext> queue = SessionUtil
						.getCommandQueue(session);
				RequestContext item = encodedMessage2Message.remove(buf);
				
				if (item == null) {
					log.error("Can't find the command :" + buf + "in the map");
					log.error("map :" + encodedMessage2Message.toString());
					throw new IllegalStateException();
				}
				queue.add(item);

			}
		}
		nextFilter.messageSent(session, writeRequest);
	}

}
