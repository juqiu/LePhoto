package com.little.framework.widget.picflow;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 
 * @author littletli 2013/03/05
 *  
 */

public class MultiTransformImageView extends TransformImageView {

    
	private ViewPager mViewPager; 
	
	private MultiTransformImgPositionController mPositionController;
	
	
    public MultiTransformImageView(Context context) {
        this(context, null);
    }

    public MultiTransformImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiTransformImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    
    public void init(Context context){
		mPositionController = new MultiTransformImgPositionController(context);
		setTransformEnabled(true);
    }
    
    
	public void setViewPager(ViewPager viewPager) {
		mViewPager = viewPager;
	}

	public ViewPager getViewPager() {
		return mViewPager;
	}
   

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mPositionController != null){ 
		    return mPositionController.onTouchEvent(this, event);
		}else{
			return super.onTouchEvent(event);
		}
	}
	
	
	
	/**
	 * 
	 */
	public void setOnGestureListener(MultiTransformImgPositionController.OnGestureListener onGestureListener) {
		if (mPositionController != null){
		    mPositionController.setOnGestureListener(onGestureListener);
		}

	}


	/*
	 * 
	 */
	public void setOnDoubleTapListener(MultiTransformImgPositionController.OnDoubleTapListener onDoubleTapListener) {
		if (mPositionController != null){
		    mPositionController.setOnDoubleTapListener(onDoubleTapListener);
		}
	}

	
	/**
	 * 
	 * @param onImageFlingListener
	 */
	public void setOnImageFlingListener(MultiTransformImgPositionController.OnImageFlingListener onImageFlingListener){
		if (mPositionController != null){
			mPositionController.setOnImageFlingListener(onImageFlingListener);		
		}
	}
	
	
	/**
	 * 
	 * @param isLongpressEnabled
	 */
	public void setIsLongpressEnabled(boolean isLongpressEnabled) {
		if (mPositionController != null){
			mPositionController.setIsLongpressEnabled(isLongpressEnabled);
		}		
	}
	
	
	/**
	 * 
	 * @param isDoubleTapZoomEnabled
	 */
	public void setIsDoubleTapZoomEnabled(boolean isDoubleTapZoomEnabled){
		if (mPositionController != null){
			mPositionController.setOnDoubleTapZoomEnabled(isDoubleTapZoomEnabled);
		}		
	}	
}



