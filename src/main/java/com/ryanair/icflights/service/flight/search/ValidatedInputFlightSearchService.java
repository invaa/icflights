package com.ryanair.icflights.service.flight.search;

import com.ryanair.icflights.model.Flight;
import com.ryanair.icflights.service.validator.FlightIntervalValidator;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Flights search service decorator.
 * Validates input by provided validation service.
 */
public final class ValidatedInputFlightSearchService implements FlightSearchService {
    @Autowired
    private FlightIntervalValidator flightIntervalValidator;
    private final FlightSearchService timetableService;

    public ValidatedInputFlightSearchService(final FlightSearchService flightSearchService) {
        this.timetableService = flightSearchService;
    }

    @Override
    public List<Flight> getFlights(
            final String from,
            final String to,
            final LocalDateTime departureTime,
            final LocalDateTime arrivalTime) {
        if (flightIntervalValidator.isValid(departureTime, arrivalTime)) {
            return timetableService.getFlights(from, to, departureTime, arrivalTime);
        } else {
            return Collections.emptyList();
        }
    }
}
