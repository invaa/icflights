package com.ryanair.icflights.dto;

import java.util.Collections;
import java.util.Map;

public final class ErrorDto {
    private final int status;
    private final Map<String, Object> message;

    public ErrorDto(
            final int status,
            final Map<String, Object> message
    ) {
        this.status = status;
        this.message = Collections.unmodifiableMap(
                message
        );
    }
    public int getStatus() {
        return this.status;
    }
    public Map<String, Object> getMessage() {
        return this.message;
    }
}
