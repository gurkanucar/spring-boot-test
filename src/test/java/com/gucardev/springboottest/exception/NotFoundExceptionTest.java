package com.gucardev.springboottest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

  @Test
  void testNotFoundException() {
    String expectedMessage = "message";
    NotFoundException ex = new NotFoundException(expectedMessage);
    assertEquals(expectedMessage, ex.getMessage());
  }
}
