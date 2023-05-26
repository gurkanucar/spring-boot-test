package com.gucardev.springboottest.service;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface UserService {

  Page<UserDTO> findAll(
      String searchTerm, String sortField, Sort.Direction sortDirection, Pageable pageable);

  User getById(Long id);

  UserDTO getByIdDTO(Long id);

  UserDTO create(UserRequest userRequest);

  UserDTO update(UserRequest userRequest);

  void delete(Long id);

  List<UsernameLengthProjection> getUserNamesListWithLengthGreaterThan(Integer length);

  List<MailUserNameProjection> getMailAndUsernames();
}
