package com.ryanair.icflights.service.flight.search;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.client.TimetableServiceClient;
import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.model.Flight;
import com.ryanair.icflights.model.FlightLeg;
import com.ryanair.icflights.model.Pair;
import com.ryanair.icflights.model.PairOfStrings;
import com.ryanair.icflights.service.interconnection.InterconnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * Searches direct and indirect flights within a given time frame
 * between airports passed as IATA codes.
 * Caches responses.
 */
public final class CachedInterchangeFlightSearchService implements FlightSearchService {
    private static final Logger log =
            LoggerFactory.getLogger(CachedInterchangeFlightSearchService.class);
    @Autowired
    private TimetableServiceClient timetableServiceClient;
    @Autowired
    private InterconnectionService interconnectionService;
    @Autowired
    private ServiceConfig serviceConfig;
    @Autowired
    @Qualifier("direct")
    private FlightSearchService directFlightSearchService;

    private ExecutorService executorService;

    @PostConstruct
    public void startExecutor() {
        this.executorService = Executors.newCachedThreadPool();
    }

    @PreDestroy
    public void stopExecutor() {
        this.executorService.shutdownNow();
    }

    @Cacheable(CacheConfig.CACHE_NAME)
    @Override
    public List<Flight> getFlights(
            final String from,
            final String to,
            final LocalDateTime departureTime,
            final LocalDateTime arrivalTime) {
        final List<CompletableFuture<Boolean>> resultFutureArray
                = new ArrayList<>();
        final List<Flight> flights = new CopyOnWriteArrayList<>();
        resultFutureArray.add(
                CompletableFuture.supplyAsync(() ->
                        directFlightSearchService
                                .getFlights(from, to, departureTime, arrivalTime)
                        , executorService)
                        .thenApplyAsync(flights::addAll, executorService)
        );
        final Set<Pair<PairOfStrings, PairOfStrings>> interconnections
                = interconnectionService.getInterconnections(from, to);
        interconnections.forEach(
                interconnection ->
                        resultFutureArray.add(
                                CompletableFuture.supplyAsync(
                                        () -> directFlightSearchService
                                                .getFlights(
                                                        interconnection.getA().getA(),
                                                        interconnection.getA().getB(),
                                                        departureTime,
                                                        arrivalTime
                                                ),
                                executorService
                                ).thenApplyAsync(
                                        legOneFlights -> {
                                            legOneFlights.forEach(
                                                    flight -> {
                                                        //we have either one or no legs from direct service
                                                        final FlightLeg legOne = flight.getLegs().iterator().next();
                                                        final LocalDateTime legTwoStartingTime =
                                                                legOne.getArrivalDateTime().plusHours(
                                                                        serviceConfig.getFlightMinDelay()
                                                                );
                                                        if (legTwoStartingTime.compareTo(arrivalTime) <= 0) {
                                                            List<Flight> legTwoFlights = directFlightSearchService
                                                                    .getFlights(
                                                                            interconnection.getB().getA(),
                                                                            interconnection.getB().getB(),
                                                                            legTwoStartingTime,
                                                                            arrivalTime
                                                                    );
                                                            legTwoFlights.forEach(
                                                                    flightLegTwo -> {
                                                                        final FlightLeg legTwo = flightLegTwo.getLegs().iterator().next();
                                                                        final List<FlightLeg> legList =
                                                                                Arrays.asList(
                                                                                        new FlightLeg(
                                                                                                interconnection.getA().getA(),
                                                                                                interconnection.getA().getB(),
                                                                                                legOne.getDepartureDateTime(),
                                                                                                legOne.getArrivalDateTime()
                                                                                        ),
                                                                                        new FlightLeg(
                                                                                                interconnection.getB().getA(),
                                                                                                interconnection.getB().getB(),
                                                                                                legTwo.getDepartureDateTime(),
                                                                                                legTwo.getArrivalDateTime()
                                                                                        )
                                                                                );
                                                                        flights.add(
                                                                                new Flight(
                                                                                        1,
                                                                                        legList
                                                                                )
                                                                        );
                                                                    }
                                                            );
                                                        }
                                                    }
                                            );
                                            return true;
                                        },
                                        executorService
                                )
                        )
        );
        try {
            CompletableFuture.allOf(
                    resultFutureArray.
                            toArray(new CompletableFuture[0])
            ).get(serviceConfig.getTimetableWaitTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.warn("There were an error during the timetable calculation", e);
        }
        return flights;
    }
}
