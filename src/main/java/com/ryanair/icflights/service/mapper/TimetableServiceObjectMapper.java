package com.ryanair.icflights.service.mapper;

import com.ryanair.icflights.dto.MonthTimetableDto;
import com.ryanair.icflights.model.Flight;
import java.time.LocalDate;
import java.util.List;

/**
 * Converts monthTimetableDto to the list of flights.
 */
public interface TimetableServiceObjectMapper {
    List<Flight> mapDtoToFlights(
            final MonthTimetableDto monthTimetableDto,
            final String from,
            final String to,
            final LocalDate flightsDate
    );
}
