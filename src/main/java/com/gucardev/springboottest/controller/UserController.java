package com.gucardev.springboottest.controller;

import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import com.gucardev.springboottest.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<Page<UserDTO>> searchUsers(
      @RequestParam(required = false) String searchTerm,
      @RequestParam(defaultValue = "name") String sortField,
      @RequestParam(defaultValue = "ASC") String sortDirection,
      @PageableDefault(size = 20) Pageable pageable) {
    Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
    Page<UserDTO> result = userService.findAll(searchTerm, sortField, direction, pageable);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getByIdDTO(id));
  }

  @PostMapping
  public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserRequest userRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequest));
  }

  @PutMapping
  public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserRequest userRequest) {
    return ResponseEntity.ok(userService.update(userRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable Long id) {
    userService.delete(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/mail-username")
  public ResponseEntity<List<MailUserNameProjection>> getMailAndUsernames() {
    return ResponseEntity.ok(userService.getMailAndUsernames());
  }

  @GetMapping("/username-length/{length}")
  public ResponseEntity<List<UsernameLengthProjection>> getById(@PathVariable Integer length) {
    return ResponseEntity.ok(userService.getUserNamesListWithLengthGreaterThan(length));
  }
}
