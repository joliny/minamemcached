package com.liubing.common.memcached.client.util;

import java.util.concurrent.atomic.AtomicLong;

public class Sequence {
	
	static private AtomicLong  sequence= new AtomicLong(0);
	
	static public long next(){
		return sequence.incrementAndGet();
	}

}
