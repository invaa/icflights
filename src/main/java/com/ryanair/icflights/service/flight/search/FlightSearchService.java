package com.ryanair.icflights.service.flight.search;

import com.ryanair.icflights.model.Flight;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Flights search service contract.
 */
public interface FlightSearchService {
    List<Flight> getFlights(
            final String from,
            final String to,
            final LocalDateTime departureTime,
            final LocalDateTime arrivalTime
            );
}
