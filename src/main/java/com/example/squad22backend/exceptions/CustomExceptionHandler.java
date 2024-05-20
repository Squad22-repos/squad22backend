package com.example.squad22backend.exceptions;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException exception, WebRequest request) {

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }
}