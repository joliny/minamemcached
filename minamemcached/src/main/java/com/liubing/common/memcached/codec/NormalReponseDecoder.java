package com.liubing.common.memcached.codec;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.context.RequestContext;
import com.liubing.common.memcached.util.SessionUtil;


public class NormalReponseDecoder extends ProtocolDecoderAdapter {

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {

		IoBuffer cachedBuffer = SessionUtil.getCachedIoBuffer(session);

		cachedBuffer.put(in.buf());
		if (cachedBuffer.markValue() >= 0) {
			cachedBuffer.reset();
		} else {
			cachedBuffer.flip();
		}

		for (;;) {

			if (cachedBuffer.remaining() == 0) {
				cachedBuffer.clear();
				return;
			}

			LinkedBlockingQueue<RequestContext> queue = SessionUtil.getCommandQueue(session);

			RequestContext context = queue.peek();

			if (context == null) {
				return;
			}

			if (context.getReponse() == null) {
				context.setReponse(context.getCommand().create());
			}

			int oldlimit = cachedBuffer.limit();
			cachedBuffer.mark();
			boolean result = context.getReponse().parse(cachedBuffer,
					MemcachedConstants.DEFAULT_CHARSET);

			if (result) {
				out.write(context);
				queue.remove();

			} else {
				cachedBuffer.position(oldlimit);
				return;
			}

		}

	}

}
