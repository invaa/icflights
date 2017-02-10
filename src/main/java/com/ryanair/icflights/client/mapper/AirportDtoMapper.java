package com.ryanair.icflights.client.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.ryanair.icflights.dto.RouteDto;

import java.io.IOException;

public final class AirportDtoMapper
        extends JsonDeserializer<RouteDto> {
    @Override
    public RouteDto deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context
    ) throws IOException {
        final ObjectCodec oc = jsonParser.getCodec();
        final JsonNode node = oc.readTree(jsonParser);
        return new RouteDto(
                node.get("airportFrom").asText(),
                node.get("airportTo").asText()
        );
    }
}
