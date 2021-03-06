package com.liubing.test;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liubing.common.memcached.MemcachedClient;
import com.liubing.common.memcached.client.MultyMemcachedClient;
import com.liubing.common.memcached.client.SingleTargetMemcachedClient;

public class Test {
	static private final Log log = LogFactory
			.getLog(SingleTargetMemcachedClient.class);

	static class TestRunnable implements Runnable {

		private MemcachedClient mc;
		private CountDownLatch cd;
		int repeat;
		int start;

		public TestRunnable(MemcachedClient mc, int start, CountDownLatch cdl,
				int repeat) {
			super();
			this.mc = mc;
			this.start = start;
			this.cd = cdl;
			this.repeat = repeat;

		}

		public void run() {

			for (int i = 0; i < repeat; i++) {

				String key = Integer.toString(i);
				mc.set(key, "111ss");
				byte[] value = mc.get(key);
				if (log.isInfoEnabled()) {
					String result = new String(value);
					if (!result.equals(key)) {
						//log.info(String
						//		.format("key:%s->result:%s", key, result));
						throw new IllegalStateException();
					}
				}

			}

			cd.countDown();
			// if (log.isDebugEnabled())
			// log.debug(String.format("%s count down", Thread.currentThread()
			// .getName()));
		}

	}

	static public void main(String[] args) throws ParseException {

		// setup command line options
		Options options = new Options();
		options.addOption("h", "help", false, "print this help screen");
		options.addOption("i", "ip", true, "remote ip");
		options.addOption("p", "port", true, "remote port ");
		options.addOption("s", "size", true, "session size");
		options.addOption("t", "thread", true, "thread number");
		options.addOption("r", "repeat", true, "repeat number");

		// read command line options
		CommandLineParser parser = new PosixParser();
		CommandLine cmdline = parser.parse(options, args);

		if (cmdline.hasOption("help") || cmdline.hasOption("h")) {

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ant run", options);
			return;
		}

		String ip = "112.124.109.161";
		if (cmdline.hasOption("i")) {
			ip = cmdline.getOptionValue("i");
		} else if (cmdline.hasOption("ip")) {
			ip = cmdline.getOptionValue("ip");
		}

		int port = 11213;
		if (cmdline.hasOption("p")) {
			port = Integer.parseInt(cmdline.getOptionValue("p"));
		} else if (cmdline.hasOption("port")) {
			port = Integer.parseInt(cmdline.getOptionValue("port"));
		}

		int size = Runtime.getRuntime().availableProcessors();
		if (cmdline.hasOption("s")) {
			size = Integer.parseInt(cmdline.getOptionValue("s"));
		} else if (cmdline.hasOption("size")) {
			size = Integer.parseInt(cmdline.getOptionValue("size"));
		}

		int thread = 1;
		if (cmdline.hasOption("t")) {
			thread = Integer.parseInt(cmdline.getOptionValue("t"));
		} else if (cmdline.hasOption("thread")) {
			thread = Integer.parseInt(cmdline.getOptionValue("thread"));
		}

		int repeat = 1;
		if (cmdline.hasOption("r")) {
			repeat = Integer.parseInt(cmdline.getOptionValue("r"));
		} else if (cmdline.hasOption("repeat")) {
			repeat = Integer.parseInt(cmdline.getOptionValue("repeat"));
		}

		MultyMemcachedClient mc = new MultyMemcachedClient();
		mc.addServer(ip, port);
		mc.init();

		CountDownLatch cdl = new CountDownLatch(thread);
		long t = System.currentTimeMillis();
		for (int i = 0; i < thread; i++) {
			new Thread(new Test.TestRunnable(mc, i * 10000, cdl, repeat))
					.start();
		}

		try {
			cdl.await();
		} catch (InterruptedException e) {

		}

		long all = 2 * thread * repeat;
		long usingtime = (System.currentTimeMillis() - t);
		log
				.info(String
						.format(
								"thread num=%d, repeat=%d,size=%d, all=%d ,velocity=%d , using time:%d",
								thread, repeat, size, all, 1000 * all
										/ usingtime, usingtime));

		mc.close();
	}

}
