package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.model.Airport;
import com.ryanair.icflights.model.Pair;
import com.ryanair.icflights.model.PairOfStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements the 1 stop connection locator service for two IATA airport codes.
 * Uses caching for requests.
 */
public class CachedInterconnectionService
        implements InterconnectionService {
    private static final Logger log =
            LoggerFactory.getLogger(CachedInterconnectionService.class);
    @Autowired
    private RouteAvailabilityService routeAvailabilityService;

    @Cacheable(CacheConfig.CACHE_NAME)
    @Override
    public Set<
            Pair<PairOfStrings, PairOfStrings>
            > getInterconnections(final String from, final String to) {
        final Airport airportTo = routeAvailabilityService.getAirports().get(to);
        final Airport airportFrom = routeAvailabilityService.getAirports().get(from);
        if (airportTo == null || airportFrom == null) {
            String errorMessage = "No routes from departure airport or " +
                    "no routes to arrival airport available.";
            log.debug(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } else {
            return airportFrom.getConnectionsFrom()
                    .stream()
                    .filter(airportTo.getConnectionsTo()::contains)
                    .map(connection ->
                            new Pair<>(
                                    new PairOfStrings(from, connection),
                                    new PairOfStrings(connection, to)
                            )
                    ).collect(Collectors.toSet());
        }
    }
}
