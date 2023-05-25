package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

  boolean existsById(Long id);

  boolean existsByUsernameIgnoreCase(String username);

}
