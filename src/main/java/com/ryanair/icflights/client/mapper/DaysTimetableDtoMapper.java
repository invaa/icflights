package com.ryanair.icflights.client.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.icflights.dto.DaysTimetableDto;
import com.ryanair.icflights.dto.FlightsTimetableDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DaysTimetableDtoMapper
        extends JsonDeserializer<DaysTimetableDto> {
    @Override
    public DaysTimetableDto deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context
    ) throws IOException {
        final ObjectCodec oc = jsonParser.getCodec();
        final JsonNode node = oc.readTree(jsonParser);
        final List<FlightsTimetableDto> flightsTimetableDtos = new ArrayList<>();
        for (JsonNode flightsNode: node.get("flights")) {
            JsonParser parser = flightsNode.traverse();
            parser.setCodec(jsonParser.getCodec());
            ObjectMapper mapper = new ObjectMapper();
            flightsTimetableDtos.add(mapper.readValue(parser, FlightsTimetableDto.class));
        }
        return new DaysTimetableDto(
                node.get("day").asInt(),
                flightsTimetableDtos
        );
    }
}
