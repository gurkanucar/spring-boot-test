package com.gucardev.springboottest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringBootTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootTestApplication.class, args);
  }

}
