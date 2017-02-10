package com.ryanair.icflights.config;

/**
 * Contract for config utilized by client services.
 */
public interface ClientConfig {
    String getRouteServiceUrl();
    String getTimetableServiceUrl();
    String getTimetableServiceFromParameterName();
    String getTimetableServiceToParameterName();
    String getTimetableServiceYearParameterName();
    String getTimetableServiceMonthParameterName();
    int getTimetableServiceMaxConnections();
}
