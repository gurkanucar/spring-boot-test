package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.Address;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findAllByUser_Id(Long id);
}
