package com.gucardev.springboottest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerUnitTest {

  @Mock private UserService userService;
  private UserController userController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    userController = new UserController(userService);
  }

  @Test
  void testSearchUsers() {
    Pageable pageable = PageRequest.of(0, 20);
    Page<UserDTO> userDTOPage = Page.empty(pageable);
    when(userService.findAll(any(), any(), any(), any())).thenReturn(userDTOPage);
    ResponseEntity<Page<UserDTO>> response =
        userController.searchUsers("searchTerm", "name", "ASC", pageable);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDTOPage, response.getBody());
  }

  @Test
  void testGetById() {
    UserDTO userDTO = new UserDTO();
    when(userService.getByIdDTO(anyLong())).thenReturn(userDTO);
    ResponseEntity<UserDTO> response = userController.getById(1L);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDTO, response.getBody());
  }

  @Test
  void createUser() {
    UserDTO userDTO = new UserDTO();
    when(userService.create(any())).thenReturn(userDTO);
    ResponseEntity<UserDTO> response = userController.createUser(any());
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(userDTO, response.getBody());
  }

  @Test
  void updateUser() {
    UserDTO userDTO = new UserDTO();
    when(userService.update(any())).thenReturn(userDTO);
    ResponseEntity<UserDTO> response = userController.updateUser(any(UserRequest.class));
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(userDTO, response.getBody());
  }
}
