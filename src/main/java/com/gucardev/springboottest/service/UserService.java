package com.gucardev.springboottest.service;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import java.util.List;

public interface UserService {

  List<UserDTO> findAll();

  User getById(Long id);

  UserDTO getByIdDTO(Long id);

  UserDTO create(UserRequest userRequest);

  UserDTO update(UserRequest userRequest);

  void delete(Long id);
}
