package com.little.framework.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Author: juqiu.lt
 * Date: 2014-11-20
 * Time: 下午1:51:00
 */
public class BaseActivity extends AppCompatActivity {

    private boolean mResumed   = false;

    private boolean mStarted   = false;

    private boolean mDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ((BaseApplication) getApplication()).dispatchActivityCreatedInner(this, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;

        ((BaseApplication) getApplication()).dispatchActivityResumedInner(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStarted = true;

        ((BaseApplication) getApplication()).dispatchActivityStartedInner(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;

        ((BaseApplication) getApplication()).dispatchActivityPausedInner(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResumed = false;
        mStarted = false;

        ((BaseApplication) getApplication()).dispatchActivityStoppedInner(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDestroyed = true;

        ((BaseApplication) getApplication()).dispatchActivityDestroyedInner(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ((BaseApplication) getApplication()).dispatchActivitySaveInstanceStateInner(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ((BaseApplication) getApplication()).dispatchActivityResultInner(this, requestCode, resultCode, data);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();

        ((BaseApplication) getApplication()).dispatchActivityUserInteractionInner(this);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        ((BaseApplication) getApplication()).dispatchActivityUserLeaveHintInner(this);
    }

    @Override
    public void onSupportContentChanged() {
        super.onSupportContentChanged();

        ((BaseApplication) getApplication()).dispatchActivityContentChangedInner(this);
    }

    public final boolean isActivityResumed() {
        return mResumed;
    }

    public final boolean isActivityStarted() {
        return mStarted;
    }

    public final boolean isActivityDestroyed() {
        return mDestroyed;
    }

    @Override
    public boolean isFinishing() {
        return super.isFinishing() || isActivityDestroyed();
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        try{
            super.setSupportActionBar(toolbar);
        }catch (Exception e){
            
        }
    }
}
