package com.gucardev.springboottest.service.impl;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.UserDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.model.User;

public abstract class AddressServiceTestSupport {

  protected User user1, user2;
  protected UserDTO userDto1;

  protected Address address1, address2, address3, existingAddress, updatedAddress;
  protected AddressDTO addressDto1, addressDto2, addressDto3, updatedAddressDto;
  protected AddressRequest addressRequest;

  void setupTestData() {
    user1 = createUser(1L, "User1", "user1@test.com", "user1");
    user2 = createUser(2L, "User2", "user2@test.com", "user2");
    userDto1 = createUserDto(user1.getId(), user1.getName(), user1.getEmail(), user1.getUsername());

    address1 = createAddress(1L, "address title 1", "address detail 1", user1);
    address2 = createAddress(2L, "address title 2", "address detail 2", user1);
    address3 = createAddress(3L, "address title 3", "address detail 3", user2);

    updatedAddress =
        createAddress(
            1L,
            address1.getTitle() + "_updated",
            address1.getDetail() + "_updated",
            address1.getUser());

    updatedAddressDto =
        createAddressDTO(
            1L, updatedAddress.getTitle(), updatedAddress.getDetail(), updatedAddress.getUser());

    addressDto1 =
        createAddressDTO(1L, address1.getTitle(), address1.getDetail(), address1.getUser());
    addressDto2 =
        createAddressDTO(2L, address2.getTitle(), address2.getDetail(), address2.getUser());
    addressDto3 =
        createAddressDTO(3L, address3.getTitle(), address3.getDetail(), address3.getUser());

    addressRequest =
        AddressRequest.builder()
            .title(address1.getTitle())
            .detail(address1.getDetail())
            .userId(address1.getUser().getId())
            .build();
  }

  protected AddressDTO createAddressDTO(Long id, String title, String detail, User user) {
    return AddressDTO.builder().id(id).title(title).detail(detail).userId(user.getId()).build();
  }

  protected Address createAddress(Long id, String title, String detail, User user) {
    return Address.builder().id(id).title(title).detail(detail).user(user).build();
  }

  protected User createUser(Long id, String name, String email, String username) {
    return User.builder().id(id).name(name).email(email).username(username).build();
  }

  protected UserDTO createUserDto(Long id, String name, String email, String username) {
    return UserDTO.builder().id(id).name(name).email(email).username(username).build();
  }
}
