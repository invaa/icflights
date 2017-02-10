package com.ryanair.icflights.client;

import com.ryanair.icflights.config.ClientConfig;
import com.ryanair.icflights.dto.MonthTimetableDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Gets ryanair specific structure routes from url.
 * Caches responses.
 */
public final class LimitedConnectionTimetableServiceClient
        implements TimetableServiceClient {
    private static final Logger log =
            LoggerFactory.getLogger(LimitedConnectionTimetableServiceClient.class);
    @Autowired
    private ClientConfig clientConfig;
    private final TimetableServiceClient timetableServiceClient;
    private ExecutorService executor;

    public LimitedConnectionTimetableServiceClient(
            final TimetableServiceClient timetableServiceClient) {
        this.timetableServiceClient = timetableServiceClient;
    }
    @PostConstruct
    public void init() {
        this.executor = Executors.newWorkStealingPool(
                clientConfig.getTimetableServiceMaxConnections()
        );
    }
    @PreDestroy
    public void destroy() {
        this.executor.shutdownNow();
    }
    @Override
    public MonthTimetableDto getMonthTimetable(
            final String from,
            final String to,
            final int year,
            final int month
    ) {
        Future<MonthTimetableDto> monthTimetableDtoFuture
                = this.executor.submit(
                    () -> this.timetableServiceClient
                            .getMonthTimetable(
                                    from,
                                    to,
                                    year,
                                    month
                            )
                );
        try {
            return monthTimetableDtoFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.debug("Error during receiving data from timetable client.", e);
        }
        return new MonthTimetableDto(0, Collections.emptyList());
    }
}
