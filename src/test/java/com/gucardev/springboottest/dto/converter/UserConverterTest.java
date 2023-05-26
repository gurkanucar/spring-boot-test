package com.gucardev.springboottest.dto.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserConverterTest {

  private UserConverter userConverter;

  @BeforeEach
  void setUp() {
    userConverter = new UserConverter();
  }

  @Test
  void mapToEntityTest() {
    UserRequest userRequest = new UserRequest();
    userRequest.setUsername("username");
    userRequest.setEmail("email@test.com");
    userRequest.setName("Test User");

    User user = userConverter.mapToEntity(userRequest);

    assertEquals(userRequest.getUsername(), user.getUsername());
    assertEquals(userRequest.getEmail(), user.getEmail());
    assertEquals(userRequest.getName(), user.getName());
  }

  @Test
  void mapToDTOTest() {
    User user = new User();
    user.setId(1L);
    user.setUsername("username");
    user.setEmail("email@test.com");
    user.setName("Test User");

    UserDTO userDTO = userConverter.mapToDTO(user);

    assertEquals(user.getId(), userDTO.getId());
    assertEquals(user.getUsername(), userDTO.getUsername());
    assertEquals(user.getEmail(), userDTO.getEmail());
    assertEquals(user.getName(), userDTO.getName());
  }
}
