package com.liubing.common.memcached.client.reponse;

public class Reponse <R> {
	
	private long sequence;
	private R message;
	public long getSequence() {
		return sequence;
	}
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}
	public R getMessage() {
		return message;
	}
	public void setMessage(R message) {
		this.message = message;
	}
	
	

}
