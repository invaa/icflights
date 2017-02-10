package com.ryanair.icflights.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Services config, gets parameters from configuration file.
 */
public final class FileServiceConfig implements ServiceConfig {
    @Value("${interconnection.service.cache.ttl}")
    private long airportCacheTTL;
    @Value("${timetable.service.flight.max.delay}")
    private int flightMaxDelay;
    @Value("${timetable.service.flight.min.delay}")
    private int flightMinDelay;
    @Value("${timetable.service.wait.timeout}")
    private long timetableWaitTimeout;

    @Override
    public long getAirportCacheTTL() {
        return this.airportCacheTTL;
    }
    @Override
    public int getFlightMaxDelay() {
        return this.flightMaxDelay;
    }
    @Override
    public int getFlightMinDelay() {
        return this.flightMinDelay;
    }
    @Override
    public long getTimetableWaitTimeout() {
        return this.timetableWaitTimeout;
    }
}
