package com.little.happypic;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.little.happypic.business.BusinessId;
import com.little.happypic.business.businesslayer.PicListBusinessLayer;
import com.little.happypic.business.pojo.GalleryList;
import com.little.framework.app.BaseBusinessFragment;
import com.little.framework.businessframework.business.BusinessResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.image.QualityInfo;


/**
 * Created by little on 16/3/12.
 */
public class PicClassFragment  extends BaseBusinessFragment{

    private int mClassId = 0;

    private RecyclerView mRecyclerView;

    private ArrayList<GalleryList.Gallery> mGalleryList = new ArrayList<>();

    private RecyclerViewAdapter mAdapter;

    private int mPageIndex = 1;

    private boolean mFinished = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(
                R.layout.picclass_list_layout, container, false);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return mRecyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mClassId = getArguments().getInt("id");
        initData();
    }


    /**
     *
     */
    private void initData(){
        PicListBusinessLayer.getInstance().getGalleryList(this, mClassId, mPageIndex);
    }


    public void doDataLoad(){
        if (!mFinished){
            PicListBusinessLayer.getInstance().getGalleryList(this, mClassId, mPageIndex);
        }
    }



    @Override
    protected void onBusinessResultImpl(BusinessResult result) {
        super.onBusinessResultImpl(result);
        switch (result.id){
            case BusinessId.Pic.ID_PIC_CLASS_LIST:
                handleGetPicClassList(result);
                break;
        }
    }


    /**
     *
     * @param result
     */
    private void handleGetPicClassList(BusinessResult result){
        if(result.isSuccessful()){
            GalleryList list = (GalleryList)result.getData();
            if (list != null){
                mGalleryList.addAll(list.tngou);
                mAdapter.setGalleries(mGalleryList);
                mAdapter.notifyItemRangeInserted(mGalleryList.size(), list.tngou.size());
                if (list.total == mGalleryList.size()){
                    mFinished = true;
                }else{
                    mPageIndex++;
                }
            }
        }else{
            Exception exception = result.getException();
            if (exception != null){
                Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView = null;
        mAdapter = null;
    }


    /**
     *
     */
    public static class RecyclerViewAdapter
            extends RecyclerView.Adapter<ViewHolder> {

        private Map<String,Integer> heightMap = new HashMap<>();

        private Map<String,Integer> widthMap = new HashMap<>();

        private ArrayList<GalleryList.Gallery> mGalleries;

        private View.OnClickListener mClickListener;

        private PicClassFragment mFragment;


        public RecyclerViewAdapter(PicClassFragment fragment) {
            mFragment = fragment;
            mClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer pos = (Integer)v.getTag();
                    if (pos != null){
                        GalleryList.Gallery gallery = mGalleries.get(pos);
                        Intent intent = new Intent(v.getContext(), PicListActivity.class);
                        intent.putExtra("id", gallery.id);
                        intent.putExtra("url", gallery.img);
                        v.getContext().startActivity(intent);
                    }
                }
            };
        }


        /**
         *
         * @param list
         */
        public void setGalleries(ArrayList<GalleryList.Gallery> list){
            mGalleries = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.pic_class_layout_item, parent, false);

            return new ViewHolder(view, mClickListener);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            GalleryList.Gallery gallery = mGalleries.get(position);

            final String url = gallery.img;

            holder.mTextView.setText(gallery.title);
            holder.mView.setTag(position);

            if (heightMap.containsKey(url)){
                int height = heightMap.get(url);
                if (height>0){
                    updateItemHeight(height, holder.itemView);
                    holder.mImageView.setImageURI(Uri.parse("http://tnfs.tngou.net/img"+url));
                    return;
                }
            }

            //
            ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                    if (imageInfo == null) {
                        return;
                    }
                    QualityInfo qualityInfo = imageInfo.getQualityInfo();
                    if (qualityInfo.isOfGoodEnoughQuality()){
                        int heightTarget = (int) getTargetHeight(imageInfo.getWidth(),
                                imageInfo.getHeight(),holder.itemView, url);

                        if (heightTarget <=0 )return;
                        heightMap.put(url,heightTarget);
                        updateItemHeight(heightTarget, holder.itemView);
                    }
                }

                @Override
                public void onIntermediateImageSet(String id,  ImageInfo imageInfo) {

                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                }
            };


            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse("http://tnfs.tngou.net/img"+url))
                    .setControllerListener(controllerListener)
                    .setTapToRetryEnabled(true)
                    .build();
            holder.mImageView.setController(controller);

            if (getItemCount() >= 5 && (getItemCount() - position <= 5)) {
                mFragment.doDataLoad();
            }
        }

        /**
         *
         * @param width
         * @param height
         * @param view
         * @param url
         * @return
         */
        private float getTargetHeight(float width,float height,View view, String url){
            View child = view.findViewById(R.id.image);
            float widthTarget;
            if (widthMap.containsKey(url)) {
                widthTarget = widthMap.get(url);
            } else {
                widthTarget = child.getMeasuredWidth();
                if (widthTarget > 0){
                    widthMap.put(url, (int) widthTarget);
                }
            }
            return height * (widthTarget /width);
        }


        /**
         *
         * @param height
         * @param view
         */
        private void updateItemHeight(int height, View view) {
            View child = view.findViewById(R.id.image);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            layoutParams.height = height;
            child.setLayoutParams(layoutParams);
        }

        @Override
        public int getItemCount() {
            if (mGalleries != null){
                return mGalleries.size();
            }
            return 0;
        }
    }


    /**
     *
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view, View.OnClickListener clickListener) {
            super(view);
            mView = view;
            mView.setOnClickListener(clickListener);
            mImageView = (SimpleDraweeView) view.findViewById(R.id.image);
            mTextView = (TextView) view.findViewById(R.id.text);
        }
    }
}
