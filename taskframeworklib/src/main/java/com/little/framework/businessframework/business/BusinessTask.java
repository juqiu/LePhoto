package com.little.framework.businessframework.business;

import com.little.framework.businessframework.task.Task;
import com.little.framework.businessframework.thread.Future;
import com.little.framework.utils.Pack;

import java.lang.ref.WeakReference;


/**
 *
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午3:21:38 
 *
 * @param <T>
 */
public abstract class BusinessTask<T> extends Task<T> {

    public final static Priority PRIORITY_HIGH = new Priority(20, false);

    public final static Priority PRIORITY_NORMAL = new Priority(0, true);

    public final static Priority PRIORITY_LOW = new Priority(-10, true);

    public final static Priority PRIORITY_LOWEST = new Priority(-20, true);

    //
    private WeakReference<BusinessCallback> mCallbackRef;
    
    // 
    private BusinessCallback mCallback;
    
    
    //
    private final BusinessResult mResult;
    

    // Can Pack args
    private final Pack<String> mPack = new Pack<String>();


    // boolean
    private boolean resultHasSend = false;

    
    
    /**
     * 
     */
    public BusinessTask() {
        this(NO_ID, null, false);
    }

    
    /**
     * 
     * @param id
     * @param callback
     */
    public BusinessTask(int id, BusinessCallback callback) {
        this(id, callback, false);
    }

    
    /**
     * 
     * @param id
     * @param callback
     * @param persistCallback
     */
    public BusinessTask(int id, BusinessCallback callback, boolean persistCallback) {
        super(id);
        setPriority(PRIORITY_NORMAL);
        setCallback(callback, persistCallback);
        mResult = new BusinessResult(id);
    }

    
    /**
     * 
     * @param callback
     * @param persist
     */
    public final void setCallback(BusinessCallback callback, boolean persist) {
        if (!persist) {
            mCallbackRef = new WeakReference<BusinessCallback>(callback);
            mCallback = null;
        } else {
            mCallbackRef = null;
            mCallback = callback;
        }
    }

    
    /**
     * 
     * @return
     */
    protected BusinessCallback getCallback() {
        if (mCallback != null) {
            return mCallback;
        } else if (mCallbackRef != null) {
            return mCallbackRef.get();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public final Pack<String> getPack() {
        return mPack;
    }

    /**
     * 
     * @return
     */
    public final BusinessResult getResult() {
        return mResult;
    }

    
    /**
     * 
     * 
     */
    public final void sendResultToCallback() {
        BusinessCallback callback = getCallback();
        if (callback != null) {
            resultHasSend = true;
            callback.onBusinessResult(mResult);
        }
    }


    /**
     *
     */
    public boolean isResultHasSend(){
        return resultHasSend;
    }


    
    /**
     * 
     */
    public void onFutureDone(Future<T> future){
        if (future.isCancelled()){
            mResult.mResultCode = BusinessResult.RESULT_CANCEL;
            sendResultToCallback();
        }       
    }
}
