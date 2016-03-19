
package com.little.framework.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;


/**
 *
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午4:19:46 
 *
 */
public class BaseFragment extends Fragment {

    private Application mApplication;

    private Thread mMainThread = Looper.getMainLooper().getThread();
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mApplication = activity.getApplication();

        ((BaseApplication) mApplication).dispatchFragmentAttachedInner(this, activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BaseApplication) mApplication).dispatchFragmentCreatedInner(this, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((BaseApplication) mApplication).dispatchFragmentViewCreatedInner(this, view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        ((BaseApplication) mApplication).dispatchFragmentStartedInner(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((BaseApplication) mApplication).dispatchFragmentResumedInner(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        ((BaseApplication) mApplication).dispatchFragmentPausedInner(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        ((BaseApplication) mApplication).dispatchFragmentStoppedInner(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ((BaseApplication) mApplication).dispatchFragmentSaveInstanceStateInner(this, outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((BaseApplication) mApplication).dispatchFragmentDestroyedInner(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        ((BaseApplication) mApplication).dispatchFragmentDetachedInner(this);
    }

    public final void runOnUiThread(Runnable action) {
        if (!isMainThread()) {
            mMainHandler.post(action);
        } else {
            action.run();
        }
    }


    /**
     * 
     * @param r
     */
    public final void post(Runnable r) {
        mMainHandler.post(r);
    }


    /**
     * 
     * @param r
     * @param delayMillis
     */
    public final void postDelayed(Runnable r, long delayMillis) {
        mMainHandler.postDelayed(r, delayMillis);
    }

    
    /**
     * 
     * @param r
     */
    public final void removeCallbacks(Runnable r) {
        mMainHandler.removeCallbacks(r);
    }


    /**
     * 
     * @return
     */
    public final boolean isMainThread() {
        return mMainThread == Thread.currentThread();
    }
}
