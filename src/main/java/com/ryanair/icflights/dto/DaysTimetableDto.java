package com.ryanair.icflights.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ryanair.icflights.client.mapper.DaysTimetableDtoMapper;
import java.util.List;

@JsonDeserialize(using = DaysTimetableDtoMapper.class)
public final class DaysTimetableDto {
    private final int day;
    private final List<FlightsTimetableDto> flights;

    public DaysTimetableDto(
            final int day,
            final List<FlightsTimetableDto> flights
    ) {
        this.day = day;
        this.flights = flights;
    }
    public int getDay() {
        return this.day;
    }
    public List<FlightsTimetableDto> getFlights() {
        return this.flights;
    }
}
