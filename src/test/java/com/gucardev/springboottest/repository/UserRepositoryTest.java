package com.gucardev.springboottest.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.model.projection.MailUserNameProjection;
import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import com.gucardev.springboottest.spesification.UserSpecifications;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private UserRepository userRepository;

  private User user1, user2, user3, user4;

  @BeforeEach
  public void setup() {
    user1 = new User();
    user1.setUsername("user1");
    user1.setName("name1");
    user1.setEmail("email1@test.com");

    user2 = new User();
    user2.setUsername("username2");
    user2.setName("name2");
    user2.setEmail("email2@test.com");

    user3 = new User();
    user3.setUsername("username3");
    user3.setName("name3");
    user3.setEmail("email3@test.com");

    user4 = new User();
    user4.setUsername("username4");
    user4.setName("name4");
    user4.setEmail("email4@test.com");

    entityManager.persist(user1);
    entityManager.persist(user2);
    entityManager.persist(user3);
    entityManager.persist(user4);
    entityManager.flush();
  }

  @Test
  void searchByKeyword_givenKeyword_returnMatchingUsers() {
    Specification<User> spec = UserSpecifications.searchByKeyword("user1");
    Pageable pageable = PageRequest.of(0, 10);

    Page<User> users = userRepository.findAll(spec, pageable);

    assertEquals(1, users.getContent().size());
    assertEquals("user1", users.getContent().get(0).getUsername());
  }

  @ParameterizedTest
  @CsvSource({"asc,0,3", "desc, 3,0"})
  void sortByField_givenSortDirectionAndField_returnSortedUsers(String sortDir, int order1, int order2) {
    Sort.Direction direction = Sort.Direction.fromString(sortDir.toUpperCase());

    Specification<User> spec = UserSpecifications.sortByField("username", direction);
    Pageable pageable = PageRequest.of(0, 10);

    Page<User> users = userRepository.findAll(spec, pageable);

    assertEquals(4, users.getContent().size());
    assertEquals("user1", users.getContent().get(order1).getUsername());
    assertEquals("username4", users.getContent().get(order2).getUsername());
  }

  @Test
  void existsByUsernameIgnoreCase_givenExistingUsername_returnTrue() {
    boolean exists = userRepository.existsByUsernameIgnoreCase(user1.getUsername());
    assertTrue(exists);
  }

  @Test
  void existsByUsernameIgnoreCase_givenNonExistingUsername_returnFalse() {
    boolean exists = userRepository.existsByUsernameIgnoreCase("non-existing-username");
    assertFalse(exists);
  }

  @Test
  void existsById_givenExistingId_returnTrue() {
    boolean exists = userRepository.existsById(user1.getId());
    assertTrue(exists);
  }

  @Test
  void existsById_givenNonExistingId_returnFalse() {
    boolean exists = userRepository.existsById(-1L);
    assertFalse(exists);
  }

  @Test
  void getUserNamesListWithLengthGreaterThan_givenLength_returnUsernamesWithLengthGreaterThan() {
    List<UsernameLengthProjection> users = userRepository.getUserNamesListWithLengthGreaterThan(8);

    assertEquals(3, users.size());
    assertEquals("username2", users.get(0).getUsername());
    assertEquals("username3", users.get(1).getUsername());
    assertEquals("username4", users.get(2).getUsername());
  }

  @Test
  void findAllMailAndUserName_givenNoCondition_returnMailAndUsernames() {
    List<MailUserNameProjection> users = userRepository.findAllMailAndUserName();

    assertEquals(4, users.size());

    assertEquals("email1@test.com", users.get(0).getEmail());
    assertEquals("user1", users.get(0).getUsername());

    assertEquals("email2@test.com", users.get(1).getEmail());
    assertEquals("username2", users.get(1).getUsername());

    assertEquals("email3@test.com", users.get(2).getEmail());
    assertEquals("username3", users.get(2).getUsername());

    assertEquals("email4@test.com", users.get(3).getEmail());
    assertEquals("username4", users.get(3).getUsername());
  }
}
