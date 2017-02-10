package com.ryanair.icflights.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collections;
import java.util.List;

@JsonPropertyOrder(
        {
                "stops",
                "legs"
        }
)
public final class Flight {
    private final int stops;
    private final List<FlightLeg> legs;

    public Flight(
            final int stops,
            final List<FlightLeg> legs) {
        this.stops = stops;
        this.legs = Collections.unmodifiableList(legs);
    }
    public int getStops() {
        return this.stops;
    }
    public List<FlightLeg> getLegs() {
        return this.legs;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Flight flight = (Flight) o;
        return stops == flight.stops && legs.equals(flight.legs);
    }
    @Override
    public int hashCode() {
        int result = stops;
        result = 31 * result + legs.hashCode();
        return result;
    }
}
