package com.little.framework.businessframework.business;



import com.little.framework.utils.Pack;

import java.util.Map;


/**
 *
 * Author: juqiu.lt
 * Date: 2015-1-30
 * Time: 下午3:18:33 
 *
 */
public class BusinessResult extends Pack<String> {
	
	private static final long serialVersionUID = -2313525078625494027L;
		
	public static final int RESULT_SUCCESS  = 0;
	
	public static final int RESULT_FAIL     = 1;
		
	public static final int RESULT_CANCEL   = 2;
	

    final public int id;
    
    public int mResultCode;
    
    private Object mData;

    private Exception mException;
    
    private String resultMsg;

    private Map<String, String> mRequestParams;


    /**
     *
     * @param id
     */
    public BusinessResult(int id) {
        this.id = id;
    }


    /**
     *
     * @return
     */
    public Object getData() {
        return mData;
    }

    /**
     *
     * @param mData
     */
    public void setData(Object mData) {
        this.mData = mData;
    }


    /**
     *
     * @param exception
     */
    public void setException(Exception exception){
        this.mException = exception;
    }

    /**
     *
     */
    public Exception getException(){
        return mException;
    }


    /**
     *
     * @return
     */
    public boolean isSuccessful(){
        return mResultCode == RESULT_SUCCESS;
    }



    /**
     *
     * @param resultMsg
     */
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    /**
     *
     * @return
     */
    public String getResultMsg() {
        return resultMsg;
    }


    /**
     *
     * @return
     */
    public Map<String, String> getRequestParams() {
        return mRequestParams;
    }

    /**
     *
     * @param mRequestParams
     */
    public void setRequestParams(Map<String, String> mRequestParams) {
        this.mRequestParams = mRequestParams;
    }
}
