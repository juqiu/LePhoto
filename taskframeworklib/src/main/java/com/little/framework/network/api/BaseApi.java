/**
 *
 */

package com.little.framework.network.api;

import com.little.framework.network.engine.NetworkEngineInterface;
import com.little.framework.network.exception.BaseException;

import java.util.Map;


/**
 *
 * @param <T>
 */
public abstract class BaseApi<T> {


    private NetworkEngineInterface mNetworkEngine;


    /**
     *
     * @return
     */
    public abstract String getApiUrl();


    /**
     *
     * @return
     */
    public abstract Map<String, String> getParams();


    /**
     *
     * @return
     */
    public abstract String getRequestType();


    /**
     *
     * @return
     * @throws BaseException
     */
    public abstract T execute() throws BaseException;


    /**
     *
     * @param networkEngine
     */
    public void setNetworkEngine(NetworkEngineInterface networkEngine){
        mNetworkEngine = networkEngine;
    }

    /**
     *
     */
    public NetworkEngineInterface getNetworkEngine(){
        return mNetworkEngine;
    }


}
