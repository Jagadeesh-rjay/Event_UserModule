package com.example.demo.exception;

import org.springframework.validation.BindingResult;

public class BadRequestException extends RuntimeException {

    private BindingResult bindingResult;

    public BadRequestException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}