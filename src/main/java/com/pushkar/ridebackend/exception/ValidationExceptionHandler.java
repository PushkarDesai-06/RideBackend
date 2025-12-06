package com.pushkar.ridebackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();

        map.put("error", "VALIDATION_ERROR");

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            map.put(error.getObjectName(), error.getDefaultMessage());
        });

        map.put("timestamp" , new Date().toInstant().toString());

        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
