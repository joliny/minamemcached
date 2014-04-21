package com.liubing.common.memcached.client.mina.codec.encoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.liubing.common.memcached.client.util.SessionConstants;
import com.liubing.common.memcached.command.Command;

public abstract class CommandEncoder<C> extends ProtocolEncoderAdapter {

	@SuppressWarnings("unchecked")
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		
		Command<C>  command= (Command<C>) message;  
		
		Integer  max = (Integer) session.getAttribute(SessionConstants.ATTRIBUTE_REQUEST_LENGTH_MAX);
		IoBuffer buf = IoBuffer.allocate(max.intValue());
		
		buf.putLong(command.getSequence());
		
		doEncode(session,buf,command.getMessage());
		
		buf.flip();
		
        out.write(buf);

	}

	protected abstract void doEncode(IoSession session , IoBuffer buf, C message);

}
