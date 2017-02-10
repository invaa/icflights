package com.ryanair.icflights.service.flight.search;

import com.ryanair.icflights.client.TimetableServiceClient;
import com.ryanair.icflights.config.ServiceConfig;
import com.ryanair.icflights.model.*;
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
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public final class CachedInterchangeFlightSearchServiceTest {
    private final LocalDateTime now = LocalDate.now().atStartOfDay();

    @Configuration
    static class ContextConfiguration {
        @Bean
        @Primary
        public FlightSearchService flightSearchService() {
            return new CachedInterchangeFlightSearchService();
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
    private FlightSearchService cachedInterchangeFlightSearchService;

    @Test
    public void shouldReturnDirectAndIndirectFlights() throws Exception {
        final LocalDateTime departureTime = now.plusHours(10);
        final LocalDateTime arrivalTime = now.plusMonths(1);
        final LocalDateTime dirDepartureTime = now.plusDays(1);
        final LocalDateTime dirArrivalTime = now.plusDays(1).plusHours(2);
        final LocalDateTime icLegOneDepartureTime = now.plusDays(3);
        final LocalDateTime icLegOneArrivalTime = now.plusDays(3).plusHours(3);
        final LocalDateTime icLegTwoDepartureTime = now.plusDays(7);
        final LocalDateTime icLegTwoArrivalTime = now.plusDays(7).plusHours(1);
        final String from = "WRO";
        final String to = "DUB";
        final String ic = "STN";
        Mockito.when(serviceConfigMock.getFlightMinDelay())
                .thenReturn(2);
        mapFlightToDirectFlightMock(departureTime, arrivalTime, dirDepartureTime, dirArrivalTime, from, to);
        mapFlightToDirectFlightMock(departureTime, arrivalTime, icLegOneDepartureTime, icLegOneArrivalTime, from, ic);
        mapFlightToDirectFlightMock(icLegOneArrivalTime.plusHours(
                serviceConfigMock.getFlightMinDelay()), arrivalTime, icLegTwoDepartureTime, icLegTwoArrivalTime, ic, to);
        final Set<Pair<PairOfStrings, PairOfStrings>> interconnections =
                new HashSet<Pair<PairOfStrings, PairOfStrings>>() {{
                    add(new Pair<>(
                            new PairOfStrings(from, ic),
                            new PairOfStrings(ic, to)
                    ));
                }};
        Mockito.when(interconnectionServiceMock.getInterconnections(from, to))
                .thenReturn(interconnections);
        List<Flight> expectedFlights =
                Arrays.asList(
                        getFlight(0, getLeg(from, to, dirDepartureTime, dirArrivalTime)),
                        getFlight(1, getLeg(from, ic, icLegOneDepartureTime, icLegOneArrivalTime),
                                getLeg(ic, to, icLegTwoDepartureTime, icLegTwoArrivalTime))

                );
        List<Flight> actualFlights = cachedInterchangeFlightSearchService.getFlights(
                from,
                to,
                departureTime,
                arrivalTime
        );
        Iterator iterator = actualFlights.iterator();
        for (Flight expectedFlight : expectedFlights) {
            Flight actualFlight = (Flight) iterator.next();
            assertEquals(expectedFlight.getStops(), actualFlight.getStops());
            assertReflectionEquals(
                    expectedFlight.getLegs(),
                    actualFlight.getLegs(),
                    ReflectionComparatorMode.LENIENT_ORDER
            );
        }
    }
    private FlightLeg getLeg(
            final String from,
            final String to,
            final LocalDateTime departureTime,
            final LocalDateTime arrivalTime
    ) {
        return
                new FlightLeg(
                        from,
                        to,
                        departureTime,
                        arrivalTime
                );
    }
    private Flight getFlight(
            final int stops,
            final FlightLeg... legs
    ) {
        return new Flight(
                stops,
                Arrays.asList(legs)
        );
    }
    private void mapFlightToDirectFlightMock(
            final LocalDateTime departureTime,
            final LocalDateTime arrivalTime,
            final LocalDateTime flightDepartureTime,
            final LocalDateTime flightArrivalTime,
            final String from,
            final String to
    ) {
        Mockito.when(directFlightSearchServiceMock
                .getFlights(from,
                        to,
                        departureTime,
                        arrivalTime))
                .thenReturn(
                        Collections.singletonList(
                                new Flight(
                                        0,
                                        Collections.singletonList(
                                                new FlightLeg(
                                                        from,
                                                        to,
                                                        flightDepartureTime,
                                                        flightArrivalTime
                                                )
                                        )
                                )
                        )
                );
    }
}