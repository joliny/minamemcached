package com.liubing.common.memcached.reponse;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

public interface MemcachedResponse<T> {

	boolean isSuccess();
	
	T getResult();

//	@Deprecated
//	boolean parse(final CachedByteBuffer cacheBuf, Charset charSet);

	boolean parse(List<String> context, Charset defaultCharset);

	boolean parse(IoBuffer in, Charset defaultCharset);

//	@Deprecated
//	boolean parse(List<String> lines);
}
