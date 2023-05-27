package com.gucardev.springboottest.service;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.model.Address;

public interface AddressService {

  AddressDTO getByIdDTO(Long id);

  Address getById(Long id);

  AddressDTO create(AddressRequest addressRequest);

  AddressDTO update(AddressRequest addressRequest);

  void delete(Long id);
}
