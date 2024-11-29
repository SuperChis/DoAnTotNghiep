package org.example.ezyshop.exception;

public class NotFoundException extends IllegalArgumentException{
    private boolean success;
    private int code;

    public NotFoundException(boolean success, int code, String msg){
        super(msg);
        this.success = success;
        this.code = code;
    }
}
