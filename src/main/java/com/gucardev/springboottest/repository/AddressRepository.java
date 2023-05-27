package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository  extends JpaRepository<Address,Long> {

}
