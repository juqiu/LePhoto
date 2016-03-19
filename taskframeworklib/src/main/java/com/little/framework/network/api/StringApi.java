

package com.little.framework.network.api;

import com.little.framework.network.engine.HttpOkNetworkEngine;
import com.little.framework.network.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class StringApi extends BaseApi<String> {

    private CommonUrl mCommonUrl;
    private HashMap<String, String> mParams;

    public StringApi(CommonUrl commonUrl) {
        mCommonUrl = commonUrl;
        mParams = new HashMap<String, String>();
    }

    public void addParams(String key, String value) {
        mParams.put(key, value);
    }

    @Override
    public String getApiUrl() {
        return mCommonUrl.getApiUrl();
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    public String getRequestType() {
        return mCommonUrl.getRequestType();
    }

    @Override
    public String execute() {
        try {
            return  HttpOkNetworkEngine.getInstance().request(this);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return null;
    }
}