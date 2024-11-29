package org.example.ezyshop.exception;

import org.example.ezyshop.base.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = CustomerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<BaseResponse> handleCustomException(CustomerException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(false, 500, e.getMessage()));
    }

    @ExceptionHandler(value = AuthenticationFailException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final String handleAuthenticationFailException(AuthenticationFailException e){
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<BaseResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseResponse(false, 404, ex.getMessage()));
    }


    @ExceptionHandler(value = RequetFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<BaseResponse> handleRequestFailException(RequetFailException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(false, 400, e.getMessage()));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public final ResponseEntity<BaseResponse> handleRequestFailException(TokenRefreshException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseResponse(false, 403, e.getMessage()));
    }
}
