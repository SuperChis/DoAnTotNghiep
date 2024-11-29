package org.example.ezyshop.exception;

public class TokenRefreshException extends IllegalArgumentException{
    private boolean success;
    private int code;

    private String token;

    public TokenRefreshException(boolean success, int code, String msg){
        super(msg);
        this.success = success;
        this.code = code;
    }

    public TokenRefreshException(String token, String msg){
        super(msg);
        this.token = token;
    }
}
