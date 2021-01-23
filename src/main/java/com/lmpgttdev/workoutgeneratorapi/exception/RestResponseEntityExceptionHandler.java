package com.lmpgttdev.workoutgeneratorapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Global response handler for escalating service level exceptions to the controller
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND);
        body.put("message", ex.getMessage());
        body.put("cause", ex.getCause());
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateObjectException.class)
    public ResponseEntity<Object> handleDuplicateObjectException(
            DuplicateObjectException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT);
        body.put("message", ex.getMessage());
        body.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

}
