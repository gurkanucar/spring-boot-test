package com.gucardev.springboottest.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.gucardev.springboottest.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User user1;

  @BeforeEach
  void setUp() {
    user1 = new User();
    user1.setUsername("username1");
    user1.setEmail("email@test.com");
    user1.setName("Test User");
    entityManager.persistAndFlush(user1);
  }

  @Test
  void whenExistsByUsernameIgnoreCase_thenReturnTrue() {
    boolean exists = userRepository.existsByUsernameIgnoreCase(user1.getUsername());
    assertTrue(exists);
  }

  @Test
  void whenExistsByUsernameIgnoreCase_thenReturnFalse() {
    boolean exists = userRepository.existsByUsernameIgnoreCase("non-existing-username");
    assertFalse(exists);
  }

  @Test
  void whenExistsById_thenReturnTrue() {
    boolean exists = userRepository.existsById(user1.getId());
    assertTrue(exists);
  }

  @Test
  void whenExistsById_thenReturnFalse() {
    boolean exists = userRepository.existsById(-1L);
    assertFalse(exists);
  }
}
