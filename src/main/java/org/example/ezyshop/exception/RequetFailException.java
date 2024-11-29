package org.example.ezyshop.exception;

public class RequetFailException extends IllegalArgumentException{
    private boolean success;
    private int code;

    public RequetFailException (String msg){
        super(msg);
    }
    public RequetFailException(boolean success, int code, String msg){
        super(msg);
        this.success = success;
        this.code = code;
    }
}
