package com.gucardev.springboottest.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.gucardev.springboottest.dto.RestPageResponse;
import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import com.gucardev.springboottest.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class IntegrationTestSupport {

  WireMockServer wireMockServer = new WireMockServer(3000);

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @Autowired protected UserRepository userRepository;

  @BeforeEach
  void setupBeforeEach() throws Exception {

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

    // wiremock setup

    wireMockServer.start();

    List<UserDTO> mockUserDTOs =
        Arrays.asList(
            new UserDTO(1L, "User1", "user1@example.com", "username1"),
            new UserDTO(2L, "User2", "user2@example.com", "username2"));

    RestPageResponse<UserDTO> pageResponse = new RestPageResponse<>(mockUserDTOs);

    String jsonResponse = objectMapper.writeValueAsString(pageResponse);

    WireMock.configureFor("localhost", wireMockServer.port());
    stubFor(
        get(urlEqualTo("/mock/user"))
            .willReturn(
                aResponse().withHeader("Content-Type", "application/json").withBody(jsonResponse)));
  }

  @AfterEach
  void tearDownAfterEach() {
    // userRepository.deleteAll();
    // wireMockServer.stop();
  }

  @AfterAll
  void tearDownAfterAll() {
    wireMockServer.stop();
  }
}
