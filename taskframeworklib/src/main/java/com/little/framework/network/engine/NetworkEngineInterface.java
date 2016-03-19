package com.little.framework.network.engine;

import com.little.framework.network.api.BaseApi;
import com.little.framework.network.exception.BaseException;

/**
 * Created by little on 16/3/12.
 */
public interface NetworkEngineInterface {
     String request(BaseApi baseApi) throws BaseException;
}
