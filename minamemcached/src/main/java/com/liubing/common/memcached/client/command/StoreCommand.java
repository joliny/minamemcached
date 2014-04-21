package com.liubing.common.memcached.client.command;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.reponse.MemcachedResponse;

/**
 * abstract class ,  prepare for set/add/repalce
 * @author zhengtao.wuzt
 *
 * @param <T>
 */
public abstract class StoreCommand<T extends MemcachedResponse<?>> implements MemcachedCommand<T > {

	private static final  String COMMAND_FORMATER="%s %s %d %d %d\r\n";
	private String key;
	private int flags;
	private int exptime;
	private byte[]  value;
	private Charset  charset = MemcachedConstants.DEFAULT_CHARSET;
	
	
	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getExptime() {
		return exptime;
	}

	public void setExptime(int exptime) {
		this.exptime = exptime;
	}

	public byte[] getValue() {
		return value;
	}
	

	public void setValue(byte[] value) {
		this.value = value;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	/**
	 * <command name> <key> <flags> <exptime> <bytes>\r\n
	 * <data block>\r\n
	 */
	public IoBuffer toBuffer(Charset charset) {
		
		IoBuffer buf = IoBuffer.allocate(1024);
		buf.setAutoExpand(true);
		try {
			String command = String.format(COMMAND_FORMATER, getName().toString() , this.key , flags , exptime , value.length);
			buf.putString(command , charset.newEncoder());
			buf.put(value);
			buf.putString("\r\n", charset.newEncoder());
		} catch (CharacterCodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return buf;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

}
