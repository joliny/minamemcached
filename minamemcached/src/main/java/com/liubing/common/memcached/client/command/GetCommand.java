package com.liubing.common.memcached.client.command;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.liubing.common.memcached.reponse.GetReponse;

public class GetCommand implements MemcachedCommand<GetReponse> {

	private String value;

	public Name getName() {
		return Name.get;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IoBuffer toBuffer(Charset charset) {
		IoBuffer buf = IoBuffer.allocate(100);
		buf.setAutoExpand(true);

		String str = String.format("%s %s\r\n", getName().toString(), value);

		buf.put(str.getBytes(charset));

		return buf;
	}

	public GetReponse create() {

		GetReponse r = new GetReponse();

		return r;
	}

}
