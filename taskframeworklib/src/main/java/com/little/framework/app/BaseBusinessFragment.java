package com.little.framework.app;

import android.app.Activity;

import com.little.framework.businessframework.business.BusinessCallback;
import com.little.framework.businessframework.business.BusinessResult;


/**
 *
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午4:35:49
 *
 */
public class BaseBusinessFragment extends BaseFragment implements BusinessCallback {

	
	
    // -------------- Handle the BusinessResult on MainThread --------------
    protected void onBusinessResultImpl(BusinessResult result) {
    	
    	
    }

    
    @Override
    public final void onBusinessResult(final BusinessResult result) {
        if (result == null) {
            return;
        }
        if (isAlive()) {
            if (isMainThread()) {
                onBusinessResultImpl(result);
            } else {
                post(new Runnable() {
                    @Override
                    public void run() {
                        if (isAlive()) {
                            // double check.
                            onBusinessResultImpl(result);
                        }
                    }
                });
            }
        }
    }
    
    protected final boolean isAlive() {
        Activity activity = getActivity();
        return activity != null && !activity.isFinishing() && !isRemoving() && !isDetached();
    }

}
