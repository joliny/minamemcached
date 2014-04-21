/**
 * 
 */
package com.liubing.common.memcached.context;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import com.liubing.common.memcached.client.command.MemcachedCommand;
import com.liubing.common.memcached.reponse.MemcachedResponse;

public class RequestContext {
	private MemcachedCommand<?> command;
	private MemcachedResponse<?> reponse;
	private CountDownLatch master;
	private boolean success = false;

	public boolean isSuccess() {
		return success;
	}
	public void release() {
		master.countDown();

	}

	public CountDownLatch getMaster() {
		return master;
	}

	public RequestContext() {
		master = new CountDownLatch(1);
	}

	public RequestContext(int i) {
		master = new CountDownLatch(i);
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public MemcachedResponse<?> getReponse() {
		return reponse;
	}

	public void setReponse(MemcachedResponse<?> reponse) {
		this.reponse = reponse;
	}

	public MemcachedCommand<?> getCommand() {
		return command;
	}

	public void setCommand(MemcachedCommand<?> command) {
		this.command = command;
	}

	public void await() throws InterruptedException {
		this.master.await();
	}

	public void await(long timeout, TimeUnit unit) throws InterruptedException {
		this.master.await(timeout, unit);
	}


	public void setMaster(CountDownLatch master) {
		this.master = master;
	}

}