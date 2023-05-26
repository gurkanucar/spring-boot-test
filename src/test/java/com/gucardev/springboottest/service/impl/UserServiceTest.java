package com.gucardev.springboottest.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.converter.UserConverter;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceTest extends UserServiceTestSupport {

  @Mock private UserRepository userRepository;

  @Mock private UserConverter userConverter;

  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserServiceImpl(userRepository, userConverter);
    super.setupTestData();
  }

  @Test
  void findAllTest() {
    when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);
    when(userConverter.mapToDTO(user2)).thenReturn(userDto2);

    List<UserDTO> result = userService.findAll();

    assertEquals(2, result.size());
  }

  @Test
  void getByIdTest() {
    when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

    User result = userService.getById(existingUser.getId());

    assertEquals(existingUser, result);
  }

  @Test
  void getByIdNotFoundTest() {
    Long nonExistentId = 100L;
    when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> userService.getById(nonExistentId));
  }

  @Test
  void createTest() {
    when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())).thenReturn(false);
    when(userConverter.mapToEntity(userRequest)).thenReturn(user1);
    when(userRepository.save(user1)).thenReturn(user1);
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);

    UserDTO result = userService.create(userRequest);

    assertEquals(userDto1, result);
  }

  @Test
  void createWithExistingUsernameTest() {
    when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())).thenReturn(true);

    assertThrows(RuntimeException.class, () -> userService.create(userRequest));
  }

  @Test
  void updateTest() {
    when(userRepository.existsById(userRequest.getId())).thenReturn(true);
    when(userRepository.findById(userRequest.getId())).thenReturn(Optional.of(existingUser));
    when(userConverter.mapToEntity(userRequest)).thenReturn(updatedUser);
    when(userRepository.save(updatedUser)).thenReturn(updatedUser);
    when(userConverter.mapToDTO(updatedUser)).thenReturn(updatedUserDto);

    UserDTO result = userService.update(userRequest);

    assertEquals(updatedUserDto, result);
  }

  @Test
  void updateNotFoundTest() {
    Long nonExistentId = 100L;
    UserRequest nonExistentUserRequest =
        createUserRequest(
            nonExistentId, "Non-Existent User", "nonexistent@test.com", "nonExistentUser");

    when(userRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.update(nonExistentUserRequest));
  }

  @Test
  void deleteTest() {
    when(userRepository.existsById(existingUser.getId())).thenReturn(true);

    userService.delete(existingUser.getId());

    verify(userRepository).deleteById(existingUser.getId());
  }

  @Test
  void deleteNotFoundTest() {
    Long nonExistentId = 100L;
    when(userRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.delete(nonExistentId));
    verify(userRepository, never()).deleteById(nonExistentId);
  }
}
