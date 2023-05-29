package com.gucardev.springboottest.controller;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.request.AddressRequest;
import com.gucardev.springboottest.service.AddressService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnExpression(
    "${address.controller.enabled:false}")
@RestController
@RequestMapping("/api/address")
public class AddressController {

  private final AddressService addressService;

  public AddressController(AddressService addressService) {
    this.addressService = addressService;
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<AddressDTO>> getAll(@PathVariable Long id) {
    List<AddressDTO> result = addressService.getAllByUserId(id);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AddressDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(addressService.getByIdDTO(id));
  }

  @PostMapping
  public ResponseEntity<AddressDTO> createAddress(@RequestBody @Valid AddressRequest addressRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(addressRequest));
  }

  @PutMapping
  public ResponseEntity<AddressDTO> updateAddress(@RequestBody @Valid AddressRequest addressRequest) {
    return ResponseEntity.ok(addressService.update(addressRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<AddressDTO> deleteAddress(@PathVariable Long id) {
    addressService.delete(id);
    return ResponseEntity.ok().build();
  }
}
