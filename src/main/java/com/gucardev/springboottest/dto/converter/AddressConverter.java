package com.gucardev.springboottest.dto.converter;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.model.User;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.stereotype.Component;

@Component
public class AddressConverter {

  BiFunction<Object, Object, Object> nullable =
      (val, defaultVal) -> Optional.ofNullable(val).orElse(defaultVal);

  public Address mapToEntity(AddressRequest addressRequest) {
    return Address.builder()
        .title((String) nullable.apply(addressRequest.getTitle(), ""))
        .detail((String) nullable.apply(addressRequest.getDetail(), ""))
        .user(User.builder().id((Long) nullable.apply(addressRequest.getUserId(), -1L)).build())
        .build();
  }

  public AddressDTO mapToDTO(Address address) {
    return AddressDTO.builder()
        .id((Long) nullable.apply(address.getId(), 0L))
        .title((String) nullable.apply(address.getTitle(), ""))
        .detail((String) nullable.apply(address.getDetail(), ""))
        .userId(
            (Long)
                nullable.apply(address.getUser() != null ? address.getUser().getId() : null, -1L))
        .build();
  }
}
