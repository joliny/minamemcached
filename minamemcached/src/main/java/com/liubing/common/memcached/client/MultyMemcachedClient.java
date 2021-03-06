package com.liubing.common.memcached.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


import com.liubing.common.memcached.MemcachedClient;
import com.liubing.common.memcached.reponse.SetReponse;

public class MultyMemcachedClient implements MemcachedClient {

	private List<InetSocketAddress> servers = new ArrayList<InetSocketAddress>(
			50);

	public static interface Selector {
		InetSocketAddress select(String key, List<InetSocketAddress> t);
	}

	private Selector selector = new Selector() {

		public InetSocketAddress select(String key, List<InetSocketAddress> t) {

			return servers.get(t.hashCode() % servers.size());
		}
	};
	private ConcurrentHashMap<InetSocketAddress, SingleTargetMemcachedClient> mcs = new ConcurrentHashMap<InetSocketAddress, SingleTargetMemcachedClient>();

	public void addServer(String ip, int port) {
		servers.add(new InetSocketAddress(ip, port));
	}
	
	public void removeServer(String ip, int port){
		servers.remove(new InetSocketAddress(ip, port));
	}
	
	public boolean add(String key, byte[] value) {
		InetSocketAddress target = selector.select(key, servers);

		return mcs.get(target).add(key, value);

	}

	public void init() {
		for (InetSocketAddress target : servers) {
			SingleTargetMemcachedClient mc = new SingleTargetMemcachedClient(
					target.getHostName(), target.getPort(), Runtime
							.getRuntime().availableProcessors() + 1);
			mc.init();
			mcs.put(target, mc);

		}
	}

	public void close() {

		for (Map.Entry<InetSocketAddress, SingleTargetMemcachedClient> entry : mcs
				.entrySet()) {

			entry.getValue().close();

		}
		mcs = null;
	}

	public byte[] get(String key) {
		InetSocketAddress target = selector.select(key, servers);

		return mcs.get(target).get(key);
	}

	public SetReponse set(String key, String value) {
		InetSocketAddress target = selector.select(key, servers);

		return mcs.get(target).set(key, value);
	}

}
