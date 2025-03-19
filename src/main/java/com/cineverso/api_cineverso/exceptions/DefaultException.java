package com.cineverso.api_cineverso.exceptions;

import org.springframework.http.HttpStatus;

public class DefaultException extends BaseException{

    public DefaultException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
