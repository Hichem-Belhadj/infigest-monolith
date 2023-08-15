package com.hbtheme.infigestback.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class BaseController {

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<String> handleMethodArgumentTypeMismatch() {
        return new ResponseEntity<>("Incorrect field type!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
