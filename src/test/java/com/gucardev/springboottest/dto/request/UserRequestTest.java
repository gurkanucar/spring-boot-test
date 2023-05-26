package com.gucardev.springboottest.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.platform.commons.util.StringUtils;

class UserRequestTest {

  private final Validator validator;

  UserRequestTest() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @ParameterizedTest
  @CsvSource({
    "1, testUsername, test@mail.com, testName, false",
    "2, ab, test@mail.com, testName, true",
    "3, testUsername, invalidEmail, testName, true",
    "4, testUsername, test@mail.com, '', true",
    "'', testUsername, test@mail.com, testName, false",
    "'',testUsernameToooooooooooooooooooooooooooooLong, test@mail.com, testName, true",
    "'', '', test@mail.com, testName, true",
    "'', testUsername, '', testName, true",
    "'', testUsername, test@mail.com, ' ', true"
  })
  void testUserRequest(
      String idInput, String username, String email, String name, boolean hasViolations) {
    Long id = StringUtils.isBlank(idInput) ? 1L : Long.parseLong(idInput);

    UserRequest userRequest =
        UserRequest.builder().id(id).username(username).email(email).name(name).build();

    Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
    assertEquals(hasViolations, !violations.isEmpty());
  }
}
