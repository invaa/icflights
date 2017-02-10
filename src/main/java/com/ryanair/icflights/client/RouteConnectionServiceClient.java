package com.ryanair.icflights.client;

import com.ryanair.icflights.dto.RouteDto;

import java.util.List;

/**
 * Route connections client contract.
 */
public interface RouteConnectionServiceClient {
    List<RouteDto> getRoutes();
}
