package com.liubing.common.memcached.client;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;


import com.liubing.common.memcached.MemcachedClient;
import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.client.command.AddCommand;
import com.liubing.common.memcached.client.command.GetCommand;
import com.liubing.common.memcached.client.command.SetCommand;
import com.liubing.common.memcached.client.queue.creator.SimpleSessionCreator;
import com.liubing.common.memcached.codec.CommandEncoder;
import com.liubing.common.memcached.codec.NormalReponseDecoder;
import com.liubing.common.memcached.filter.AppendFilter;
import com.liubing.common.memcached.handler.MemcachedClientHandler;
import com.liubing.common.memcached.pool.MemcachedMutexIoSessionPool;
import com.liubing.common.memcached.reponse.AddReponse;
import com.liubing.common.memcached.reponse.GetReponse;
import com.liubing.common.memcached.reponse.SetReponse;

public class SingleTargetMemcachedClient implements MemcachedClient {

	final int DEFAULT_CPU = Runtime.getRuntime().availableProcessors();
	private MemcachedMutexIoSessionPool pool = null;
	private int size;

	public SingleTargetMemcachedClient(String host, int port, int size) {
		this.port = port;
		this.host = host;
		this.size = size;

	}
	public SingleTargetMemcachedClient(String host, int port) {
		this.port = port;
		this.host = host;
		this.size = DEFAULT_CPU + 1;

	}

	private String host;
	private int port;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public byte[] get(String key) {

		GetCommand command = new GetCommand();

		command.setValue(key);

		GetReponse rsp = (GetReponse) pool.send(command, true);

		if (rsp != null && rsp.isSuccess()) {
			return rsp.getResult();
		}

		return new byte[0];

	}

	public SetReponse set(String key, String value) {

		SetCommand setCommand = new SetCommand();
		setCommand.setKey(key);
		setCommand.setValue(value.getBytes());

		return (SetReponse) pool.send(setCommand, true);
	}

	public boolean add(String key, byte[] value) {

		AddCommand add = new AddCommand();
		add.setKey(key);
		add.setValue(value);

		AddReponse ar = (AddReponse) pool.send(add, true);

		if (ar.isSuccess()
				&& ar.getResult().equals(MemcachedConstants.FLAG_STORED)) {
			return true;
		}

		return false;
	}

	public void close() {
		if (pool != null) {
			pool.close();
		}
	}

	public void init() {
		if (pool == null) {

			pool = new MemcachedMutexIoSessionPool();
			ProtocolCodecFactory factory = new ProtocolCodecFactory() {
				private ProtocolDecoder decoder = new NormalReponseDecoder();
				// private ProtocolDecoder decoder = new LinedReponseDecoder();
				// private ProtocolDecoder decoder = new CachedReponseDecoder();
				private ProtocolEncoder encoder = new CommandEncoder();

				public ProtocolDecoder getDecoder(IoSession session)
						throws Exception {
					return decoder;
				}

				public ProtocolEncoder getEncoder(IoSession session)
						throws Exception {
					return encoder;
				}
			};

			pool.addLast("append", new AppendFilter());
			pool.addLast("codec", new ProtocolCodecFilter(factory));

			pool.setHandler(new MemcachedClientHandler());

			SimpleSessionCreator sessionQueueCreator = new SimpleSessionCreator();
			sessionQueueCreator.setHost(getHost());
			sessionQueueCreator.setPort(getPort());
			sessionQueueCreator.setSize(size);
			pool.setSessionQueueCreator(sessionQueueCreator);
		}
		pool.init();

	}
}
