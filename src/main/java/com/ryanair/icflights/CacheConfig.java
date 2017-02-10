package com.ryanair.icflights;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@EnableCaching
public class CacheConfig {
    private static final Logger log =
            LoggerFactory.getLogger(CacheConfig.class);
    public static final String CACHE_NAME = "IC_CACHE";
    public static final long CACHE_TTL = 60 * 1000;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CACHE_NAME);
    }
    @CacheEvict(allEntries = true, value = {CACHE_NAME})
    @Scheduled(fixedRate = CACHE_TTL)
    public void evictCache() {
        log.debug("Flushed cache for " + CACHE_NAME);
    }
}
