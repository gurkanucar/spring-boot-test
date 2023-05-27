package com.gucardev.springboottest.service.impl;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.repository.AddressRepository;
import com.gucardev.springboottest.service.AddressService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;

  public AddressServiceImpl(AddressRepository addressRepository) {
    this.addressRepository = addressRepository;
  }

  @Override
  public List<AddressDTO> getAll() {
    return null;
  }

  @Override
  public AddressDTO getByIdDTO(Long id) {
    return null;
  }

  @Override
  public Address getById(Long id) {
    return null;
  }

  @Override
  public AddressDTO create(AddressRequest addressRequest) {
    return null;
  }

  @Override
  public AddressDTO update(AddressRequest addressRequest) {
    return null;
  }

  @Override
  public void delete(Long id) {}
}
