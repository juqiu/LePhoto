package com.little.framework.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;

/**
 * Alibaba Author: juqiu.lt Date: 2014-11-20 Time: 上午10:35:45
 */
public class BaseApplication extends Application {

    private int     mActivityVisibleCount             = 0;

    private boolean mIgnoreActivityVisibleCountChange = false;

    // ------------------ application callbacks ------------------
    public interface ApplicationCallbacks {

        //
        void onApplicationEnterForeground(Application application);

        //
        void onApplicationEnterBackground(Application application);
    }
    public interface OnLowMemory{

        public void onLowMemory();

    }
    private final ArrayList<ApplicationCallbacks> mApplicationCallbacks = new ArrayList<ApplicationCallbacks>();

    // ------------------ activity lifecycle ---------------------------
    public interface ActivityLifecycleCallbacks {

        void onActivityCreated(Activity activity, Bundle savedInstanceState);

        void onActivityStarted(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityStopped(Activity activity);

        void onActivitySaveInstanceState(Activity activity, Bundle outState);

        void onActivityDestroyed(Activity activity);
    }

    public interface ActivityUserCallbacks {

        void onActivityUserInteraction(Activity activity);

        void onActivityUserLeaveHint(Activity activity);
    }

    public interface ActivityResultCallbacks {

        void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
    }

    public interface ActivityContentCallbacks {

        void onActivityContentChanged(Activity activity);
    }

    private final ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList<ActivityLifecycleCallbacks>();

    private final ArrayList<ActivityUserCallbacks>      mActivityUserCallbacks      = new ArrayList<ActivityUserCallbacks>();

    private final ArrayList<ActivityResultCallbacks>    mActivityResultCallbacks    = new ArrayList<ActivityResultCallbacks>();

    private final ArrayList<ActivityContentCallbacks>   mActivityContentCallbacks   = new ArrayList<ActivityContentCallbacks>();

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (mActivityLifecycleCallbacks) {
            mActivityLifecycleCallbacks.remove(callback);
        }
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityLifecycleCallbacks) {
            if (mActivityLifecycleCallbacks.size() > 0) {
                callbacks = mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    public void registerActivityUserCallbacks(ActivityUserCallbacks callback) {
        synchronized (mActivityUserCallbacks) {
            mActivityUserCallbacks.add(callback);
        }
    }

    public void unregisterActivityUserCallbacks(ActivityUserCallbacks callback) {
        synchronized (mActivityUserCallbacks) {
            mActivityUserCallbacks.remove(callback);
        }
    }

    private Object[] collectActivityUserCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityUserCallbacks) {
            if (mActivityUserCallbacks.size() > 0) {
                callbacks = mActivityUserCallbacks.toArray();
            }
        }
        return callbacks;
    }

    public void registerActivityResultCallbacks(ActivityResultCallbacks callback) {
        synchronized (mActivityResultCallbacks) {
            mActivityResultCallbacks.add(callback);
        }
    }

    public void unregisterActivityResultCallbacks(ActivityResultCallbacks callback) {
        synchronized (mActivityResultCallbacks) {
            mActivityResultCallbacks.remove(callback);
        }
    }

    private Object[] collectActivityResultCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityResultCallbacks) {
            if (mActivityResultCallbacks.size() > 0) {
                callbacks = mActivityResultCallbacks.toArray();
            }
        }
        return callbacks;
    }

    public void registerActivityContentCallbacks(ActivityContentCallbacks callback) {
        synchronized (mActivityContentCallbacks) {
            mActivityContentCallbacks.add(callback);
        }
    }

    public void unregisterActivityContentCallbacks(ActivityContentCallbacks callback) {
        synchronized (mActivityContentCallbacks) {
            mActivityContentCallbacks.remove(callback);
        }
    }

    private Object[] collectActivityContentCallbacks() {
        Object[] callbacks = null;
        synchronized (mActivityContentCallbacks) {
            if (mActivityContentCallbacks.size() > 0) {
                callbacks = mActivityContentCallbacks.toArray();
            }
        }
        return callbacks;
    }

    // ------------------ Internal API ------------------

    /* package */void dispatchActivityCreatedInner(Activity activity, Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityCreated(activity, savedInstanceState);
            }
        }
    }

    /* package */void dispatchActivityStartedInner(Activity activity) {
        updateActivityVisibleCount(true, mIgnoreActivityVisibleCountChange);
        mIgnoreActivityVisibleCountChange = false;
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityStarted(activity);
            }
        }
    }

    /* package */void dispatchActivityResumedInner(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityResumed(activity);
            }
        }
    }

    /* package */void dispatchActivityPausedInner(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPaused(activity);
            }
        }
    }

    /* package */void dispatchActivityStoppedInner(Activity activity) {
        mIgnoreActivityVisibleCountChange = isActivityConfigChanging(activity);
        updateActivityVisibleCount(false, mIgnoreActivityVisibleCountChange);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityStopped(activity);
            }
        }
    }

    /* package */void dispatchActivitySaveInstanceStateInner(Activity activity, Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivitySaveInstanceState(activity, outState);
            }
        }
    }

    /* package */void dispatchActivityDestroyedInner(Activity activity) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityDestroyed(activity);
            }
        }
    }

    /* package */void dispatchActivityUserInteractionInner(Activity activity) {
        Object[] callbacks = collectActivityUserCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityUserCallbacks) callbacks[i]).onActivityUserInteraction(activity);
            }
        }
    }

    /* package */void dispatchActivityUserLeaveHintInner(Activity activity) {
        Object[] callbacks = collectActivityUserCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityUserCallbacks) callbacks[i]).onActivityUserLeaveHint(activity);
            }
        }
    }

    /* package */void dispatchActivityResultInner(Activity activity, int requestCode, int resultCode, Intent data) {
        Object[] callbacks = collectActivityResultCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityResultCallbacks) callbacks[i]).onActivityResult(activity, requestCode, resultCode, data);
            }
        }
    }

    /* package */void dispatchActivityContentChangedInner(Activity activity) {
        Object[] callbacks = collectActivityContentCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((ActivityContentCallbacks) callbacks[i]).onActivityContentChanged(activity);
            }
        }
    }

    private void updateActivityVisibleCount(boolean increase, boolean ignore) {
        if (increase) {
            final int prev = mActivityVisibleCount;
            mActivityVisibleCount++;
            if (prev == 0 && !ignore) {
                dispatchApplicationEnterForeground();
            }
        } else {
            mActivityVisibleCount--;
            if (mActivityVisibleCount == 0 && !ignore) {
                dispatchApplicationEnterBackground();
            }
        }
    }

    public void registerApplicationCallbacks(ApplicationCallbacks callback) {
        synchronized (mApplicationCallbacks) {
            mApplicationCallbacks.add(callback);
        }
    }

    public void unregisterApplicationCallbacks(ApplicationCallbacks callback) {
        synchronized (mApplicationCallbacks) {
            mApplicationCallbacks.remove(callback);
        }
    }

    private void dispatchApplicationEnterForeground() {
        Object[] callbacks = collectApplicationCallbacks();
        if (callbacks != null) {
            for (Object c : callbacks) {
                ((ApplicationCallbacks) c).onApplicationEnterForeground(this);
            }
        }
    }

    private void dispatchApplicationEnterBackground() {
        Object[] callbacks = collectApplicationCallbacks();
        if (callbacks != null) {
            for (Object c : callbacks) {
                ((ApplicationCallbacks) c).onApplicationEnterBackground(this);
            }
        }
    }

    private Object[] collectApplicationCallbacks() {
        Object[] callbacks = null;
        synchronized (mApplicationCallbacks) {
            if (mApplicationCallbacks.size() > 0) {
                callbacks = mApplicationCallbacks.toArray();
            }
        }
        return callbacks;
    }

    @SuppressLint("NewApi")
    private static boolean isActivityConfigChanging(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return activity.isChangingConfigurations();
        }
        return activity.getChangingConfigurations() != 0;
    }

    // ~~~~~~~~~~~~~~~~~~~~ fragment lifecycle ~~~~~~~~~~~~~~~~~~~~
    public interface FragmentLifecycleCallbacks {

        void onFragmentAttached(Fragment fragment, Activity activity);

        void onFragmentCreated(Fragment fragment, Bundle savedInstanceState);

        void onFragmentStarted(Fragment fragment);

        void onFragmentResumed(Fragment fragment);

        void onFragmentPaused(Fragment fragment);

        void onFragmentStopped(Fragment fragment);

        void onFragmentSaveInstanceState(Fragment fragment, Bundle outState);

        void onFragmentDestroyed(Fragment fragment);

        void onFragmentDetached(Fragment fragment);
    }

    public interface FragmentViewCallbacks {

        void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState);
    }

    private final ArrayList<FragmentLifecycleCallbacks> mFragmentLifecycleCallbacks = new ArrayList<FragmentLifecycleCallbacks>();

    private final ArrayList<FragmentViewCallbacks>      mFragmentViewCallbacks      = new ArrayList<FragmentViewCallbacks>();

    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (mFragmentLifecycleCallbacks) {
            mFragmentLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callback) {
        synchronized (mFragmentLifecycleCallbacks) {
            mFragmentLifecycleCallbacks.remove(callback);
        }
    }

    private Object[] collectFragmentLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (mFragmentLifecycleCallbacks) {
            if (mFragmentLifecycleCallbacks.size() > 0) {
                callbacks = mFragmentLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    public void registerFragmentViewCallbacks(FragmentViewCallbacks callback) {
        synchronized (mFragmentViewCallbacks) {
            mFragmentViewCallbacks.add(callback);
        }
    }

    public void unregisterFragmentViewCallbacks(FragmentViewCallbacks callback) {
        synchronized (mFragmentViewCallbacks) {
            mFragmentViewCallbacks.remove(callback);
        }
    }

    private Object[] collectFragmentViewCallbacks() {
        Object[] callbacks = null;
        synchronized (mFragmentViewCallbacks) {
            if (mFragmentViewCallbacks.size() > 0) {
                callbacks = mFragmentViewCallbacks.toArray();
            }
        }
        return callbacks;
    }

    public boolean isApplicationForground() {
        return mActivityVisibleCount > 0;
    }

    // ------------------ Internal API ------------------

    /* package */void dispatchFragmentAttachedInner(Fragment fragment, Activity activity) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentAttached(fragment, activity);
            }
        }
    }

    /* package */void dispatchFragmentCreatedInner(Fragment fragment, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentCreated(fragment, savedInstanceState);
            }
        }
    }

    /* package */void dispatchFragmentStartedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentStarted(fragment);
            }
        }
    }

    /* package */void dispatchFragmentResumedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentResumed(fragment);
            }
        }
    }

    /* package */void dispatchFragmentPausedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentPaused(fragment);
            }
        }
    }

    /* package */void dispatchFragmentStoppedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentStopped(fragment);
            }
        }
    }

    /* package */void dispatchFragmentSaveInstanceStateInner(Fragment fragment, Bundle outState) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentSaveInstanceState(fragment, outState);
            }
        }
    }

    /* package */void dispatchFragmentDestroyedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentDestroyed(fragment);
            }
        }
    }

    /* package */void dispatchFragmentDetachedInner(Fragment fragment) {
        Object[] callbacks = collectFragmentLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentLifecycleCallbacks) callbacks[i]).onFragmentDetached(fragment);
            }
        }
    }

    /* package */void dispatchFragmentViewCreatedInner(Fragment fragment, View view, Bundle savedInstanceState) {
        Object[] callbacks = collectFragmentViewCallbacks();
        if (callbacks != null) {
            for (int i = 0; i < callbacks.length; i++) {
                ((FragmentViewCallbacks) callbacks[i]).onFragmentViewCreated(fragment, view, savedInstanceState);
            }
        }
    }
}
