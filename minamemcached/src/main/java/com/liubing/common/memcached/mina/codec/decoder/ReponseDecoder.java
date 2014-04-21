package com.liubing.common.memcached.mina.codec.decoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.liubing.common.memcached.client.reponse.Reponse;
import com.liubing.common.memcached.client.util.SessionConstants;

public abstract class ReponseDecoder<R> extends CumulativeProtocolDecoder {

	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {

		long sequence = ((Long) session.getAttribute(
				SessionConstants.ATTRIBUTE_ENCODER_SEQUENCE,
				Long.parseLong("-1"))).longValue();

		if (sequence == -1) {
			if (in.remaining() >= 8) {
				sequence = in.getLong();
				session.setAttribute(
						SessionConstants.ATTRIBUTE_ENCODER_SEQUENCE,
						Long.valueOf(sequence));
				return encodeBody(session, in, out, sequence);
			} else {
				return false;
			}

		} else {
			return encodeBody(session, in, out, sequence);
		}
	}

	private boolean encodeBody(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out, long sequence) {
		R message = doDecode(session, in);
		if (message == null) {
			return false;
		}
		Reponse<R> reponse = new Reponse<R>();
		reponse.setSequence(sequence);
		reponse.setMessage(message);

		out.write(reponse);
		session.setAttribute(SessionConstants.ATTRIBUTE_ENCODER_SEQUENCE,
				Long.valueOf(-1));
		return true;
	}

	abstract protected R doDecode(IoSession session, IoBuffer in);

}
