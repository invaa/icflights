package com.ryanair.icflights.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@JsonPropertyOrder(
        {
                "departureAirport",
                "arrivalAirport",
                "departureDateTime",
                "arrivalDateTime"
        }
)
public final class FlightLeg {
    @JsonProperty("departureAirport")
    private final String from;
    @JsonProperty("arrivalAirport")
    private final String to;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private final LocalDateTime departureDateTime;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private final LocalDateTime arrivalDateTime;

    public FlightLeg(
            final String from,
            final String to,
            final LocalDateTime departureDateTime,
            final LocalDateTime arrivalDateTime
    ) {
        this.from = from;
        this.to = to;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }
    public String getFrom() {
        return this.from;
    }
    public String getTo() {
        return this.to;
    }
    public LocalDateTime getDepartureDateTime() {
        return this.departureDateTime;
    }
    public LocalDateTime getArrivalDateTime() {
        return this.arrivalDateTime;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FlightLeg flightLeg = (FlightLeg) o;
        return from.equals(flightLeg.from)
                && to.equals(flightLeg.to)
                    && departureDateTime.equals(flightLeg.departureDateTime)
                        && arrivalDateTime.equals(flightLeg.arrivalDateTime);
    }
    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        result = 31 * result + departureDateTime.hashCode();
        result = 31 * result + arrivalDateTime.hashCode();
        return result;
    }
}
