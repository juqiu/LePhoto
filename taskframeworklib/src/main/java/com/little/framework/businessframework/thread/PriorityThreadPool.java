package com.little.framework.businessframework.thread;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * Author: juqiu.lt
 * Date: 2014-12-4
 * Time: 下午3:32:26 
 *
 */
public class PriorityThreadPool extends ThreadPool {

    public PriorityThreadPool() {
        this("priority-thread-pool", DEFAULT_POOL_SIZE);
    }

    public PriorityThreadPool(String name, int poolSize) {
        super(name, poolSize, poolSize, new PriorityBlockingQueue<Runnable>());
    }

    public <T> Future<T> submit(Job<T> job, FutureListener<T> listener, Priority priority) {
        if (priority == null) {
            priority = Priority.NORMAL;
        }
        PriorityJob<T> priorityJob = new PriorityJob<T>(job, priority.priority, priority.fifo);
        return super.submit(priorityJob, listener);
    }

    public <T> Future<T> submit(Job<T> job, Priority priority) {
        return submit(job, null, priority);
    }

    @Override
    public <T> Future<T> submit(Job<T> job, FutureListener<T> listener) {
        return submit(job, listener, null);
    }

    @Override
    public <T> Future<T> submit(Job<T> job) {
        return submit(job, null, null);
    }


    /**
     *
     */
    public static class Priority {

        public final static Priority LOW = new Priority(-1, true);

        public final static Priority NORMAL = new Priority(0, true);

        public final static Priority HIGH = new Priority(1, false);

        public final int priority;
        public final boolean fifo;

        public Priority(int priority, boolean fifo) {
            this.priority = priority;
            this.fifo = fifo;
        }
    }

    private static class PriorityJob<T> implements Job<T>, Comparable<PriorityJob> {

        private static final AtomicLong SEQ = new AtomicLong(0);

        private final Job<T> mJob;

        /**
         * the bigger, the prior.
         */
        private final int mPriority;

        /**
         * whether fifo(with same {@link #mPriority}).
         */
        private final boolean mFifo;

        /**
         * seq number.
         */
        private final long mSeqNum;

        public PriorityJob(Job<T> job, int priority, boolean fifo) {
            mJob = job;
            mPriority = priority;
            mFifo = fifo;
            mSeqNum = SEQ.getAndIncrement();
        }

        @Override
        public T run(JobContext jc) {
            return mJob.run(jc);
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(PriorityJob another) {
            return mPriority > another.mPriority ? -1 : (mPriority < another.mPriority ? 1 : subCompareTo(another));
        }

        private int subCompareTo(PriorityJob another) {
            int result = mSeqNum < another.mSeqNum ? -1 : (mSeqNum > another.mSeqNum ? 1 : 0);
            return mFifo ? result : -result;
        }
    }
}
