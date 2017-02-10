package com.ryanair.icflights.config;

import org.springframework.beans.factory.annotation.Value;

/**
 * Client services config, gets parameters from configuration file.
 */
public final class FileClientConfig implements ClientConfig {
    @Value("${route.service.url}")
    private String routeServiceUrl;
    @Value("${timetable.service.url}")
    private String timetableServiceUrl;
    @Value("${timetable.service.from.parameter.name}")
    private String timetableServiceFromParameterName;
    @Value("${timetable.service.to.parameter.name}")
    private String timetableServiceToParameterName;
    @Value("${timetable.service.year.parameter.name}")
    private String timetableServiceYearParameterName;
    @Value("${timetable.service.month.parameter.name}")
    private String timetableServiceMonthParameterName;
    @Value("${timetable.service.max.connections}")
    private int timetableServiceMaxConnections;

    @Override
    public String getRouteServiceUrl() {
        return this.routeServiceUrl;
    }
    @Override
    public String getTimetableServiceUrl() {
        return this.timetableServiceUrl;
    }
    @Override
    public String getTimetableServiceFromParameterName() {
        return this.timetableServiceFromParameterName;
    }
    @Override
    public String getTimetableServiceToParameterName() {
        return this.timetableServiceToParameterName;
    }
    @Override
    public String getTimetableServiceYearParameterName() {
        return this.timetableServiceYearParameterName;
    }
    @Override
    public String getTimetableServiceMonthParameterName() {
        return this.timetableServiceMonthParameterName;
    }
    @Override
    public int getTimetableServiceMaxConnections() {
        return this.timetableServiceMaxConnections;
    }
}
