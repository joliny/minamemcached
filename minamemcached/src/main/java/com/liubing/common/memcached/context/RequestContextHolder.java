package com.liubing.common.memcached.context;

public class RequestContextHolder {
	private RequestContext context;

	public RequestContextHolder() {
		context = new RequestContext();
	}

	public RequestContextHolder(RequestContext context) {

		this.context = context;
	}

	public void setContext(RequestContext context) {
		this.context = context;
	}

	public RequestContext getContext() {
		return context;
	}

}
