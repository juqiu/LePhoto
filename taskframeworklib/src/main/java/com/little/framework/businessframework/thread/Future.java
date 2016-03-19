package com.little.framework.businessframework.thread;


/**
 *
 * Author: juqiu.lt
 * Date: 2014-12-4
 * Time: 下午3:30:34 
 *
 * @param <T>
 */
public interface Future<T> {
	
    void cancel();
    
    boolean isCancelled();
    
    boolean isDone();
    
    T get();
    
    void waitDone();
}
