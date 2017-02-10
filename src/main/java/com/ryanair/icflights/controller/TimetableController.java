package com.ryanair.icflights.controller;

import com.ryanair.icflights.annotation.ApiVersion;
import com.ryanair.icflights.model.Flight;
import com.ryanair.icflights.service.flight.search.FlightSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for timetable services.
 */
@RestController
@ApiVersion(1)
@RequestMapping("timetable")
public final class TimetableController {
    @Autowired
    private FlightSearchService flightSearchService;

    @RequestMapping("/interconnections")
    public List<Flight> interConnection(
            @RequestParam(value="departure") String from,
            @RequestParam(value="arrival") String to,
            @RequestParam(value="departureDateTime")
            @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
                    LocalDateTime departureTime,
            @RequestParam(value="arrivalDateTime")
            @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
                    LocalDateTime arrivalTime
    ) {
        return flightSearchService.getFlights(from, to, departureTime, arrivalTime);
    }
}
