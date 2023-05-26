package com.gucardev.springboottest.remote;

import com.gucardev.springboottest.dto.RestPageResponse;
import com.gucardev.springboottest.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "remote-user", url = "http://localhost:3000/mock")
public interface RemoteUserClient {

  @GetMapping("/user")
  RestPageResponse<UserDTO> getUsers();
}
