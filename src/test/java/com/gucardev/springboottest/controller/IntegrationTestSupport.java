package com.gucardev.springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTestSupport {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected UserRepository userRepository;

  @BeforeEach
  void setUp() throws Exception {
    userRepository.deleteAll();
    for (int i = 0; i < 5; i++) {
      UserRequest userRequest =
          UserRequest.builder()
              .email("example" + (i + 1) + "@mail.com")
              .username("username" + (i + 1))
              .name("name" + (i + 1))
              .build();
      mockMvc.perform(
          MockMvcRequestBuilders.post("/api/user")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(userRequest))
              .accept(MediaType.APPLICATION_JSON));
    }
  }

  @AfterEach
  void tearDown() {
    //userRepository.deleteAll();
  }
}
