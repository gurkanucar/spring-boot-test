package com.gucardev.springboottest.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gucardev.springboottest.dto.RestPageResponse;
import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.converter.UserConverter;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import com.gucardev.springboottest.remote.RemoteUserClient;
import com.gucardev.springboottest.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends UserServiceTestSupport {

  @Mock private UserRepository userRepository;

  @Mock private UserConverter userConverter;

  @Mock private RemoteUserClient userClient;

  // @InjectMocks
  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    // MockitoAnnotations.openMocks(this); // or use class annotation
    userService = new UserServiceImpl(userRepository, userConverter, userClient);
    // userService = Mockito.spy(new UserServiceImpl(userRepository, userConverter, userClient));
    super.setupTestData();
  }

  @Test
  @DisplayName("getAllPageable returns all users with pagination")
  void getAllPageable_givenPageable_returnUsers() {
    Page<User> usersPage = new PageImpl<>(Arrays.asList(user1, user2));

    when(userRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(usersPage);
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);
    when(userConverter.mapToDTO(user2)).thenReturn(userDto2);

    Pageable pageable = PageRequest.of(0, 5);
    Page<UserDTO> result = userService.getAllPageable("", "name", Sort.Direction.ASC, pageable);

    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }

  @Test
  void getById_givenExistingId_returnUser() {
    when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

    User result = userService.getById(existingUser.getId());

    assertEquals(existingUser, result);
  }

  @Test
  void getById_givenNonExistentId_throwException() {
    Long nonExistentId = 100L;
    when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> userService.getById(nonExistentId));
  }

  @Test
  void getByIdDTO_givenExistingId_returnUserDTO() {
    // doReturn(existingUser).when(userService).getById(any()); // if you use spy, you can comment
    // code below and uncomment this. Because getById is a another method inside same test class
    when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

    when(userConverter.mapToDTO(existingUser)).thenReturn(userDto1);

    UserDTO result = userService.getByIdDTO(existingUser.getId());

    assertEquals(userDto1, result);
  }

  @Test
  void create_givenNewUser_returnCreatedUser() {
    when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())).thenReturn(false);
    when(userConverter.mapToEntity(userRequest)).thenReturn(user1);
    when(userRepository.save(user1)).thenReturn(user1);
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);

    UserDTO result = userService.create(userRequest);

    assertEquals(userDto1, result);
  }

  @Test
  void create_givenExistingUsername_throwException() {
    when(userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())).thenReturn(true);

    assertThrows(RuntimeException.class, () -> userService.create(userRequest));
  }

  @Test
  void update_givenExistingUser_returnUpdatedUser() {
    UserRequest userRequest =
        UserRequest.builder()
            .id(user1.getId())
            .username("username_will_not_update")
            .name("Updated User")
            .email("updated@test.com")
            .build();
    User updatedUser = new User();
    updatedUser.setEmail(userRequest.getEmail());
    updatedUser.setName(userRequest.getName());
    UserDTO updatedUserDto = new UserDTO();
    BeanUtils.copyProperties(updatedUser, updatedUserDto);

    when(userRepository.existsById(any())).thenReturn(true);
    when(userRepository.findById(any())).thenReturn(Optional.of(user1));
    when(userRepository.save(any())).thenReturn(updatedUser);
    when(userConverter.mapToDTO(updatedUser)).thenReturn(updatedUserDto);

    UserDTO actual = userService.update(userRequest);
    assertEquals(updatedUserDto, actual);
  }

  @Test
  void update_givenNonExistentUser_throwException() {
    Long nonExistentId = 100L;
    UserRequest nonExistentUserRequest =
        createUserRequest(
            nonExistentId, "Non-Existent User", "nonexistent@test.com", "nonExistentUser");

    when(userRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.update(nonExistentUserRequest));
  }

  @Test
  void delete_givenExistingUser_removeUser() {
    when(userRepository.existsById(existingUser.getId())).thenReturn(true);

    userService.delete(existingUser.getId());

    verify(userRepository).deleteById(existingUser.getId());
  }

  @Test
  void delete_givenNonExistentUser_throwException() {
    Long nonExistentId = 100L;
    when(userRepository.existsById(nonExistentId)).thenReturn(false);

    assertThrows(RuntimeException.class, () -> userService.delete(nonExistentId));
    verify(userRepository, never()).deleteById(nonExistentId);
  }

  @Test
  void getUserNamesListWithLengthGreaterThan_givenLength_returnUsernames() {
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
  void getMailAndUsernames_givenNoCondition_returnMailAndUsernames() {
    List<MailUserNameProjection> expectedList =
        Arrays.asList(
            getMailUsernameProjection(user1.getEmail(), user1.getUsername()),
            getMailUsernameProjection(user2.getEmail(), user2.getUsername()));
    when(userRepository.findAllMailAndUserName()).thenReturn(expectedList);

    List<MailUserNameProjection> result = userService.getMailAndUsernames();

    assertEquals(expectedList, result);
  }

  @Test
  void getDifferentUsers_givenUsernamesList_returnDifferentUsers() {
    RestPageResponse<UserDTO> userDTOPage =
        new RestPageResponse<>(Arrays.asList(userDto1, userDto2));
    when(userClient.getUsers()).thenReturn(userDTOPage);
    when(userRepository.findUsersNotInUsernameList(any()))
        .thenReturn(Collections.singletonList(user3));

    when(userConverter.mapToDTO(user3)).thenReturn(userDto3);

    List<UserDTO> differentUsers = userService.getDifferentUsers();
    assertEquals(1, differentUsers.size());
    assertEquals(userDto3, differentUsers.get(0));
    verify(userConverter, times(differentUsers.size())).mapToDTO(any(User.class));
  }
}
