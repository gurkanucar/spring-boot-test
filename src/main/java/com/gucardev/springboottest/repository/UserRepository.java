package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {

  boolean existsById(Long id);

  boolean existsByUsernameIgnoreCase(String username);

  Page<User> findAll(Specification<User> spec, Pageable pageable);

  @Query(
      value = "select u.username as username, u.email as email from user_table u",
      nativeQuery = true)
  List<MailUserNameProjection> findAllMailAndUserName();
}
