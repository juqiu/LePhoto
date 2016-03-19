package com.little.happypic.business.api;

import com.little.happypic.business.pojo.GalleryPics;
import com.little.framework.network.api.CommonUrl;
import com.little.framework.network.api.JsonApi;

/**
 * Created by little on 16/3/13.
 */
public class GalleryApi extends JsonApi<GalleryPics>{

    public GalleryApi(CommonUrl commonUrl, String id) {
        super(commonUrl);
        addParams("id", id);
    }
}
