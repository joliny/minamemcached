package com.liubing.common.memcached.client.queue.creator;


public abstract class AbstractSessionCreator
	implements SessionQueueCreator {

	private String host;
	private int port;
	private int size = 5;
	private boolean order;
	private int requestMaxLength = 2048;
	private int reponseMaxLength = 10240;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public boolean isOrder() {
		return order;
	}
	public void setOrder(boolean order) {
		this.order = order;
	}
	public int getRequestMaxLength() {
		return requestMaxLength;
	}
	public void setRequestMaxLength(int requestMaxLength) {
		this.requestMaxLength = requestMaxLength;
	}
	public int getReponseMaxLength() {
		return reponseMaxLength;
	}
	public void setReponseMaxLength(int reponseMaxLength) {
		this.reponseMaxLength = reponseMaxLength;
	}
	
	

}
