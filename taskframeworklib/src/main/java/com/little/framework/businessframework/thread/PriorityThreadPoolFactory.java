package com.little.framework.businessframework.thread;

/**
 *
 * Author: juqiu.lt
 * Date: 2014-12-4
 * Time: 下午3:34:28
 */
public class PriorityThreadPoolFactory {

    // ------------- default --------------
    private static class InstanceHolder {
        public static final PriorityThreadPool INSTANCE = new PriorityThreadPool();
    }


    //-------------- BusinessThreadPool--------
    private static class BusinessThreadPoolInstanceHolder {
        public static final PriorityThreadPool INSTANCE = new PriorityThreadPool("busi_threadpool",
                PriorityThreadPool.CPU_COUNT + 1);
    }


    /**
     *
     * @return
     */
    public static PriorityThreadPool getDefaultThreadPool() {
        return InstanceHolder.INSTANCE;
    }

    /*
     *
     * @return
     */
    public static PriorityThreadPool getBusinessThreadPool() {
        return BusinessThreadPoolInstanceHolder.INSTANCE;
    }
}
