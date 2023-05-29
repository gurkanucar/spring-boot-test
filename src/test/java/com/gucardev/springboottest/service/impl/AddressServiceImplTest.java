package com.gucardev.springboottest.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gucardev.springboottest.dto.AddressDTO;
import com.gucardev.springboottest.dto.converter.AddressConverter;
import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.repository.AddressRepository;
import com.gucardev.springboottest.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest extends AddressServiceTestSupport {

  @Mock private AddressRepository addressRepository;
  @Mock private AddressConverter addressConverter;
  @Mock private UserService userService;

  private AddressServiceImpl addressService;

  @BeforeEach
  void setup() {
    addressService =
        Mockito.spy(new AddressServiceImpl(addressRepository, userService, addressConverter));
    setupTestData();
  }

  @Test
  void getAllByUserId_givenExistingUserId_ReturnAddressDTO() {
    when(userService.userExistsById(any())).thenReturn(Boolean.TRUE);

    when(addressConverter.mapToDTO(address1)).thenReturn(addressDto1);
    when(addressConverter.mapToDTO(address2)).thenReturn(addressDto2);

    when(addressRepository.findAllByUser_Id(anyLong()))
        .thenReturn(Arrays.asList(address1, address2));

    List<AddressDTO> actual = addressService.getAllByUserId(anyLong());
    assertEquals(Arrays.asList(addressDto1, addressDto2), actual);
    assertEquals(2, actual.size());
    verify(addressRepository, times(1)).findAllByUser_Id(anyLong());
  }

  @Test
  void getAllByUserId_givenNonExistingUserId_ThrowException() {
    when(userService.userExistsById(any())).thenReturn(Boolean.FALSE);
    assertThrows(RuntimeException.class, () -> addressService.getAllByUserId(anyLong()));
  }

  @ParameterizedTest
  @CsvSource({"1,true", "2,false"})
  void getByIdDTO_givenId_ReturnAddressDTOorThrowException(Long id, Boolean isExisting) {
    if (isExisting) {
      doReturn(address1).when(addressService).getById(id);
      when(addressConverter.mapToDTO(address1)).thenReturn(addressDto1);
      AddressDTO actual = addressService.getByIdDTO(id);
      assertEquals(addressDto1, actual);
      verify(addressConverter, times(1)).mapToDTO(address1);

    } else {
      doThrow(new RuntimeException()).when(addressService).getById(id);
      assertThrows(RuntimeException.class, () -> addressService.getByIdDTO(id));
      verify(addressConverter, never()).mapToDTO(any());
    }
  }

  @ParameterizedTest
  @CsvSource({"1,true", "2,false"})
  void getById_givenId_ReturnAddressOrThrowException2(Long id, Boolean isExisting) {
    if (isExisting) {
      when(addressRepository.findById(id)).thenReturn(Optional.of(address1));
      Address actual = addressService.getById(id);
      assertEquals(address1, actual);

    } else {
      when(addressRepository.findById(id)).thenReturn(Optional.empty());
      assertThrows(RuntimeException.class, () -> addressService.getById(id));
    }
  }


}
