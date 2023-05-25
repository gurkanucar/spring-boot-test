package com.gucardev.springboottest.dto.converter;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;

public class UserConverter {

  public static User mapToEntity(UserRequest userRequest) {
    return User.builder()
        .username(userRequest.getUsername())
        .email(userRequest.getEmail())
        .name(userRequest.getName())
        .build();
  }

  public static UserDTO mapToDTO(User user) {
    return UserDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .name(user.getName())
        .build();
  }
}
