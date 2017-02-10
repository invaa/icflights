package com.ryanair.icflights.service;

import com.ryanair.icflights.service.period.DefaultPeriodService;
import com.ryanair.icflights.service.period.PeriodService;
import org.junit.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.*;

public final class DefaultPeriodServiceTest {
    private final PeriodService periodService = new DefaultPeriodService();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Test
    public void shouldReturnZero() throws Exception {
        final LocalDateTime departureTime = LocalDateTime.parse("2017-01-03T10:00", formatter);
        final LocalDateTime arrivalTime = LocalDateTime.parse("2017-01-03T22:00", formatter);
        assertEquals(0, periodService.getMonthsChangesAmount(
                departureTime,
                arrivalTime
        ));
    }
    @Test
    public void shouldReturnOne() throws Exception {
        final LocalDateTime departureTime = LocalDateTime.parse("2017-01-29T10:00", formatter);
        final LocalDateTime arrivalTime = LocalDateTime.parse("2017-02-01T22:00", formatter);
        assertEquals(1, periodService.getMonthsChangesAmount(
                departureTime,
                arrivalTime
        ));
    }
    @Test
    public void shouldReturnFourteen() throws Exception {
        final LocalDateTime departureTime = LocalDateTime.parse("2017-01-29T10:00", formatter);
        final LocalDateTime arrivalTime = LocalDateTime.parse("2018-03-30T22:00", formatter);
        assertEquals(14, periodService.getMonthsChangesAmount(
                departureTime,
                arrivalTime
        ));
    }
    @Test
    public void shouldReturnThirteen() throws Exception {
        final LocalDateTime departureTime = LocalDateTime.parse("2017-01-29T10:00", formatter);
        final LocalDateTime arrivalTime = LocalDateTime.parse("2018-02-01T22:00", formatter);
        assertEquals(13, periodService.getMonthsChangesAmount(
                departureTime,
                arrivalTime
        ));
    }
    @Test
    public void shouldReturnMinusOne() throws Exception {
        final LocalDateTime departureTime = LocalDateTime.parse("2017-02-04T10:00", formatter);
        final LocalDateTime arrivalTime = LocalDateTime.parse("2017-01-03T22:00", formatter);
        assertEquals(-1, periodService.getMonthsChangesAmount(
                departureTime,
                arrivalTime
        ));
    }
}