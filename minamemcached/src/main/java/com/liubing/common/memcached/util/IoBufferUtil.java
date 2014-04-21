package com.liubing.common.memcached.util;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;

public final class IoBufferUtil {

	private IoBufferUtil() {

	}

	public static String getString(IoBuffer in, byte[] buf) {

		int pos = indexOf(in, buf);

		if (pos == -1) {
			return null;
		}

		try {
			return in.getString(pos - in.position(), Charset.defaultCharset()
					.newDecoder());
		} catch (CharacterCodingException e) {
			throw new IllegalStateException();
		}

	}

	public static int indexOf(IoBuffer in, byte[] buf) {

		if (buf == null || buf.length == 0) {
			throw new IllegalArgumentException();
		}
		int oldposition = in.position();
		int oldlimit = in.limit();
		try {
			int pos = -1;
			for (;;) {

				pos = in.indexOf(buf[0]);

				if (pos == -1) {
					break;
				}

				in.position(pos);

				if (startWith(in, buf)) {
					return pos + buf.length;
				}

				in.get();

			}

			return pos;
		} finally {
			in.position(oldposition);
			in.limit(oldlimit);
		}
	}

	/**
	 * 
	 * no side effect
	 * 
	 * @param in
	 * @param buf
	 * @return
	 */
	public static boolean startWith(IoBuffer in, byte[] buf) {

		if (buf == null || buf.length == 0) {
			return false;
		}

		if (buf.length > in.limit()-in.position()) {
			return false;
		}

		int oldposition = in.position();
		int oldlimit = in.limit();

		try {

			for (byte b : buf) {

				if (b != in.get()) {
					return false;
				}
			}

			return true;
		} finally {
			in.position(oldposition);
			in.limit(oldlimit);
		}

	}
}
