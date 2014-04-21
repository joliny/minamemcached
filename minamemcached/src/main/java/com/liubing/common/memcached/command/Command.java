package com.liubing.common.memcached.command;

public class Command<C> {
	
	private long sequence;
	private C message;
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	public C getMessage() {
		return message;
	}
	public void setMessage(C message) {
		this.message = message;
	}

	
	

}
