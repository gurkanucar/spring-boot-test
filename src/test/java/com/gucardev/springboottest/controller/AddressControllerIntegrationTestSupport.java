package com.gucardev.springboottest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.dto.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
abstract class AddressControllerIntegrationTestSupport {

  @Autowired protected MockMvc mockMvc;

  @Autowired protected ObjectMapper objectMapper;

  @BeforeEach
  void setupBeforeEach() throws Exception {

    for (int i = 0; i < 2; i++) {
      UserRequest userRequest =
          UserRequest.builder()
              .email("example" + (i + 1) + "@mail.com")
              .username("username" + (i + 1))
              .name("name" + (i + 1))
              .build();
      MvcResult mvcResult =
          mockMvc
              .perform(
                  MockMvcRequestBuilders.post("/api/user")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(userRequest))
                      .accept(MediaType.APPLICATION_JSON))
              .andReturn();

      String responseBody = mvcResult.getResponse().getContentAsString();
      UserDTO createdUser = objectMapper.readValue(responseBody, UserDTO.class);

      // Create addresses for the user
      for (int j = 0; j < 3; j++) {
        AddressRequest addressRequest =
            AddressRequest.builder()
                .title("Address " + (j + 1) + " for user " + (i + 1))
                .detail("Detail " + (j + 1) + " for address " + (j + 1) + " of user " + (i + 1))
                .userId(createdUser.getId())
                .build();

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest))
                .accept(MediaType.APPLICATION_JSON));
      }
    }
  }
}
