package com.little.framework.businessframework.business;

import com.little.framework.businessframework.thread.PriorityThreadPoolFactory;
import com.little.framework.businessframework.thread.ThreadPool;
import com.little.framework.network.api.BaseApi;

/**
 * Created by little on 16/3/7.
 */
public class ApiTask extends BusinessTask {

    //
    protected BaseApi mApi;

    /**
     *
     * @param id
     * @param callback
     */
    public ApiTask(int id, BaseApi api, BusinessCallback callback) {
        super(id, callback);
        this.mApi = api;
        setThreadPool(PriorityThreadPoolFactory.getBusinessThreadPool());
    }


    @Override
    protected void onExecute(ThreadPool.JobContext jobContext) {
        Object networkResult  = null;
        Exception exception = null;
        if (mApi != null) {
            try {
                networkResult = mApi.execute();
            } catch (Exception e) {
                exception = e;
            }
        }
        if (networkResult != null) {
            BusinessResponse response = new BusinessResponse();
            response.setResponse(networkResult);
            scheduleFinish(true, response);
        }else{
            BusinessResponse response = new BusinessResponse();
            response.setException(exception);
            scheduleFinish(false, response);
        }
    }
}
