package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.model.Airport;
import com.ryanair.icflights.model.Pair;
import com.ryanair.icflights.model.PairOfStrings;
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
public final class CachedInterconnectionServiceTest {
    @Configuration
    static class ContextConfiguration {
        @Bean
        public InterconnectionService routeAvailabilityService() {
            return new CachedInterconnectionService();
        }
    }
    @MockBean
    private RouteAvailabilityService routeAvailabilityServiceMock;
    @Autowired
    private InterconnectionService cachedInterconnectionService;

    @Test
    public void shouldReturnWroStnDubInterconnection() throws Exception {
        final Map<String, Airport> mockAirportsResult = new HashMap<>();
        mockAirportsResult.put("WRO",
                new Airport(
                        "WRO",
                        Collections.emptySet(),
                        new HashSet<String>() {{add("STN");}}
                )
        );
        mockAirportsResult.put("DUB",
                new Airport(
                        "DUB",
                        new HashSet<String>() {{add("STN");}},
                        Collections.emptySet()
                )
        );
        mockAirportsResult.put("STN",
                new Airport(
                        "STN",
                        new HashSet<String>() {{add("WRO");}},
                        new HashSet<String>() {{add("DUB");}}
                )
        );
        Mockito.when(routeAvailabilityServiceMock.getAirports())
                .thenReturn(mockAirportsResult);
        final Set<Pair<PairOfStrings, PairOfStrings>> expectedIc =
                new HashSet<Pair<PairOfStrings, PairOfStrings>>() {{
                    add(new Pair<>(
                            new PairOfStrings("WRO","STN"),
                            new PairOfStrings("STN","DUB")
                    ));
                }};
        final Set<Pair<PairOfStrings, PairOfStrings>> actualIc =
            cachedInterconnectionService.getInterconnections("WRO", "DUB");
        assertReflectionEquals(
                expectedIc,
                actualIc,
                ReflectionComparatorMode.LENIENT_ORDER
        );
    }
    @Test
    public void shouldNotReturnDubStnWroIfNoReturnConnection() throws Exception {
        final Map<String, Airport> mockAirportsResult = new HashMap<>();
        mockAirportsResult.put("WRO",
                new Airport(
                        "WRO",
                        Collections.emptySet(),
                        new HashSet<String>() {{add("STN");}}
                )
        );
        mockAirportsResult.put("DUB",
                new Airport(
                        "DUB",
                        new HashSet<String>() {{add("STN");}},
                        Collections.emptySet()
                )
        );
        mockAirportsResult.put("STN",
                new Airport(
                        "STN",
                        new HashSet<String>() {{add("WRO");}},
                        new HashSet<String>() {{add("DUB");}}
                )
        );
        Mockito.when(routeAvailabilityServiceMock.getAirports())
                .thenReturn(mockAirportsResult);
        final Set<Pair<PairOfStrings, PairOfStrings>> expectedIc = Collections.emptySet();
        final Set<Pair<PairOfStrings, PairOfStrings>> actualIc =
                cachedInterconnectionService.getInterconnections("DUB", "WRO");
        assertReflectionEquals(
                expectedIc,
                actualIc,
                ReflectionComparatorMode.LENIENT_ORDER
        );
    }
}