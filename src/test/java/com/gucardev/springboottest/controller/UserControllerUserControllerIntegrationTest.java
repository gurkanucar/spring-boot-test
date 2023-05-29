package com.gucardev.springboottest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gucardev.springboottest.dto.RestPageResponse;
import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.UserRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class UserControllerUserControllerIntegrationTest extends UserControllerIntegrationTestSupport {

  @Test
  void searchUsers_givenSortFieldAndSortDirection_returnsUsers() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/user")
                    .param("sortField", "username")
                    .param("sortDirection", "ASC")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    RestPageResponse<UserDTO> userDTOS =
        objectMapper.readValue(content, new TypeReference<RestPageResponse<UserDTO>>() {});
    assertEquals(userDTOS.getContent().get(0).getUsername(), "username1");
  }

  @Test
  void getById_givenUserId_returnsUser() throws Exception {
    long userId = 1L;
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/user/" + userId)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    UserDTO userDTO = objectMapper.readValue(content, UserDTO.class);
    assertEquals(userDTO.getUsername(), "username1");
  }

  @Test
  void createUser_givenUserRequest_createsUser() throws Exception {
    UserRequest userRequest =
        UserRequest.builder()
            .id(1L)
            .username("username_new")
            .name("name_new")
            .email("email_new@mail.com")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    UserDTO userDTO = objectMapper.readValue(content, UserDTO.class);
    assertEquals(userDTO.getUsername(), userRequest.getUsername());
  }

  @Test
  void updateUser_givenUserRequest_updatesUser() throws Exception {

    UserRequest userRequest =
        UserRequest.builder()
            .id(1L)
            .username("username1_update")
            .name("name1_update")
            .email("email1_update@mail.com")
            .build();
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userRequest))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    UserDTO userDTO = objectMapper.readValue(content, UserDTO.class);
    assertNotEquals(userDTO.getUsername(), userRequest.getUsername());
    assertEquals(userDTO.getName(), userRequest.getName());
    assertEquals(userDTO.getEmail(), userRequest.getEmail());
  }

  @Test
  void deleteById_givenUserId_deletesUser() throws Exception {
    long userId = 1L;
    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/api/user/" + userId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void differentUsers_returnsMultipleUsers() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/user/different-users")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    List<UserDTO> userDTOS = objectMapper.readValue(content, new TypeReference<List<UserDTO>>() {});
    assertTrue(userDTOS.size() > 0);
  }

  @Test
  void getMailAndUsernames_returnsMailAndUsernames() throws Exception {
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/user/mail-username")
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
  }

  @Test
  void getUsernameLength_givenLength_returnsUsernamesWithLength() throws Exception {
    int length = 5;
    MvcResult mvcResult =
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/api/user/username-length/" + length)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
  }
}
