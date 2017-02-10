package com.ryanair.icflights.service.flight.search;

import com.ryanair.icflights.CacheConfig;
import com.ryanair.icflights.client.TimetableServiceClient;
import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.dto.MonthTimetableDto;
import com.ryanair.icflights.model.Flight;
import com.ryanair.icflights.service.period.PeriodService;
import com.ryanair.icflights.service.mapper.TimetableServiceObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Searches direct flights within a given time frame
 * between airports passed as IATA codes.
 * Caches responses.
 */
public final class CachedDirectFlightSearchService implements FlightSearchService {
    private static final Logger log =
            LoggerFactory.getLogger(CachedDirectFlightSearchService.class);
    @Autowired
    private TimetableServiceClient timetableServiceClient;
    @Autowired
    private TimetableServiceObjectMapper timetableMapper;
    @Autowired
    private PeriodService periodService;
    @Autowired
    private ServiceConfig serviceConfig;

    @Cacheable(CacheConfig.CACHE_NAME)
    @Override
    public List<Flight> getFlights(
            final String from,
            final String to,
            final LocalDateTime departureDateTime,
            final LocalDateTime arrivalDateTime) {
        final List<Flight> flights = new ArrayList<>();
        for (int i = 0; i <= periodService.getMonthsChangesAmount(departureDateTime, arrivalDateTime); i++) {
            final LocalDate flightsDate = departureDateTime.toLocalDate().plusMonths(i);
            final MonthTimetableDto monthTimetableDto
                    = timetableServiceClient.getMonthTimetable(
                        from,
                        to,
                        flightsDate.getYear(),
                        flightsDate.getMonth().getValue()
            );
            final List<Flight> currentFLights
                    = timetableMapper.mapDtoToFlights(
                        monthTimetableDto,
                        from,
                        to,
                        flightsDate
            );
            flights.addAll(
                    filterUnsuitable(
                        currentFLights,
                        departureDateTime,
                        arrivalDateTime
                    )
            );
        }
        return flights;
    }

    private List<Flight> filterUnsuitable(
            final List<Flight> flights,
            final LocalDateTime departureDateTime,
            final LocalDateTime arrivalDateTime
    ) {
        return flights.stream().flatMap(
              flight -> flight.getLegs()
                     .stream()
                     .filter(
                             leg ->
                                     leg.getArrivalDateTime()
                                             .compareTo(arrivalDateTime) <= 0
                                                && leg.getDepartureDateTime().compareTo(departureDateTime) >= 0
                     )
                     .map(
                             flightLeg -> new Flight(
                                     0,
                                     Collections.singletonList(
                                             flightLeg
                                     )
                             )
                     )
        ).collect(Collectors.toList());
    }
}
