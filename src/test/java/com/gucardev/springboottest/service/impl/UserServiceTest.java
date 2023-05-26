package com.gucardev.springboottest.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.converter.UserConverter;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import com.gucardev.springboottest.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

class UserServiceTest extends UserServiceTestSupport {

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
    Page<User> usersPage = new PageImpl<>(Arrays.asList(user1, user2));

    when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(usersPage);
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);
    when(userConverter.mapToDTO(user2)).thenReturn(userDto2);

    Pageable pageable = PageRequest.of(0, 5);
    Page<UserDTO> result = userService.findAll("", "name", Sort.Direction.ASC, pageable);

    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
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

  @Test
  void getByIdDTOTest() {
    when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
    when(userConverter.mapToDTO(existingUser)).thenReturn(userDto1);

    UserDTO result = userService.getByIdDTO(existingUser.getId());

    assertEquals(userDto1, result);
  }

  @Test
  void getUserNamesListWithLengthGreaterThanTest() {
    int length = 8;
    List<UsernameLengthProjection> expectedList =
        Arrays.asList(
            getUsernameLengthProjection(
                user1.getId(), user1.getUsername(), user1.getEmail(), user1.getUsername().length()),
            getUsernameLengthProjection(
                user2.getId(),
                user2.getUsername(),
                user2.getEmail(),
                user2.getUsername().length()));

    when(userRepository.getUserNamesListWithLengthGreaterThan(length)).thenReturn(expectedList);

    List<UsernameLengthProjection> result =
        userService.getUserNamesListWithLengthGreaterThan(length);

    assertEquals(expectedList, result);
  }

  @Test
  void getMailAndUsernamesTest() {
    List<MailUserNameProjection> expectedList =
        Arrays.asList(
            getMailUsernameProjection(user1.getEmail(), user1.getUsername()),
            getMailUsernameProjection(user2.getEmail(), user2.getUsername()));
    when(userRepository.findAllMailAndUserName()).thenReturn(expectedList);

    List<MailUserNameProjection> result = userService.getMailAndUsernames();

    assertEquals(expectedList, result);
  }
}
