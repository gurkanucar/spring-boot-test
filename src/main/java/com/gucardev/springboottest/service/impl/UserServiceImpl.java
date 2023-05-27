package com.gucardev.springboottest.service.impl;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.converter.UserConverter;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import com.gucardev.springboottest.remote.RemoteUserClient;
import com.gucardev.springboottest.repository.UserRepository;
import com.gucardev.springboottest.service.UserService;
import com.gucardev.springboottest.spesification.UserSpecifications;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserConverter userConverter;
  private final RemoteUserClient userClient;

  public UserServiceImpl(
      UserRepository userRepository, UserConverter userConverter, RemoteUserClient userClient) {
    this.userRepository = userRepository;
    this.userConverter = userConverter;
    this.userClient = userClient;
  }

  @Override
  public Page<UserDTO> getAllPageable(
      String searchTerm, String sortField, Sort.Direction sortDirection, Pageable pageable) {
    Specification<User> spec =
        Specification.where(UserSpecifications.searchByKeyword(searchTerm))
            .and(UserSpecifications.sortByField(sortField, sortDirection));
    return userRepository.findAll(spec, pageable).map(userConverter::mapToDTO);
  }

  @Override
  public User getById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("not found!"));
  }

  @Override
  public UserDTO getByIdDTO(Long id) {
    return userConverter.mapToDTO(getById(id));
  }

  @Override
  public UserDTO create(UserRequest userRequest) {
    if (userRepository.existsByUsernameIgnoreCase(userRequest.getUsername())) {
      throw new RuntimeException("user already exists!");
    }
    User saved = userRepository.save(userConverter.mapToEntity(userRequest));
    return userConverter.mapToDTO(saved);
  }

  @Override
  public UserDTO update(UserRequest userRequest) {
    if (!userRepository.existsById(userRequest.getId())) {
      throw new RuntimeException("user does not exists!");
    }
    User existing = getById(userRequest.getId());
    existing.setEmail(userRequest.getEmail());
    existing.setName(userRequest.getName());
    User saved = userRepository.save(existing);
    return userConverter.mapToDTO(saved);
  }

  @Override
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("user does not exists!");
    }
    userRepository.deleteById(id);
  }

  @Override
  public List<UsernameLengthProjection> getUserNamesListWithLengthGreaterThan(Integer length) {
    return userRepository.getUserNamesListWithLengthGreaterThan(length);
  }

  @Override
  public List<MailUserNameProjection> getMailAndUsernames() {
    return userRepository.findAllMailAndUserName();
  }

  @Override
  public List<UserDTO> getDifferentUsers() {
    List<UserDTO> remoteUsers = userClient.getUsers().getContent();
    List<User> differentUsers =
        userRepository.findUsersNotInUsernameList(getUsername.apply(remoteUsers));
    return differentUsers.stream().map(userConverter::mapToDTO).collect(Collectors.toList());
  }

  private final Function<List<UserDTO>, List<String>> getUsername =
      value -> value.stream().map(UserDTO::getUsername).collect(Collectors.toList());
}
