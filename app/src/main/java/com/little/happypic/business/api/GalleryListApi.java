package com.little.happypic.business.api;

import com.little.happypic.business.pojo.GalleryList;
import com.little.framework.network.api.CommonUrl;
import com.little.framework.network.api.JsonApi;

/**
 * Created by little on 16/3/12.
 */
public class GalleryListApi extends JsonApi<GalleryList> {

    public GalleryListApi(CommonUrl commonUrl, String id, String page) {
        super(commonUrl);
        addParams("id", id);
        addParams("page", page);
    }
}
