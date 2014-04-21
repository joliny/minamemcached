package com.liubing.common.memcached.client.command;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

import com.liubing.common.memcached.reponse.MemcachedResponse;


public interface MemcachedCommand<T extends MemcachedResponse<?>> {

	Name getName();

	IoBuffer toBuffer(Charset charset);

	T create();

}
