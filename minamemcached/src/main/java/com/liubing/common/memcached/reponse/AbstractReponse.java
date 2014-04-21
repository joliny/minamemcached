package com.liubing.common.memcached.reponse;



public abstract class AbstractReponse<T> implements MemcachedResponse<T> {

	protected boolean success = false;

	protected T result;

	public T getResult() {
		return result;
	}

	public boolean isSuccess() {
		return success;
	}

//	@Deprecated
//	public boolean parse(final CachedByteBuffer cacheBuf, Charset charSet) {
//
//		int oldBegin = cacheBuf.getBegin();
//		int oldLimit = cacheBuf.getLength();
//		boolean result = false;
//
//		try {
//			result = doParse(cacheBuf, charSet);
//			return result;
//		} finally {
//			if (result == true) {
//				this.success = true;
//			} else if (result == false) {
//				cacheBuf.reset(oldBegin, oldLimit);
//			}
//		}
//
//	}

//	abstract protected boolean doParse(CachedByteBuffer cacheBuf,
//			Charset charSet);

}
