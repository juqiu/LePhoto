
package com.little.framework.network.api;

import com.little.framework.network.engine.HttpOkNetworkEngine;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.little.framework.network.engine.NetworkEngineInterface;
import com.little.framework.network.exception.BaseException;

/**
 *
 * @param <T>
 */
public class JsonApi<T> extends BaseApi<T> {

    private CommonUrl mCommonUrl;

    private HashMap<String, String> mParams;


    public JsonApi(CommonUrl commonUrl) {
        mCommonUrl = commonUrl;
        mParams = new HashMap<String, String>();
    }

    /**
     *
     * @param key
     * @param value
     */
    public void addParams(String key, String value) {
        mParams.put(key, value);
    }

    /**
     *
     * @return
     */
    @Override
    public String getApiUrl() {
        return mCommonUrl.getApiUrl();
    }


    /**
     *
     * @return
     */
    @Override
    public Map<String, String> getParams() {
        return mParams;
    }


    /**
     *
     * @return
     */
    @Override
    public String getRequestType() {
        return mCommonUrl.getRequestType();
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T execute() throws BaseException{

        T response = null;
        String responseString = null;

        NetworkEngineInterface networkEngine = getNetworkEngine();
        if (networkEngine == null){
            networkEngine = HttpOkNetworkEngine.getInstance();
        }
        responseString = networkEngine.request(this);

        if (responseString != null){
           response =  parseToEntity(responseString);
        }
        return response;
    }


    /**
     *
     * Default Use Json To
     * @return
     */
    public T parseToEntity(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, getEntityClass());
    }

}
