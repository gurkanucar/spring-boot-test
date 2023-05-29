package com.gucardev.springboottest.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gucardev.springboottest.dto.request.AddressRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class AddressControllerIntegrationTest extends AddressControllerIntegrationTestSupport {

  @Test
  void getAll_givenUserId_returnsAddresses() throws Exception {
    long userId = 1L;
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/api/address/user/" + userId)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  void getById_givenValidId_ShouldReturnAddressDTO() throws Exception {
    mockMvc
        .perform(get("/api/address/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)));
  }

  @Test
  void getById_givenNonExistentId_ShouldReturnError() throws Exception {
    Long nonExistingId = 100000L;
    mockMvc.perform(get("/api/address/{id}", nonExistingId)).andExpect(status().is4xxClientError());
  }

  @Test
  void createAddress_givenValidAddressRequest_ShouldReturnCreatedAddressDTO() throws Exception {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setTitle("Test title");
    addressRequest.setDetail("Test detail");
    addressRequest.setUserId(1L);

    mockMvc
        .perform(
            post("/api/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title", is("Test title")))
        .andExpect(jsonPath("$.detail", is("Test detail")));
  }

  @Test
  void createAddress_givenIncompleteAddressRequest_ShouldReturn400BadRequest() throws Exception {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setTitle("Test title");

    mockMvc
        .perform(
            post("/api/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateAddress_givenValidAddressRequest_ShouldReturnUpdatedAddressDTO() throws Exception {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setId(1L);
    addressRequest.setTitle("Updated title");
    addressRequest.setDetail("Updated detail");
    addressRequest.setUserId(1L);

    mockMvc
        .perform(
            put("/api/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title", is("Updated title")))
        .andExpect(jsonPath("$.detail", is("Updated detail")));
  }

  @Test
  void deleteAddress_givenValidId_ShouldReturnStatusOk() throws Exception {
    mockMvc.perform(delete("/api/address/{id}", 1L)).andExpect(status().isOk());
  }
}
