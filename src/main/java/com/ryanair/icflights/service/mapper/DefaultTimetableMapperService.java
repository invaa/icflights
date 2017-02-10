package com.ryanair.icflights.service.mapper;

import com.ryanair.icflights.dto.MonthTimetableDto;
import com.ryanair.icflights.model.Flight;
import com.ryanair.icflights.model.FlightLeg;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implements the monthTimetableDto to the list of flights convertor.
 */
public final class DefaultTimetableMapperService
        implements TimetableServiceObjectMapper {
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<Flight> mapDtoToFlights(
            final MonthTimetableDto monthTimetableDto,
            final String from,
            final String to,
            final LocalDate flightsDate
    ) {
        return monthTimetableDto
                .getDays()
                .stream()
                .flatMap(daysDto -> daysDto.getFlights().stream()
                        .map(
                                flightsDto ->
                                        new Flight(
                                                0,
                                                Arrays.asList(
                                                        new FlightLeg(
                                                                from,
                                                                to,
                                                                LocalDateTime.of(
                                                                        LocalDate.of(
                                                                                flightsDate.getYear(),
                                                                                flightsDate.getMonth(),
                                                                                daysDto.getDay()
                                                                        ),
                                                                        LocalTime.parse(
                                                                                flightsDto.getDepartureTime(),
                                                                                FORMATTER)
                                                                ),
                                                                LocalDateTime.of(
                                                                        LocalDate.of(
                                                                                flightsDate.getYear(),
                                                                                flightsDate.getMonth(),
                                                                                daysDto.getDay()
                                                                        ),
                                                                        LocalTime.parse(
                                                                                flightsDto.getArrivalTime(),
                                                                                FORMATTER)
                                                                )
                                                        )
                                                )
                                        )
                        )
                ).collect(Collectors.toList());
    }
}
