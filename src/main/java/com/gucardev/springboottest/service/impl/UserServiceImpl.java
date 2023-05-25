package com.gucardev.springboottest.service.impl;

import static com.gucardev.springboottest.dto.converter.UserConverter.mapToDTO;
import static com.gucardev.springboottest.dto.converter.UserConverter.mapToEntity;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.converter.UserConverter;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.repository.UserRepository;
import com.gucardev.springboottest.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /* TODO add pagination */
  @Override
  public List<UserDTO> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream().map(UserConverter::mapToDTO).collect(Collectors.toList());
  }

  @Override
  public User getById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("not found!"));
  }

  @Override
  public UserDTO getByIdDTO(Long id) {
    return mapToDTO(getById(id));
  }

  @Override
  public UserDTO create(UserRequest userRequest) {
    if (userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())) {
      throw new RuntimeException("user already exists!");
    }
    User saved = userRepository.save(mapToEntity(userRequest));
    return mapToDTO(saved);
  }

  @Override
  public UserDTO update(UserRequest userRequest) {
    if (!userRepository.existsById(userRequest.getId())) {
      throw new RuntimeException("user does not exists!");
    }
    User existing = getById(userRequest.getId());
    existing.setEmail(userRequest.getEmail());
    existing.setName(userRequest.getName());
    User saved = userRepository.save(mapToEntity(userRequest));
    return mapToDTO(saved);
  }

  @Override
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("user does not exists!");
    }
    userRepository.deleteById(id);
  }
}
