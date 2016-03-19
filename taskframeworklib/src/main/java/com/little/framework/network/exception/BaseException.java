package com.little.framework.network.exception;

/**
 * Created by little on 16/3/7.
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = -2431196726844826744L;

    private int mErrorCode;

    protected BaseException() {
        super();
    }

    protected BaseException(Throwable t) {
        super(t);
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(int errorCode, String msg){
        super(msg);
        mErrorCode = errorCode;
    }


    /**
     *
     * @return
     */
    public int getErrorCode(){
        return mErrorCode;
    }


    /**
     *
     * @param msg
     * @param t
     */
    public BaseException(String msg, Throwable t) {
        super(msg, t);
    }
}
