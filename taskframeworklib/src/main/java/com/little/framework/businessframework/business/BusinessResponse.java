package com.little.framework.businessframework.business;



/**
 * 
 * Author juqiu.lt
 * Date: 13-12-31
 * Time: 上午10:00
 *
 */
public class BusinessResponse {


    private Object  mResponse;
    
    protected Exception mException;
   
    
    public BusinessResponse(){

    }
    
    
    /**
     * 
     * @param exception
     */   
    public void setException(Exception exception){
    	mException = exception;
    }
    
    /**
     * 
     * @return
     */
    public Exception getException(){
    	return mException;
    }
  
     
    /**
     * 
     * @param response
     */
    public void setResponse(Object response){
    	mResponse = response;
    }
    
    /**
     * 
     * @return
     */
    public Object getResponse(){
        return mResponse;
    }

}
