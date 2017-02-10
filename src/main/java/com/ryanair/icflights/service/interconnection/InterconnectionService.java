package com.ryanair.icflights.service.interconnection;

import com.ryanair.icflights.model.Pair;
import com.ryanair.icflights.model.PairOfStrings;
import java.util.Set;

/**
 * Get the 1 stop connection between a two IATA airport codes.
 */
public interface InterconnectionService {
    Set<
            Pair<PairOfStrings, PairOfStrings>
    > getInterconnections(final String from, final String to);
}
