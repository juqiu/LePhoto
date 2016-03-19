package com.little.framework.network.engine;

import com.little.framework.network.api.BaseApi;
import com.little.framework.network.api.RequestType;
import com.little.framework.network.exception.BaseException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by little on 16/3/7.
 */
public class HttpOkNetworkEngine implements  NetworkEngineInterface{

    static HttpOkNetworkEngine sInstance = new HttpOkNetworkEngine();

    private  OkHttpClient client;

    private HttpOkNetworkEngine(){
         client = new OkHttpClient();
    }

    public static HttpOkNetworkEngine getInstance() {
        if (sInstance == null) {
            synchronized (HttpOkNetworkEngine.class) {
                if (sInstance == null) {
                    sInstance = new HttpOkNetworkEngine();
                }
            }
        }
        return sInstance;
    }


    @Override
    public  String request(BaseApi baseApi) throws BaseException {

        String result = null;
        Request request = null;

        if(baseApi.getRequestType().equalsIgnoreCase(RequestType.GET)){
            String url = baseApi.getApiUrl();
            StringBuilder sbUrl = new StringBuilder(url);

            // Params
            if (baseApi.getParams() != null && baseApi.getParams().size() > 0) {
                if (!(url.endsWith("?") || url.endsWith("&"))) {
                    sbUrl.append("?");
                }
                Set<Map.Entry<String, String>> set = baseApi.getParams().entrySet();
                for (Map.Entry<String, String> entry : set) {
                    if (entry.getValue() != null) {
                        sbUrl.append(entry.getKey());
                        sbUrl.append("=");
                        sbUrl.append(entry.getValue());
                        sbUrl.append("&");
                    }
                }
            }
            request = new Request.Builder().url(sbUrl.toString()).build();
        }else{
            FormEncodingBuilder builder = new FormEncodingBuilder();
            // Params
            if (baseApi.getParams() != null && baseApi.getParams().size() > 0) {
                Set<Map.Entry<String, String>> set = baseApi.getParams().entrySet();
                for (Map.Entry<String, String> entry : set) {
                    if (entry.getValue() != null) {
                        builder.addEncoded(entry.getKey(), entry.getValue());
                    }
                }
            }
            request = new Request.Builder()
                    .url(baseApi.getApiUrl())
                    .post(builder.build())
                    .build();
        }

        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()){
                result = response.body().string();
            }else {
                throw new BaseException(response.code(), response.message());
            }
        } catch (IOException e) {
            throw new BaseException(e.toString());
        }
        return  result;

    }

}
