package com.ryanair.icflights.client;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.config.ClientConfig;
import com.ryanair.icflights.dto.MonthTimetableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Gets ryanair specific structure timetables from url.
 * Caches responses.
 */
public final class CachedRyanairTimetableServiceClient
        implements TimetableServiceClient {
    @Autowired
    private ClientConfig clientConfig;

    @Override
    @Cacheable(CacheConfig.CACHE_NAME)
    public MonthTimetableDto getMonthTimetable(
            final String from,
            final String to,
            final int year,
            final int month
    ) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(clientConfig.getTimetableServiceFromParameterName(), from);
        parameters.put(clientConfig.getTimetableServiceToParameterName(), to);
        parameters.put(clientConfig.getTimetableServiceYearParameterName(), year);
        parameters.put(clientConfig.getTimetableServiceMonthParameterName(), month);
        String url =
                UriComponentsBuilder
                        .fromUriString(clientConfig.getTimetableServiceUrl())
                        .buildAndExpand(parameters).toUriString();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                        url,
                        MonthTimetableDto.class
        );
    }
}
