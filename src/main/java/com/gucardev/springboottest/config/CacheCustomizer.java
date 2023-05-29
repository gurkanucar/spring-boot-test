package com.gucardev.springboottest.config;

import java.util.Arrays;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

  private final CachingConfigurationProperties cacheConfig;

  public CacheCustomizer(CachingConfigurationProperties cacheConfig) {
    this.cacheConfig = cacheConfig;
  }

  @Override
  public void customize(ConcurrentMapCacheManager cacheManager) {
    cacheManager.setCacheNames(
        Arrays.asList(
            cacheConfig.getUser().getCacheName(), cacheConfig.getAddress().getCacheName()));
  }
}
