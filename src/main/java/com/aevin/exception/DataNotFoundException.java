package com.aevin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity nor found")
public class DataNotFoundException extends RuntimeException {
    private static final Long serialVersionUID = 1L;

    public DataNotFoundException(String message) {
        super(message);
    } // DataNotFoundException
}
