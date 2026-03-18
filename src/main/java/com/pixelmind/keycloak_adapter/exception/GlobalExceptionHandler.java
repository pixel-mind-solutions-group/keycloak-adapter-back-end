package com.pixelmind.keycloak_adapter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<Object> handleBaseException(BaseException ex) {
        log.error("BaseException: {}", ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Base exception");
        details.add(ex.getMessage());
        ApiErrors errors = new ApiErrors(ex.getMessage(), details, HttpStatus.resolve(ex.getErrorCode()), LocalDateTime.now());
        return ResponseEntity.status(ex.getErrorCode()).body(errors);
    }
}
