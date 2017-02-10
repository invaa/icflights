package com.ryanair.icflights.client;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.config.ClientConfig;
import com.ryanair.icflights.dto.RouteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

/**
 * Gets ryanair specific structure routes from url.
 * Caches responses.
 */
public final class CachedRyanairRouteConnectionServiceClient
        implements RouteConnectionServiceClient {
    @Autowired
    private ClientConfig clientConfig;

    @Override
    @Cacheable(CacheConfig.CACHE_NAME)
    public List<RouteDto> getRoutes() {
        RestTemplate restTemplate = new RestTemplate();
        return Arrays.asList(
                restTemplate.getForObject(
                    clientConfig.getRouteServiceUrl(),
                    RouteDto[].class
                )
        );

    }
}
