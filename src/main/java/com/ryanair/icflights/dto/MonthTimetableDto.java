package com.ryanair.icflights.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ryanair.icflights.client.mapper.MonthTimetableDtoMapper;

import java.util.List;

@JsonDeserialize(using = MonthTimetableDtoMapper.class)
public final class MonthTimetableDto {
    private final int month;
    private final List<DaysTimetableDto> days;

    public MonthTimetableDto(
            final int month,
            final List<DaysTimetableDto> days
    ) {
        this.month = month;
        this.days = days;
    }
    public int getMonth() {
        return this.month;
    }
    public List<DaysTimetableDto> getDays() {
        return this.days;
    }
}
