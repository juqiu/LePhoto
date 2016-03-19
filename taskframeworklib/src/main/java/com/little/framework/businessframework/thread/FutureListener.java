package com.little.framework.businessframework.thread;


/**
 *
 * Author: juqiu.lt
 * Date: 2014-12-4
 * Time: 下午3:31:10 
 *
 * @param <T>
 */
public interface FutureListener<T> {
    
    void onFutureBegin(Future<T> future);
    
    void onFutureDone(Future<T> future);
}