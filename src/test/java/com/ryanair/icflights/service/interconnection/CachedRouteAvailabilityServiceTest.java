package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.client.RouteConnectionServiceClient;
import com.ryanair.icflights.dto.RouteDto;
import com.ryanair.icflights.model.Airport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.unitils.reflectionassert.ReflectionComparatorMode;

import java.util.*;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public final class CachedRouteAvailabilityServiceTest {
    @Configuration
    static class ContextConfiguration {
     @Bean
        public RouteAvailabilityService routeAvailabilityService() {
            return new CachedRouteAvailabilityService();
        }
    }
    @MockBean
    private RouteConnectionServiceClient routeConnectionServiceClientMock;
    @Autowired
    private RouteAvailabilityService cachedRouteAvailabilityService;

    @Test
    public void shouldReturnEmaVlcAndVlcEmaforEmaVlc() throws Exception {
        Mockito.when(routeConnectionServiceClientMock.getRoutes())
                .thenReturn(
                        Collections.singletonList(
                                new RouteDto("EMA", "VLC")
                        )
                );
        final Map<String, Airport> actual = cachedRouteAvailabilityService.getAirports();
        final Map<String, Airport> expected = new HashMap<>();
        expected.put("VLC",
                new Airport(
                        "VLC",
                        new HashSet<String>() {{add("EMA");}},
                        Collections.emptySet()
                )
        );
        expected.put("EMA",
                new Airport(
                        "EMA",
                        Collections.emptySet(),
                        new HashSet<String>() {{add("VLC");}}
                )
        );
        for (Map.Entry<String, Airport> entry : expected.entrySet()) {
            assertReflectionEquals(
                    entry.getValue(),
                    actual.get(entry.getKey()),
                    ReflectionComparatorMode.LENIENT_ORDER
            );
        }
    }
    @Test
    public void shouldReturnThreeAirportsWhenRoutesFromAndTo() throws Exception {
        Mockito.when(routeConnectionServiceClientMock.getRoutes())
                .thenReturn(
                        Arrays.asList(
                                new RouteDto("WRO", "STN"),
                                new RouteDto("STN", "DUB")
                        )
                );
        final Map<String, Airport> actual = cachedRouteAvailabilityService.getAirports();
        final Map<String, Airport> expected = new HashMap<>();
        expected.put("WRO",
                new Airport(
                        "WRO",
                        Collections.emptySet(),
                        new HashSet<String>() {{add("STN");}}
                )
        );
        expected.put("DUB",
                new Airport(
                        "DUB",
                        new HashSet<String>() {{add("STN");}},
                        Collections.emptySet()
                )
        );
        expected.put("STN",
                new Airport(
                        "STN",
                        new HashSet<String>() {{add("WRO");}},
                        new HashSet<String>() {{add("DUB");}}
                )
        );
        for (Map.Entry<String, Airport> entry : expected.entrySet()) {
            assertReflectionEquals(
                    entry.getValue(),
                    actual.get(entry.getKey()),
                    ReflectionComparatorMode.LENIENT_ORDER
            );
        }
    }
}