package com.little.framework.businessframework.task;

/**
 * 
 *
 * Author: juqiu.lt
 * Date: 2015-2-2
 * Time: 上午9:58:42 
 *
 * @param <T>
 */
public interface TaskListener<T> {
    
    public void onTaskDone(Task<T> task);
}
