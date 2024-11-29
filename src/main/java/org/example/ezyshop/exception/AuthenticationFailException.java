package org.example.ezyshop.exception;

public class AuthenticationFailException extends IllegalArgumentException{
    private boolean success;
    private int code;

    public AuthenticationFailException(boolean success, int code, String msg){
        super(msg);
        this.success = success;
        this.code = code;
    }

}
