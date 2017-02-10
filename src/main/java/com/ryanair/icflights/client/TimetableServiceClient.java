package com.ryanair.icflights.client;

import com.ryanair.icflights.dto.MonthTimetableDto;

/**
 * Timetable client contract.
 */
public interface TimetableServiceClient {
    MonthTimetableDto getMonthTimetable(
            final String from,
            final String to,
            final int year,
            final int month
    );
}
