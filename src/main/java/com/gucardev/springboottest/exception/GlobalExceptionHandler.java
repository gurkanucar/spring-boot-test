package com.gucardev.springboottest.exception;

import java.util.AbstractMap.SimpleEntry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<SimpleEntry<String, String>> handleRuntimeException(RuntimeException ex) {
    return new ResponseEntity<>(new SimpleEntry<>("error", ex.getMessage()), HttpStatus.NOT_FOUND);
  }
}
