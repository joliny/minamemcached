package com.liubing.common.memcached.reponse;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

import com.liubing.common.memcached.MemcachedConstants;
import com.liubing.common.memcached.util.IoBufferUtil;


public class GetReponse extends AbstractReponse<byte[]> {

	
	public GetReponse() {
		this.result = new byte[0];
	}

	

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public String getKey() {
		return key;
	}

	private int length = -1;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	private String flags;
	private String key;

	public void setKey(String key) {
		this.key = key;

	}

	public byte[] getResult() {

		return this.result;
	}

//	@Deprecated
//	protected boolean doParse(final CachedByteBuffer cacheBuf, Charset charSet) {
//		if (cacheBuf.getLength() < MemcachedConstants.FLAG_END.length()) {
//			if (log.isDebugEnabled()) {
//				log.debug(String.format("Parse error:limt=%d<5", cacheBuf
//						.getLength()));
//			}
//			return false;
//		}
//
//		String str = cacheBuf.getString(MemcachedConstants.FLAG_END.length(),
//				charSet);
//		if (str.equals(MemcachedConstants.FLAG_END)) {
//			
//			return true;
//		} else if (str.equals("VALUE")) {
//			String line = cacheBuf.getString("\r\n", charSet);
//			if (line == null) {
//				if (log.isTraceEnabled()) {
//					log.trace(String.format(
//							"Parse error:Can't find \\r\\n ,  buf:%s",
//							new String(cacheBuf.getBuf(), cacheBuf.getBegin(),
//									cacheBuf.getLength())));
//				}
//				return false;
//			}
//			String[] tokens = line.split(" ");
//			this.key = tokens[1];
//			this.flags = tokens[2];
//			this.length = Integer.parseInt(tokens[3].trim());
//
//			if (this.length + 7 > cacheBuf.getLength()) {
//				if (log.isTraceEnabled()) {
//					log
//							.trace(String
//									.format(
//											"Parse error:length=%d , limit=%d , begin=%d  , buf:%s",
//											this.length, cacheBuf.getLength(),
//											cacheBuf.getBegin(), new String(
//													cacheBuf.getBuf(), cacheBuf
//															.getBegin(),
//													cacheBuf.getLength())));
//				}
//
//				return false;
//			}
//
//			result = cacheBuf.getBytes(length);
//			cacheBuf.skip(7);
//			return true;
//
//		}
//		if (log.isDebugEnabled()) {
//			log
//					.debug(String
//							.format(
//									"Parse error:Get command's reponse must be started VALUE or END\r\n",
//									this.length, cacheBuf.getLength()));
//		}
//
//		return false;
//	}

	public boolean parse(List<String> context, Charset defaultCharset) {

		String firstLine =  context.get(0);
		
		if(firstLine.equals("END")){
			this.result = new byte[0];
			context.remove(0);
			this.success = true;
			return true;
		}else if (firstLine.startsWith("VALUE")){
			if(context.size() <3){
				return false;
			}
			
			String[] tokens = firstLine.split(" ");
			this.key = tokens[1];
			this.flags = tokens[2];
			this.length = Integer.parseInt(tokens[3].trim());
			
			this.result =  context.get(1).substring(0, length).getBytes(defaultCharset);
			
			context.remove(0);
			context.remove(0);
			context.remove(0);
			this.success = true;
			return true;
		}
		throw new IllegalStateException("Parse error:Get command's reponse must be started VALUE or END\r\n , line=" + firstLine);
	}

	public boolean parse(IoBuffer in, Charset defaultCharset) {
	     String firstLine = IoBufferUtil.getString(in,MemcachedConstants.FLAG_LINE_END_BYTE);
         if(firstLine==null){
                 return false;
         }

         firstLine = firstLine.trim();
         if(firstLine.equals("END")){
                 this.result = new byte[0];
                 this.success = true;
                 return true;
         }else if (firstLine.startsWith("VALUE")){

                 String[] tokens = firstLine.split(" ");
                 this.key = tokens[1];
                 this.flags = tokens[2];
                 this.length = Integer.parseInt(tokens[3]);

                 if(this.length == 0){
                         System.out.println(0);
                 }

                 if(in.remaining()<this.length+7){
                         return false;
                 }
                 this.result = new byte[this.length];


                 in.get(this.result);
                 in.skip(7);

                 this.success = true;
                 return true;
         }
         throw new IllegalStateException("Parse error:Get command's reponse must be started VALUE or END\r\n ," +
                         " line=" + firstLine + " line's length=" + firstLine.length());

	}

}
