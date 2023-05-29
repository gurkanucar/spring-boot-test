package com.gucardev.springboottest.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableConfigurationProperties(value = CachingConfigurationProperties.class)
@Configuration
class CachingConfigurationPropertiesTest {

  @Autowired private CachingConfigurationProperties cachingConfigurationProperties;

  @Test
  void constructor_shouldInitializeProperties() {

    String userCacheName = "user";
    Long userCacheTtl = 10000L;
    String addressCacheName = "address";
    Long addressCacheTtl = 10000L;

    assertEquals(userCacheName, cachingConfigurationProperties.getUser().getCacheName());
    assertEquals(userCacheTtl, cachingConfigurationProperties.getUser().getCacheTtl());
    assertEquals(addressCacheName, cachingConfigurationProperties.getAddress().getCacheName());
    assertEquals(addressCacheTtl, cachingConfigurationProperties.getAddress().getCacheTtl());
  }
}
