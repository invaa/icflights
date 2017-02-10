package com.ryanair.icflights.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ryanair.icflights.client.mapper.FlightsTimetableDtoMapper;

@JsonDeserialize(using = FlightsTimetableDtoMapper.class)
public final class FlightsTimetableDto {
    private final String number;
    private final String arrivalTime;
    private final String departureTime;

    public FlightsTimetableDto(
            final String number,
            final String departureTime,
            final String arrivalTime
    ) {
        this.number = number;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }
    public String getNumber() {
        return this.number;
    }
    public String getArrivalTime() {
        return this.arrivalTime;
    }
    public String getDepartureTime() {
        return this.departureTime;
    }
}
