package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.model.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides the implementation of route availability service.
 * Updates the inner routes cache each airport TTL seconds.
 */
public final class SingleThreadUpdateRouteAvailabilityService
        implements RouteAvailabilityService {
    private static final Logger log =
            LoggerFactory.getLogger(SingleThreadUpdateRouteAvailabilityService.class);
    @Autowired
    private ServiceConfig serviceConfig;
    private Map<String, Airport> airports;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final RouteAvailabilityService routeAvailabilityService;

    public SingleThreadUpdateRouteAvailabilityService(
            final RouteAvailabilityService routeAvailabilityService
    ) {
        this.routeAvailabilityService = routeAvailabilityService;
    }
    @PostConstruct
    public void init() throws Exception {
        startScheduler();
    }
    @PreDestroy
    public void destroy() throws Exception {
        stopScheduler();
    }
    private void startScheduler() {
        log.info("Starting scheduled route availability service with update interval of "
                + this.serviceConfig.getAirportCacheTTL());
        Runnable task = () -> {
            Thread.currentThread().setName("au-scheduler");
            try {
                Map<String, Airport> airportMap
                        = this.routeAvailabilityService.getAirports();
                this.airports = Collections.unmodifiableMap(airportMap);
                log.info("Airports updated.");
            } catch (RuntimeException e) {
                log.warn("Error updating airports.", e);
            }
        };
        this.scheduler.scheduleWithFixedDelay(
                task,
                0,
                this.serviceConfig.getAirportCacheTTL(),
                TimeUnit.SECONDS
        );
    }
    private void stopScheduler() {
        log.info("Stopping route availability service...");
        this.scheduler.shutdownNow();
    }
    @Override
    public Map<String, Airport> getAirports() {
        return this.airports;
    }
}
