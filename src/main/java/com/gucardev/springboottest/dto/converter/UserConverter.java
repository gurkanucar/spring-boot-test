package com.gucardev.springboottest.dto.converter;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  public User mapToEntity(UserRequest userRequest) {
    return User.builder()
        .username(Optional.ofNullable(userRequest.getUsername()).orElse(""))
        .email(Optional.ofNullable(userRequest.getEmail()).orElse(""))
        .name(Optional.ofNullable(userRequest.getName()).orElse(""))
        .build();
  }

  public UserDTO mapToDTO(User user) {
    return UserDTO.builder()
        .id(Optional.ofNullable(user.getId()).orElse(0L))
        .username(Optional.ofNullable(user.getUsername()).orElse(""))
        .email(Optional.ofNullable(user.getEmail()).orElse(""))
        .name(Optional.ofNullable(user.getName()).orElse(""))
        .build();
  }
}
