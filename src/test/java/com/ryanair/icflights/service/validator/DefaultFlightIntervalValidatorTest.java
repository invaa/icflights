package com.ryanair.icflights.service.validator;

import com.ryanair.icflights.client.TimetableServiceClient;
import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.service.flight.search.CachedInterchangeFlightSearchService;
import com.ryanair.icflights.service.flight.search.FlightSearchService;
import com.ryanair.icflights.service.interconnection.InterconnectionService;
import com.ryanair.icflights.service.interconnection.RouteAvailabilityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public final class DefaultFlightIntervalValidatorTest {
    private final LocalDateTime now = LocalDate.now().atStartOfDay();
    private final static int MAX_DURATION_OF_DATETIME_INTERVAL_HOURS = 10 * 24;
    @Configuration
    static class ContextConfiguration {
        @Bean
        @Primary
        public FlightSearchService flightSearchService() {
            return new CachedInterchangeFlightSearchService();
        }
        @Bean
        public FlightIntervalValidator flightIntervalValidator() {
            return new DefaultFlightIntervalValidator();
        }
    }
    @MockBean
    @Qualifier("direct")
    private FlightSearchService directFlightSearchServiceMock;
    @MockBean
    private RouteAvailabilityService routeAvailabilityServiceMock;
    @MockBean
    private TimetableServiceClient timetableServiceClientMock;
    @MockBean
    private InterconnectionService interconnectionServiceMock;
    @MockBean
    private ServiceConfig serviceConfigMock;
    @Autowired
    private FlightIntervalValidator flightIntervalValidator;

    @Test(expected=IllegalArgumentException.class)
    public void shouldReturnFalseForExceededDuration() throws Exception {
        final LocalDateTime departureTime = now.plusDays(3);
        final LocalDateTime arrivalTime = now.plusDays(15);
        Mockito.when(serviceConfigMock.getFlightMaxDelay())
                .thenReturn(MAX_DURATION_OF_DATETIME_INTERVAL_HOURS);
         assertFalse(
                flightIntervalValidator.isValid(
                        departureTime,
                        arrivalTime
                )
        );
    }
    @Test(expected=IllegalArgumentException.class)
    public void shouldReturnFalseForLessThanZeroMonthDuration() throws Exception {
        final LocalDateTime departureTime = now.plusMonths(1);
        final LocalDateTime arrivalTime = now.plusDays(10);
        Mockito.when(serviceConfigMock.getFlightMaxDelay())
                .thenReturn(MAX_DURATION_OF_DATETIME_INTERVAL_HOURS);
        assertFalse(
                flightIntervalValidator.isValid(
                        departureTime,
                        arrivalTime
                )
        );
    }
    @Test(expected=IllegalArgumentException.class)
    public void shouldReturnFalseForPastForDepTime() throws Exception {
        final LocalDateTime departureTime = now.minusMonths(1);
        final LocalDateTime arrivalTime = now.plusMonths(1);
        Mockito.when(serviceConfigMock.getFlightMaxDelay())
                .thenReturn(MAX_DURATION_OF_DATETIME_INTERVAL_HOURS);
        assertFalse(
                flightIntervalValidator.isValid(
                        departureTime,
                        arrivalTime
                )
        );
    }
    @Test(expected=IllegalArgumentException.class)
    public void shouldReturnFalseForPastForArrTime() throws Exception {
        final LocalDateTime departureTime = now.plusMonths(1);
        final LocalDateTime arrivalTime = now.minusMonths(1);
        Mockito.when(serviceConfigMock.getFlightMaxDelay())
                .thenReturn(MAX_DURATION_OF_DATETIME_INTERVAL_HOURS);
        assertFalse(
                flightIntervalValidator.isValid(
                        departureTime,
                        arrivalTime
                )
        );
    }
    @Test
    public void shouldReturnTrueForCorrectDuration() throws Exception {
        final LocalDateTime departureTime = now.plusMonths(1);
        final LocalDateTime arrivalTime = now.plusMonths(1).plusDays(8);
        Mockito.when(serviceConfigMock.getFlightMaxDelay())
                .thenReturn(MAX_DURATION_OF_DATETIME_INTERVAL_HOURS);
        assertTrue(
                flightIntervalValidator.isValid(
                        departureTime,
                        arrivalTime
                )
        );
    }
}