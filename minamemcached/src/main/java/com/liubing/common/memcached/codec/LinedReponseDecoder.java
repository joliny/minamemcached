package com.liubing.common.memcached.codec;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.textline.TextLineDecoder;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.context.RequestContext;



public class LinedReponseDecoder extends ProtocolDecoderAdapter {

	private ProtocolDecoder decoder = new TextLineDecoder("\r\n");

	@SuppressWarnings("unchecked")
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {

		final List<String> context = (List<String>) session
				.getAttribute(MemcachedConstants.KEY_CACHED_LINES);

		decoder.decode(session, in, new ProtocolDecoderOutput() {

			public void flush() {
				/**
				 * ignore
				 */

			}

			public void write(Object message) {
				context.add(message.toString());

			}
		});

		for (;;) {
			if (context.size() == 0) {
				return;
			}

			LinkedBlockingQueue<RequestContext> queue = (LinkedBlockingQueue<RequestContext>) session
					.getAttribute(MemcachedConstants.KEY_COMMAND_QUEUE);

			RequestContext item = queue.peek();

			if (item == null) {
				return;
			}

			if (item.getReponse() == null) {
				item.setReponse(item.getCommand().create());
			}

			boolean result = item.getReponse().parse(context,
					MemcachedConstants.DEFAULT_CHARSET);


			if (result) {
				out.write(item);
				queue.remove();
			} else {
				return;
			}

		}

	}
}
