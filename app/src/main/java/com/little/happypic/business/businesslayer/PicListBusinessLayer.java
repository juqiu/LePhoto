package com.little.happypic.business.businesslayer;

import com.little.happypic.business.BusinessId;
import com.little.happypic.business.api.GalleryApi;
import com.little.happypic.business.api.GalleryListApi;
import com.little.happypic.business.api.PicClassTagsListApi;
import com.little.framework.businessframework.business.AbstractBusinessLayer;
import com.little.framework.businessframework.business.ApiTask;
import com.little.framework.businessframework.business.BusinessCallback;
import com.little.framework.businessframework.business.BusinessResponse;
import com.little.framework.businessframework.task.Task;
import com.little.framework.network.api.CommonUrl;
import com.little.framework.network.api.RequestType;

/**
 * Created by little on 16/3/8.
 */
public class PicListBusinessLayer  extends AbstractBusinessLayer{


    private  static PicListBusinessLayer instance = new PicListBusinessLayer();


    /**
     *
     * @return
     */
    public static  PicListBusinessLayer getInstance(){
        return instance;
    }


    @Override
    public void onTaskDone(Task<BusinessResponse> task) {
        super.onTaskDone(task);
    }

    /**
     *
     * @param callback
     */
    public void getPicClass(BusinessCallback callback){

        PicClassTagsListApi picListApi = new PicClassTagsListApi(
                new CommonUrl("http://www.tngou.net/tnfs/api/classify", RequestType.GET));

        new ApiTask(BusinessId.Pic.ID_PIC_CLASSES, picListApi, callback).execute(this);
    }


    /**
     *
     */
    public void getGalleryList(BusinessCallback callback, int id, int page){

        GalleryListApi galleryListApi = new GalleryListApi(
                new CommonUrl("http://www.tngou.net/tnfs/api/list", RequestType.GET),
                String.valueOf(id), String.valueOf(page));

        new ApiTask(BusinessId.Pic.ID_PIC_CLASS_LIST, galleryListApi, callback).execute(this);
    }


    /**
     *
     */
    public void getPicList(BusinessCallback callback, int id){

        GalleryApi galleryListApi = new GalleryApi(
                new CommonUrl("http://www.tngou.net/tnfs/api/show", RequestType.POST),
                String.valueOf(id));

        new ApiTask(BusinessId.Pic.ID_PIC_LIST, galleryListApi, callback).execute(this);
    }


}
