package com.gucardev.springboottest;

import com.gucardev.springboottest.config.CachingConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(CachingConfigurationProperties.class)
@EnableCaching
@EnableScheduling
public class SpringBootTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootTestApplication.class, args);
  }

}
