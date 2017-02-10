package com.ryanair.icflights.service.validator;

import com.ryanair.icflights.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Validates flight interval for flight service.
 * throws IllegalArgumentException if input is not valid.
 */
public final class DefaultFlightIntervalValidator
        implements FlightIntervalValidator {
    private static final Logger log =
            LoggerFactory.getLogger(DefaultFlightIntervalValidator.class);
    @Autowired
    private ServiceConfig serviceConfig;
    private static final int SECONDS_IN_HOUR = 3600;

    @Override
    public boolean isValid(final LocalDateTime departureTime,
                          final LocalDateTime arrivalTime
    ) throws IllegalArgumentException {
            final Duration duration = Duration.between(departureTime, arrivalTime);
            final long durationHours = duration.getSeconds() / SECONDS_IN_HOUR;
            final StringBuilder errorBuilder = new StringBuilder();
            boolean result = true;
            if (durationHours > serviceConfig.getFlightMaxDelay()) {
                errorBuilder
                        .append("Specified duration exceeds maximum allowed: ")
                        .append(departureTime)
                        .append(", ")
                        .append(arrivalTime)
                        .append(". ");
                result = false;
            }
            if (durationHours < 0) {
                errorBuilder
                        .append("Specified duration should not be negative: ")
                        .append(departureTime)
                        .append(", ")
                        .append(arrivalTime)
                        .append(". ");
                result = false;
            }
            if (LocalDateTime.now().compareTo(departureTime) == 1
                    || LocalDateTime.now().compareTo(arrivalTime) == 1) {
                errorBuilder
                        .append("Specified duration should not be in the past: ")
                        .append(departureTime)
                        .append(", ")
                        .append(arrivalTime)
                        .append(". ");
                result = false;
            }
            if (!result) {
                log.debug(errorBuilder.toString());
                throw new IllegalArgumentException(errorBuilder.toString());
            }
        return true;
        }
}
