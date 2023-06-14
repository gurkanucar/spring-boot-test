package com.gucardev.springboottest.exception;

import com.gucardev.springboottest.constant.Constants;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<Map<String, String>> handleMethodArgumentNotValidEx(
      MethodArgumentNotValidException ex, WebRequest request) {
    return getMapResponseEntity(ex);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public final ResponseEntity<Map<String, String>> handleConstraintViolationEx(
      MethodArgumentNotValidException ex, WebRequest request) {
    return getMapResponseEntity(ex);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<SimpleEntry<String, String>> handleRuntimeException(RuntimeException ex) {
    return new ResponseEntity<>(
        new SimpleEntry<>(Constants.EXCEPTION_MESSAGE_KEY, ex.getMessage()),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<SimpleEntry<String, String>> notFoundException(NotFoundException ex) {
    return new ResponseEntity<>(
        new SimpleEntry<>(Constants.EXCEPTION_MESSAGE_KEY, ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(RequestNotPermitted.class)
  public ResponseEntity<SimpleEntry<String, String>> handleRateLimitException(
      RequestNotPermitted e) {
    return new ResponseEntity<>(
        new SimpleEntry<>(
            Constants.EXCEPTION_MESSAGE_KEY, "Rate limit exceeded: " + e.getMessage()),
        HttpStatus.TOO_MANY_REQUESTS);
  }

  protected ResponseEntity<Map<String, String>> getMapResponseEntity(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            x -> {
              String fieldName = ((FieldError) x).getField();
              String errorMessage = x.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
