package com.cineverso.api_cineverso.exceptions;

import org.springframework.http.HttpStatus;

public class TokenDecodeException  extends BaseException{
    public TokenDecodeException(HttpStatus status, String message) {
        super(status, message);
    }
}
