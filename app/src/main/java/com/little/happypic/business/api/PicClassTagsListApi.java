package com.little.happypic.business.api;

import com.little.happypic.business.pojo.PicClassTags;
import com.little.framework.network.api.CommonUrl;
import com.little.framework.network.api.JsonApi;

/**
 * Created by little on 16/3/7.
 */
public class PicClassTagsListApi extends JsonApi<PicClassTags> {

    public PicClassTagsListApi(CommonUrl commonUrl) {
        super(commonUrl);
    }

}
