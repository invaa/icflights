package com.ryanair.icflights.config;

/**
 * Contract for config utilized by services.
 */
public interface ServiceConfig {
    long getAirportCacheTTL();
    int getFlightMaxDelay();
    int getFlightMinDelay();
    long getTimetableWaitTimeout();
}
