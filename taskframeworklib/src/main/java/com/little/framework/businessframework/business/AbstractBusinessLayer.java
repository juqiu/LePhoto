package com.little.framework.businessframework.business;


import com.little.framework.businessframework.task.Task;

/**
 * Created by little on 16/3/8.
 */
public abstract class AbstractBusinessLayer implements BusinessTaskListener {



    @Override
    public void onTaskDone(Task<BusinessResponse> task) {

        BusinessTask<BusinessResponse> requestTask = (BusinessTask<BusinessResponse>) task;
        if (!requestTask.isResultHasSend()){
            handleResult((BusinessTask<BusinessResponse>) task);
        }

    }


    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
    protected void handleResult(BusinessTask<BusinessResponse> task){
        BusinessResult result = task.getResult();
        result.putAll(task.getPack());
        BusinessResponse response = task.get();
        if (response.getResponse() != null
                || (response.getResponse() == null && response.getException() == null)) {
            //
            result.mResultCode = BusinessResult.RESULT_SUCCESS;
            result.setData(response.getResponse());
        }else {
            result.mResultCode = BusinessResult.RESULT_FAIL;
            result.setException(response.getException());
        }
        task.sendResultToCallback();
    }

}
