package com.liubing.common.memcached.codec;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.context.RequestContext;
import com.liubing.common.memcached.context.RequestContextHolder;
import com.liubing.common.memcached.util.SessionUtil;

public class CommandEncoder extends ProtocolEncoderAdapter {

	
	private static final Log log = LogFactory.getLog(CommandEncoder.class);

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		RequestContextHolder holder = (RequestContextHolder) message;
		IoBuffer buf = holder.getContext().getCommand().toBuffer(
				MemcachedConstants.DEFAULT_CHARSET);
		buf.setAutoExpand(true);
		buf.flip();
		ConcurrentHashMap<IoBuffer, RequestContext> encodedMessage2Message =SessionUtil.getEncodedMessage2Message(session);
		RequestContext newItem = encodedMessage2Message.putIfAbsent(buf,
				holder.getContext());

		if (newItem == null) {
			out.write(buf);

		} else {
			if(log.isDebugEnabled()){
				log.debug("reusing command.");
			}
			holder.setContext(newItem);

		}

	}

}
