package com.ryanair.icflights.client.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ryanair.icflights.dto.FlightsTimetableDto;

import java.io.IOException;

public final class FlightsTimetableDtoMapper
        extends JsonDeserializer<FlightsTimetableDto> {
    @Override
    public FlightsTimetableDto deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context
    ) throws IOException {
        final ObjectCodec oc = jsonParser.getCodec();
        final JsonNode node = oc.readTree(jsonParser);
        return new FlightsTimetableDto(
                node.get("number").asText(),
                node.get("departureTime").asText(),
                node.get("arrivalTime").asText()
        );
    }
}
