package com.ryanair.icflights.model;

import java.util.Collections;
import java.util.Set;

/**
 * Airport plain object.
 */
public final class Airport {
    /**
     * IATA airport code.
     */
    private final String name;
    /**
     * Airport IATA codes which have connection to current.
     */
    private final Set<String> connectionsTo;
    /**
     * Airport IATA codes the flights exist to from current aiport.
     */
    private final Set<String> connectionsFrom;

    public Airport(
            final String name,
            final Set<String> connectionsTo,
            final Set<String> connectionsFrom
    ) {
        this.name = name;
        this.connectionsTo = connectionsTo == null ?
                Collections.emptySet()
                : Collections.unmodifiableSet(connectionsTo);
        this.connectionsFrom = connectionsFrom == null ?
                Collections.emptySet()
                : Collections.unmodifiableSet(connectionsFrom);
    }
    public String getName() {
        return name;
    }
    public Set<String> getConnectionsTo() {
        return this.connectionsTo;
    }
    public Set<String> getConnectionsFrom() {
        return this.connectionsFrom;
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Airport airport = (Airport) o;
        return name != null ? name.equals(airport.name) : airport.name == null;
    }
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
