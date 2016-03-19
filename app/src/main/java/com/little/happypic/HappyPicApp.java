package com.little.happypic;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.little.framework.app.BaseApplication;



/**
 * Created by little on 16/3/8.
 */
public class HappyPicApp extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
