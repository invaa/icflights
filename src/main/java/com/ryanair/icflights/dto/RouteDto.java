package com.ryanair.icflights.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ryanair.icflights.client.mapper.AirportDtoMapper;

@JsonDeserialize(using = AirportDtoMapper.class)
public final class RouteDto {
    private final String airportFrom;
    private final String airportTo;

    public RouteDto(final String airportFrom, final String airportTo) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
    }
    public String getAirportFrom() {
        return this.airportFrom;
    }
    public String getAirportTo() {
        return this.airportTo;
    }
}
