package com.little.happypic;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.little.framework.businessframework.thread.PriorityThreadPoolFactory;
import com.little.framework.businessframework.thread.ThreadPool;
import com.little.framework.utils.FileUtils;
import com.little.happypic.business.BusinessId;
import com.little.happypic.business.businesslayer.PicListBusinessLayer;
import com.little.happypic.business.pojo.GalleryPics;
import com.little.happypic.widget.FrescoMultiTransformImageView;
import com.little.framework.widget.picflow.MultiTransformImageView;
import com.little.framework.widget.picflow.MultiTransformImgPositionController;
import com.little.framework.widget.picflow.ViewPager;
import com.little.framework.app.BaseBusinessActivity;
import com.little.framework.businessframework.business.BusinessResult;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by little on 16/3/12.
 */
public class PicListActivity extends BaseBusinessActivity{

    private CoordinatorLayout mRoot;

    private ViewPager mViewPager;

    private PicViewImagesAdapter mAdapter;

    private TextView mInfoText;

    private GalleryPics pics;

    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_list_layout);
        mRoot = (CoordinatorLayout)findViewById(R.id.root);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mInfoText = (TextView)findViewById(R.id.info);
        id = getIntent().getIntExtra("id", 0);
        initData();
    }


    /**
     *
     */
    private void initData(){
        ArrayList<GalleryPics.Picture> list = new ArrayList<>();

        GalleryPics.Picture pic = new GalleryPics.Picture();
        pic.src = getIntent().getStringExtra("url");
        list.add(pic);

        mAdapter = new PicViewImagesAdapter(this, list);
        mViewPager.setAdapter(mAdapter);

        PicListBusinessLayer.getInstance().getPicList(this, id);
    }

    @Override
    protected void onBusinessResultImpl(BusinessResult result) {
        super.onBusinessResultImpl(result);
        switch (result.id){
            case BusinessId.Pic.ID_PIC_LIST:
                handleGetPicList(result);
                break;

        }
    }


    /**
     *
     * @param result
     */
    private void handleGetPicList(BusinessResult result){
        if (result.isSuccessful()){
            pics = (GalleryPics) result.getData();
            if (pics != null){
                mAdapter = new PicViewImagesAdapter(this, pics.list);
                mViewPager.setAdapter(mAdapter);
                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        mInfoText.setText("" + (position + 1) + "/" + mAdapter.getCount());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mInfoText.setText(""+1+"/"+mAdapter.getCount());
            }
        }
    }


    /**
     *
     */
    protected class PicViewImagesAdapter extends ViewPager.PagerAdapter {

        PicListActivity mContext;
        ArrayList<GalleryPics.Picture> mList;

        public PicViewImagesAdapter(PicListActivity c, ArrayList<GalleryPics.Picture> list) {
            super();
            mContext = c;
            mList = list;
        }


        /**
         *
         * @param index
         * @return
         */
        public GalleryPics.Picture getAt(int index){
            if (mList != null){
                return mList.get(index);
            }
            return null;
        }


        @Override
        public int getCount() {
            if (mList != null){
                return mList.size();
            }
            return 0;
        }

        @Override
        public void startUpdate(ViewGroup container) {

        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrescoMultiTransformImageView imageView = new FrescoMultiTransformImageView(mContext);
            imageView.setViewPager(mViewPager);
            imageView.setIsDoubleTapZoomEnabled(true);
            imageView.setIsLongpressEnabled(true);

            // single tap finish
            imageView.setOnDoubleTapListener(new MultiTransformImgPositionController.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    mContext.finish();
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });

            // long press to pop save
            imageView.setOnGestureListener(new MultiTransformImgPositionController.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onLongPress(MotionEvent e) {
                    Snackbar.make(mRoot, "保存到本地", Snackbar.LENGTH_LONG)
                            .setAction("保存", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mContext.doSavePic();
                                }
                            }).show();
                    return true;
                }
            });

            GalleryPics.Picture pic = mList.get(position);

            imageView.setImageUri("http://tnfs.tngou.net/img"+ pic.src);

            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((MultiTransformImageView) object);
        }

        @Override
        public void finishUpdate(ViewGroup container) {

        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }


    /**
     *
     */
    public void doSavePic(){
      final GalleryPics.Picture pic =  mAdapter.getAt(mViewPager.getCurrentItem());
       if (pic != null) {
           PriorityThreadPoolFactory.getDefaultThreadPool().submit(new ThreadPool.Job<Object>() {
               @Override
               public Object run(ThreadPool.JobContext jc) {
                   File file = getCachedImageOnDisk(Uri.parse("http://tnfs.tngou.net/img"+ pic.src));
                   if (file != null){
                       File newFile = new File(Environment.getExternalStorageDirectory() + "/happypic/"+file.getName()+".jpg");
                       if (FileUtils.copyFiles(file,newFile)){
                           Uri localUri = Uri.fromFile(newFile);
                           Intent localIntent = new Intent(
                                   Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
                           PicListActivity.this.sendBroadcast(localIntent);
                       }
                   }
                   return  null;
               }
           });

       }
    }



    //return file or null
    public static File getCachedImageOnDisk(Uri loadUri) {
        File localFile = null;
        if (loadUri != null) {
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(loadUri));
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }

}
