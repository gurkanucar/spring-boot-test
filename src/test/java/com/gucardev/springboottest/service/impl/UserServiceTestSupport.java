package com.gucardev.springboottest.service.impl;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;

public class UserServiceTestSupport {

  protected User user1, user2, existingUser, updatedUser;
  protected UserDTO userDto1, userDto2, updatedUserDto;
  protected UserRequest userRequest;

  void setupTestData() {
    user1 = createUser("User1", "user1@test.com", "user1");
    user2 = createUser("User2", "user2@test.com", "username2");
    userDto1 = createUserDto(user1.getId(), user1.getName(), user1.getEmail(), user1.getUsername());
    userDto2 = createUserDto(user2.getId(), user2.getName(), user2.getEmail(), user2.getUsername());

    existingUser = createUser("Existing User", "existing@test.com", "existingUser");
    existingUser.setId(1L);
    updatedUser = createUser("Updated User", "updated@test.com", "updatedUser");
    updatedUserDto =
        createUserDto(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail(),
            updatedUser.getUsername());

    userRequest = createUserRequest(1L, "Request User", "request@test.com", "requestUser");
  }

  protected User createUser(String name, String email, String username) {
    return User.builder().name(name).email(email).username(username).build();
  }

  protected UserDTO createUserDto(Long id, String name, String email, String username) {
    return UserDTO.builder().id(id).name(name).email(email).username(username).build();
  }

  protected UserRequest createUserRequest(Long id, String name, String email, String username) {
    UserRequest userRequest = new UserRequest();
    userRequest.setId(id);
    userRequest.setName(name);
    userRequest.setEmail(email);
    userRequest.setUsername(username);
    return userRequest;
  }

  protected MailUserNameProjection getMailUsernameProjection(String mail, String username) {
    return new MailUserNameProjection() {
      @Override
      public String getUsername() {
        return mail;
      }

      @Override
      public String getEmail() {
        return username;
      }
    };
  }

  protected UsernameLengthProjection getUsernameLengthProjection(
      Long id, String username, String mail, Integer length) {
    return new UsernameLengthProjection() {
      @Override
      public String getUsername() {
        return mail;
      }

      @Override
      public Long getId() {
        return id;
      }

      @Override
      public Integer getLength() {
        return length;
      }

      @Override
      public String getEmail() {
        return username;
      }
    };
  }
}
