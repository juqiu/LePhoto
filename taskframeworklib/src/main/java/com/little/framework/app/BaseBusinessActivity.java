package com.little.framework.app;

import android.os.Looper;

import com.little.framework.businessframework.business.BusinessCallback;
import com.little.framework.businessframework.business.BusinessResult;


/**
 *
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午4:35:49 
 *
 *
 */
public class BaseBusinessActivity extends BaseActivity implements BusinessCallback {

    private Thread mMainThread = Looper.getMainLooper().getThread();
	
    // -------------- Handle the BusinessResult on MainThread --------------
    protected void onBusinessResultImpl(BusinessResult result) {
    	
    	
    }

    
    @Override
    public final void onBusinessResult(final BusinessResult result) {
        if (result == null) {
            return;
        }
        if (!isFinishing()) {
            if (isMainThread()) {
                onBusinessResultImpl(result);
            } else {
            	runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            // double check.
                            onBusinessResultImpl(result);
                        }
                    }
                });
            }
        }
    }
    

    /**
     * 
     * @return
     */
    public final boolean isMainThread() {
        return mMainThread == Thread.currentThread();
    }
}
