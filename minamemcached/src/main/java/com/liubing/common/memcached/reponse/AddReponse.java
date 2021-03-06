package com.liubing.common.memcached.reponse;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.util.IoBufferUtil;

public class AddReponse extends AbstractReponse<String> {

	public boolean parse(List<String> context, Charset defaultCharset) {

		String line = context.get(0);
		if (line.equals("STORED") || line.equals("NOT_STORED")) {
			context.remove(0);
			this.success = true;
			return true;
		}
		throw new IllegalStateException("Add command get a reponse :" + line);
	}

	public boolean parse(IoBuffer in, Charset defaultCharset) {
		   String line = IoBufferUtil.getString(in, MemcachedConstants.FLAG_LINE_END_BYTE);
	         if(line == null){
	                 return false;
	         }
	         if (line.equals("STORED\r\n") || line.equals("NOT_STORED\r\n")) {
	                 this.result = line;
	                 this.success = true;
	                 return true;
	         }
	         throw new IllegalStateException("Set command get a reponse :" + line);

	}

}
