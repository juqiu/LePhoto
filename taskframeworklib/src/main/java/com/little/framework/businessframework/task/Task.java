package com.little.framework.businessframework.task;


import com.little.framework.businessframework.thread.Future;
import com.little.framework.businessframework.thread.FutureListener;
import com.little.framework.businessframework.thread.PriorityThreadPool;
import com.little.framework.businessframework.thread.PriorityThreadPoolFactory;
import com.little.framework.businessframework.thread.ThreadPool;

/**
 * 
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 上午10:48:48 
 * 
 * <p>Task enables execution and callback on a worker thread, and never block other threads.</p>
 * <p>Override {@link #onExecute} to do background work. Call {@link #scheduleExecute} wherever
 * you want to schedule next execution on a worker thread. Call {@link #scheduleFinish} wherever
 * you want to schedule finish of this task on a worker thread.</p>
 */


public abstract class Task<T> implements FutureListener<T> {

    /**
     * Task priority definition.
     */
    public final static class Priority extends PriorityThreadPool.Priority {

        public Priority(int priority, boolean fifo) {
            super(priority, fifo);
        }
    }

    public final static int STATUS_PENDING = 0;
    public final static int STATUS_RUNNING = 1;
    public final static int STATUS_SUCCEED = 2;
    public final static int STATUS_FAILED = 3;

    public final static int NO_ID = -1;

 
    private final int mId;

    private final ThreadPool.Job<T> mJob;
    
    private Future<T> mFuture;

    private PriorityThreadPool mThreadPool;
    private Priority mPriority;
    private TaskListener<T> mListener;

    private volatile T mResult;

    private volatile int mStatus = STATUS_PENDING;
    
    
    private boolean mCancelable = true;
    
    
    private final ThreadLocal<Boolean> mWorkerThread = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return null;
        }
    };

    public Task() {
        this(NO_ID);
    }

    public Task(int id) {
        mId = id;
        mJob = new ThreadPool.Job<T>() {
            @Override
            public T run(ThreadPool.JobContext jc) {
                // mark worker thread.
                mWorkerThread.set(true);
                onExecute(jc);
                return null;
            }
        };
    }

    /**
     * Execute this task.
     *
     * @return This instance of Task.
     */
    public final Task<T> execute() {
        if (mStatus != STATUS_PENDING) {
            switch (mStatus) {
            case STATUS_RUNNING:
                throw new IllegalStateException("Cannot execute task:"
                        + " the task is already running.");
            case STATUS_SUCCEED:
            case STATUS_FAILED:
                throw new IllegalStateException("Cannot execute task:"
                        + " the task has already been executed "
                        + "(a task can be executed only once)");
            }
        }
        mStatus = STATUS_RUNNING;

        scheduleExecute();
        return this;
    }

    /**
     * Execute this task with {@link TaskListener}.
     *
     * @param listener TaskListener to be invoked when task finished.
     * @return This instance of Task.
     */
    public final Task<T> execute(TaskListener<T> listener) {
        setListener(listener);
        return execute();
    }

    /**
     * Override this method to perform a computation on a <b>worker thread</b>.
     *
     * @param jobContext Job context, which is used to switch the task mode,
     *                   see {@link ThreadPool#MODE_CPU}, {@link ThreadPool#MODE_NETWORK} or {@link ThreadPool#MODE_NONE}.
     */
    protected abstract void onExecute(ThreadPool.JobContext jobContext);

    /**
     * Schedule the next execution of this task, which means {@link #onExecute} will be called later on a <b>worker thread</b>.
     */
    protected final void scheduleExecute() {
        performExecute(mJob, this);
    }

    private void performExecute(ThreadPool.Job<T> job, FutureListener<T> listener) {
        PriorityThreadPool threadPool = mThreadPool;
        Priority priority = mPriority;
        if (threadPool == null) {
            threadPool = PriorityThreadPoolFactory.getDefaultThreadPool();
        }
        if (priority == null) {
            throw new RuntimeException("priority should be specified before execution");
        }
        mFuture = threadPool.submit(job, listener, priority);
    }

    /**
     * Schedule finish this task, {@link TaskListener} will be invoked if specified on a <b>worker thread</b>.
     *
     * @param succeed whether this task succeed.
     * @param result  The final result of this task.
     */
    protected final void scheduleFinish(boolean succeed, T result) {
        performFinish(succeed, result);
    }

    
    /**
     * 
     * @param succeed
     * @param result
     */
    private void performFinish(boolean succeed, T result) {
        mStatus = succeed ? STATUS_SUCCEED : STATUS_FAILED;
        mResult = result;

        final TaskListener<T> listener = mListener;
        if (listener == null) {
            return;
        }
        Boolean workerThread = mWorkerThread.get();
        if (workerThread != null && workerThread) {
            // avoid context switch.
            listener.onTaskDone(this);
        } else {
            performExecute(new ThreadPool.Job<T>() {
                @Override
                public T run(ThreadPool.JobContext jc) {
                    listener.onTaskDone(Task.this);
                    return null;
                }
            }, null);
        }
    }
    
    
    public Task<T> setCancelable(boolean cancelable){
        mCancelable = cancelable;
        return this;
    }
    
    /**
     * 
     */
    public void cancel(){        
        if (mFuture != null && mCancelable){
            mFuture.cancel();
        }       
    }


    /**
     * 
     * @return
     */
    public final int getId() {
        return mId;
    }

    
    /**
     * 
     * @return
     */
    public final int getStatus() {
        return mStatus;
    }

    /**
     * Get the execution result of this task.
     *
     * @return execution result.
     */
    public final T get() {
        return mResult;
    }

    /**
     * Specify which {@link ThreadPool} is used to provide worker thread. Default one is {@link PriorityThreadPoolFactory#getDefaultThreadPool}.
     *
     * @param threadPool Thread pool
     * @return This instance of Task.
     */
    public final Task<T> setThreadPool(PriorityThreadPool threadPool) {
        mThreadPool = threadPool;
        return this;
    }

    /**
     * Specify the {@link Priority} of this task.
     *
     * @param priority Priority.
     * @return This instance of Task.
     */
    public final Task<T> setPriority(Priority priority) {
        mPriority = priority;
        return this;
    }

    /**
     * Specify the {@link TaskListener} of this task.
     *
     * @param listener task listener.
     * @return This instance of task.
     */
    public final Task<T> setListener(TaskListener<T> listener) {
        mListener = listener;
        return this;
    }
    
    /**
     * 
     */
    public void onFutureBegin(Future<T> future){
        
    }
    
    /**
     * 
     */
    public void onFutureDone(Future<T> future){

    }
}