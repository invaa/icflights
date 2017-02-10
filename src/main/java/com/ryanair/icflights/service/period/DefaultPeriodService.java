package com.ryanair.icflights.service.period;

import java.time.LocalDateTime;
import java.time.Period;

/**
 * Implements the month shift in given period calculator.
 * Shift is equals to times the month changes in a given period.
 */
public final class DefaultPeriodService implements PeriodService {
    private static final int MONTHS_IN_YEAR = 12;

    public int getMonthsChangesAmount(final LocalDateTime departureTime,
                                       final LocalDateTime arrivalTime) {
        final Period period = Period.between(
                departureTime.toLocalDate(),
                arrivalTime.toLocalDate()
        );
        int result = period.getMonths() + period.getYears() * MONTHS_IN_YEAR;
        if (result - period.getYears() * MONTHS_IN_YEAR == 0
                && departureTime.getMonth() != arrivalTime.getMonth()
            ) {
            result++;
        }
        return result;
    }
}
