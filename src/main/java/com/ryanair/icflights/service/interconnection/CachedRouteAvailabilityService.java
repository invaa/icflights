package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.client.RouteConnectionServiceClient;
import com.ryanair.icflights.model.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for getting IATA codes - Airport object representationsю
 * Uses caching for requests.
 */
public class CachedRouteAvailabilityService
        implements RouteAvailabilityService {
    @Autowired
    private RouteConnectionServiceClient routeConnectionServiceClient;

    @Cacheable(CacheConfig.CACHE_NAME)
    @Override
    public Map<String, Airport> getAirports() {
        final Map<String, Set<String>> connectionsFrom = new HashMap<>();
        final Map<String, Set<String>> connectionsTo = new HashMap<>();
        final Set<String> uniqueAirportNames = new HashSet<>();
        routeConnectionServiceClient.getRoutes().forEach(
            routeDto -> {
                updateUniqueAirportNames(uniqueAirportNames, routeDto.getAirportFrom());
                updateUniqueAirportNames(uniqueAirportNames, routeDto.getAirportTo());
                updateConnections(
                        connectionsFrom,
                        routeDto.getAirportTo(),
                        routeDto.getAirportFrom()
                );
                updateConnections(
                        connectionsTo,
                        routeDto.getAirportFrom(),
                        routeDto.getAirportTo()
                );
            }
        );
        return uniqueAirportNames
                .stream().map(
                    airportName -> new Airport(
                            airportName,
                            connectionsFrom.get(airportName),
                            connectionsTo.get(airportName)
                            )
                ).collect(
                        Collectors.toMap(
                                Airport::getName,
                                Function.identity()
                        )
                );
    }
    private void updateConnections(
            final Map<String, Set<String>> connectionMap,
            final String airportName,
            final String connectionAirportName
    ) {
        Set<String> connections = connectionMap.get(airportName);
        if (connections == null) {
            connections = new HashSet<>();
        }
        connections.add(connectionAirportName);
        connectionMap.put(airportName, connections);
    }
    private void updateUniqueAirportNames(
            final Set<String> uniqueAirportNames,
            final String airportName
    ) {
        if (!uniqueAirportNames.contains(airportName))
            uniqueAirportNames.add(airportName);
    }
}