package com.gucardev.springboottest.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void testUserGetterSetter() {
    User user = new User();
    Address address = new Address();

    List<Address> addresses = Collections.singletonList(address);

    user.setUsername("testUsername");
    user.setEmail("testEmail");
    user.setName("testName");
    user.setAddresses(addresses);

    assertEquals("testUsername", user.getUsername());
    assertEquals("testEmail", user.getEmail());
    assertEquals("testName", user.getName());
    assertEquals(addresses, user.getAddresses());
  }
}
