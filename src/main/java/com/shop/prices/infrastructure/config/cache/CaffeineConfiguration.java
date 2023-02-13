package com.shop.prices.infrastructure.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CaffeineConfiguration {

    public static final String MAIN_CACHE_NAME = "ratesCache";

    @Bean
    public CaffeineCache caffeineConfig(@Value("${application.cacheTimeout}") long cacheTimeout) {
        return new CaffeineCache(
            MAIN_CACHE_NAME,
            Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(cacheTimeout)).build()
        );
    }

}
