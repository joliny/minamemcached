package com.liubing.common.memcached;

import java.nio.charset.Charset;

import org.apache.mina.core.session.AttributeKey;

public final class MemcachedConstants {

	public static final AttributeKey KEY_ENCODED_MESSAGE2_MESSAGE = new AttributeKey(
			MemcachedConstants.class, "encodedMessage2Message");

	private MemcachedConstants() {
	}

	public static final AttributeKey KEY_COMMAND_QUEUE = new AttributeKey(
			MemcachedConstants.class, "command queue");
//	public static final AttributeKey CONTEXT = new AttributeKey(
//			MemcachedConstants.class, "context");
	public static final AttributeKey KEY_CACHED_BYTE = new AttributeKey(
			MemcachedConstants.class, "context");
	
	
	public static final AttributeKey KEY_CACHED_LINES= new AttributeKey(MemcachedConstants.class, "line.context");
    public static final byte[] FLAG_LINE_END_BYTE = "\r\n".getBytes();

	/**
	 * IoBuffer cached for NormalReponseDecoder
	 */
	public static final AttributeKey KEY_CACHED_IOBUFFER = new AttributeKey(
			MemcachedConstants.class, "cache.iobuffer");
	/**
	 * constants for memcached protocol
	 */
	public static final String FLAG_END = "END\r\n";

	public static final String FLAG_STORED = "STORED\r\n";
	public static final String FLAG_NOTSTORED = "NOT_STORED\r\n";

	public static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

}
