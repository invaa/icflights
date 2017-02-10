package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.model.Airport;

import java.util.Map;

/**
 * Service for getting IATA codes - Airport object representations
 */
public interface RouteAvailabilityService {
    Map<String, Airport> getAirports();
}
