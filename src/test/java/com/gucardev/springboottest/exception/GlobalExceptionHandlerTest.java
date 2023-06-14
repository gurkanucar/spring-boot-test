package com.gucardev.springboottest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gucardev.springboottest.constant.Constants;
import java.util.AbstractMap.SimpleEntry;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  private RuntimeException runtimeException;
  private NotFoundException notFoundException;

  @BeforeEach
  void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
    runtimeException = new RuntimeException("Test Runtime Exception");
    notFoundException = new NotFoundException("Test Not Found Exception");
  }

  @Test
  void handleRuntimeExceptionTest() {
    ResponseEntity<SimpleEntry<String, String>> responseEntity =
        globalExceptionHandler.handleRuntimeException(runtimeException);
    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    assertEquals(
        "Test Runtime Exception", Objects.requireNonNull(responseEntity.getBody()).getValue());
    assertEquals(Constants.EXCEPTION_MESSAGE_KEY, responseEntity.getBody().getKey());
  }

  @Test
  void notFoundExceptionTest() {
    ResponseEntity<SimpleEntry<String, String>> responseEntity =
        globalExceptionHandler.notFoundException(notFoundException);
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    assertEquals(
        "Test Not Found Exception", Objects.requireNonNull(responseEntity.getBody()).getValue());
    assertEquals(Constants.EXCEPTION_MESSAGE_KEY, responseEntity.getBody().getKey());
  }
}
