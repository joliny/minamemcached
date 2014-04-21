package com.liubing.common.memcached.client.pool;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

import com.liubing.common.memcached.client.queue.SessionQueue;
import com.liubing.common.memcached.client.queue.creator.SessionQueueCreator;
import com.liubing.common.memcached.client.util.CallBack;

public abstract class AbstractIoSessionPool<C, R> implements
		IoSessionPool<C, R> {

	private IoConnector connector;

	private SessionQueueCreator sessionQueueCreator;

	public SessionQueueCreator getSessionQueueCreator() {
		return sessionQueueCreator;
	}

	public void setSessionQueueCreator(SessionQueueCreator sessionQueueCreator) {
		this.sessionQueueCreator = sessionQueueCreator;
	}

	public IoConnector getConnector() {
		return connector;
	}

	public void setConnector(IoConnector connector) {
		this.connector = connector;
	}

	private SessionQueue sessions;

	public void init() {

		sessions = sessionQueueCreator.init(connector);

	}

	public SessionQueue getSessions() {
		return sessions;
	}

	public void setSessions(SessionQueue sessions) {
		this.sessions = sessions;
	}

	protected IoSession selectSession() {

		IoSession session = sessions.get();

		if (session.isConnected()) {
			return session;
		}
		throw new IllegalStateException("No session is connected!");
	}

	public void addLast(String name, IoFilter filter) {
	
		getConnector().getFilterChain().addLast(name, filter);
	
	}

	public void setHandler(IoHandler handler) {
	
		getConnector().setHandler(handler);
	}

	public void addFirst(String name, IoFilter filter) {
	
		getConnector().getFilterChain().addFirst(name, filter);
	}
	public void close() {
	
			getSessions().foreach(new CallBack<IoSession>() {
	
				public void execute(IoSession session) {
	
					session.close();
	
				}
			});
	
			getConnector().dispose();
		}

//	public R send(C message, boolean sync) {
//
//		Command<C> command = new Command<C>();
//		long sequence = Sequence.next();
//
//		command.setSequence(sequence);
//		command.setMessage(message);
//
//		if (sync) {
//
//			return doSendSync(command);
//		} else {
//			selectSession().write(command);
//
//			return null;
//		}
//	}
//
//	abstract protected R doSendSync(Command<C> commond);

}
