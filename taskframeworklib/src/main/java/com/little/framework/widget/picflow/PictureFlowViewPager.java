package com.little.framework.widget.picflow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author littletli
 *
 */
public class PictureFlowViewPager extends ViewPager {


	public PictureFlowViewPager(Context context) {
		super(context, null);
	}
	
	public PictureFlowViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	return false;
    }
}
