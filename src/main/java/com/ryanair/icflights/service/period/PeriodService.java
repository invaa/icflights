package com.ryanair.icflights.service.period;

import java.time.LocalDateTime;

/**
 * Calculates month shifts in given period.
 */
public interface PeriodService {
     int getMonthsChangesAmount(
             final LocalDateTime departureTime,
             final LocalDateTime arrivalTime
     );
}
