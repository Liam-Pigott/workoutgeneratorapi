package com.lmpgttdev.workoutgeneratorapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateObjectException extends RuntimeException {

    public DuplicateObjectException(String exception) {
        super(exception);
    }
    
}
