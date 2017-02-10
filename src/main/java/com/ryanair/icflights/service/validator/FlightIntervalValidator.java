package com.ryanair.icflights.service.validator;

import java.time.LocalDateTime;

/**
 * Validates flight interval for flight service.
 */
public interface FlightIntervalValidator {
    boolean isValid(final LocalDateTime departureTime,
                    final LocalDateTime arrivalTime
    ) throws IllegalArgumentException;
}
