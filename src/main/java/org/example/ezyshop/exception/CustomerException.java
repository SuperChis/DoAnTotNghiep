package org.example.ezyshop.exception;
 
public class CustomerException extends IllegalArgumentException{

    private boolean success;
    private int code;
    public CustomerException(String message){
        super(message);
    }
    public CustomerException(boolean success, int code, String msg){
        super(msg);
        this.success = success;
        this.code = code;
    }
}
