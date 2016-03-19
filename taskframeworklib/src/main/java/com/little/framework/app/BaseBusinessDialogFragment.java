package com.little.framework.app;


import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;

import com.little.framework.businessframework.business.BusinessCallback;
import com.little.framework.businessframework.business.BusinessResult;


/**
 * Author: juqiu.lt@alibaba
 * Date: 2015-10-08
 * Time: 13:02
 */
public class BaseBusinessDialogFragment extends DialogFragment implements BusinessCallback {


    private Thread mMainThread = Looper.getMainLooper().getThread();
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

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
                mMainHandler.post(new Runnable() {
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

    /**
     *
     * @return
     */
    public final boolean isMainThread() {
        return mMainThread == Thread.currentThread();
    }
}
