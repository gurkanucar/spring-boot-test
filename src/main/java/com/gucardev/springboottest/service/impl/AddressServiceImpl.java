package com.gucardev.springboottest.service.impl;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.converter.AddressConverter;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.repository.AddressRepository;
import com.gucardev.springboottest.service.AddressService;
import com.gucardev.springboottest.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final UserService userService;
  private final AddressConverter addressConverter;

  public AddressServiceImpl(
      AddressRepository addressRepository,
      UserService userService,
      AddressConverter addressConverter) {
    this.addressRepository = addressRepository;
    this.userService = userService;
    this.addressConverter = addressConverter;
  }

  @Override
  public List<AddressDTO> getAllByUserId(Long id) {
    if (!userService.userExistsById(id)) {
      throw new RuntimeException("user not found!");
    }
    return addressRepository.findAllByUser_Id(id).stream()
        .map(addressConverter::mapToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public AddressDTO getByIdDTO(Long id) {
    return addressRepository
        .findById(id)
        .map(addressConverter::mapToDTO)
        .orElseThrow(() -> new RuntimeException("address not found!"));
  }

  @Override
  public Address getById(Long id) {
    return addressRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("address not found!"));
  }

  @Override
  public AddressDTO create(AddressRequest addressRequest) {
    if (!userService.userExistsById(addressRequest.getUserId())) {
      throw new RuntimeException("user not found!");
    }
    Address address = addressConverter.mapToEntity(addressRequest);

    return addressConverter.mapToDTO(addressRepository.save(address));
  }

  @Override
  public AddressDTO update(AddressRequest addressRequest) {
    if (!Optional.ofNullable(addressRequest.getId()).isPresent()) {
      throw new RuntimeException("address not found!");
    }

    Address existing = addressRepository.findById(addressRequest.getId()).orElse(null);
    if (!Optional.ofNullable(existing).isPresent()) {
      throw new RuntimeException("address not found!");
    }
    existing.setTitle(addressRequest.getTitle());
    existing.setDetail(addressRequest.getDetail());
    return addressConverter.mapToDTO(addressRepository.save(existing));
  }

  @Override
  public void delete(Long id) {
    if (!addressRepository.findById(id).isPresent()) {
      throw new RuntimeException("address not found!");
    }
    addressRepository.deleteById(id);
  }
}
