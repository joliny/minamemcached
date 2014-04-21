package com.liubing.common.memcached;

import com.liubing.common.memcached.reponse.SetReponse;

public interface MemcachedClient {

	public byte[] get(String key);

	public SetReponse set(String key, String value);

	public boolean add(String key, byte[] value);
	
	public void init();
	
	
	public void close();

}
