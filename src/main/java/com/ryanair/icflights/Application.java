package com.ryanair.icflights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.ryanair.icflights.config.ClientConfig;
import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.config.FileClientConfig;
import com.ryanair.icflights.config.FileServiceConfig;
import org.springframework.context.annotation.Bean;
import com.ryanair.icflights.client.CachedRyanairRouteConnectionServiceClient;
import com.ryanair.icflights.client.RouteConnectionServiceClient;
import com.ryanair.icflights.client.CachedRyanairTimetableServiceClient;
import com.ryanair.icflights.client.LimitedConnectionTimetableServiceClient;
import com.ryanair.icflights.client.TimetableServiceClient;
import com.ryanair.icflights.service.interconnection.InterconnectionService;
import com.ryanair.icflights.service.interconnection.CachedInterconnectionService;
import com.ryanair.icflights.service.interconnection.SingleThreadUpdateRouteAvailabilityService;
import com.ryanair.icflights.service.interconnection.RouteAvailabilityService;
import com.ryanair.icflights.service.interconnection.CachedRouteAvailabilityService;
import com.ryanair.icflights.service.flight.search.FlightSearchService;
import com.ryanair.icflights.service.flight.search.CachedInterchangeFlightSearchService;
import com.ryanair.icflights.service.flight.search.CachedDirectFlightSearchService;
import com.ryanair.icflights.service.flight.search.ValidatedInputFlightSearchService;
import com.ryanair.icflights.service.mapper.DefaultTimetableMapperService;
import com.ryanair.icflights.service.mapper.TimetableServiceObjectMapper;
import com.ryanair.icflights.service.period.DefaultPeriodService;
import com.ryanair.icflights.service.period.PeriodService;
import com.ryanair.icflights.service.validator.DefaultFlightIntervalValidator;
import com.ryanair.icflights.service.validator.FlightIntervalValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    public ClientConfig clientConfig() {
        return new FileClientConfig();
    }
    @Bean
    public ServiceConfig serviceConfig() {
        return new FileServiceConfig();
    }
    @Bean
    public RouteConnectionServiceClient airportClient() {
       return new CachedRyanairRouteConnectionServiceClient();
    }
    @Bean
    public TimetableServiceClient timetableClient() {
       return new CachedRyanairTimetableServiceClient();
    }
    @Bean @Primary
    public TimetableServiceClient limitedConnectionTimetableClient() {
       return new LimitedConnectionTimetableServiceClient(
            timetableClient()
       );
    }
    @Bean
    public TimetableServiceClient timetableServiceClient() {
       return new CachedRyanairTimetableServiceClient();
    }
    @Bean
    public FlightSearchService directFlightSearchService() {
        return new CachedDirectFlightSearchService();
    }
    @Bean @Qualifier("direct")
    public FlightSearchService validatedDirectSearchService() {
        return new ValidatedInputFlightSearchService(
            directFlightSearchService()
        );
    }
    @Bean
    public FlightSearchService interchangeFlightSearchService() {
       return new CachedInterchangeFlightSearchService();
    }
    @Bean @Qualifier("interchange") @Primary
    public FlightSearchService validatedInterchangeSearchService() {
       return new ValidatedInputFlightSearchService(
            interchangeFlightSearchService()
       );
    }
    @Bean
    public FlightIntervalValidator flightIntervalValidator() {
        return new DefaultFlightIntervalValidator();
    }
    @Bean
    public TimetableServiceObjectMapper timetableServiceObjectMapper() {
        return new DefaultTimetableMapperService();
    }
    @Bean
    public PeriodService periodService() {
        return new DefaultPeriodService();
    }
    @Bean
    public RouteAvailabilityService routeAvailabilityService() {
        return new CachedRouteAvailabilityService();
    }
    @Bean @Primary
    public RouteAvailabilityService updatableRouteAvailabilityService() {
         return new SingleThreadUpdateRouteAvailabilityService(
            routeAvailabilityService()
         );
    }
    @Bean
    public InterconnectionService cachedInterconnectionService() {
        return new CachedInterconnectionService();
    }
}
