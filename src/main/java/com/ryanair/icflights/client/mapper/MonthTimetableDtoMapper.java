package com.ryanair.icflights.client.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanair.icflights.dto.DaysTimetableDto;
import com.ryanair.icflights.dto.MonthTimetableDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonthTimetableDtoMapper
        extends JsonDeserializer<MonthTimetableDto> {
    private static final Logger log =
            LoggerFactory.getLogger(MonthTimetableDtoMapper.class);

    @Override
    public MonthTimetableDto deserialize(
            final JsonParser jsonParser,
            final DeserializationContext context
    ) throws IOException {
        final ObjectCodec oc = jsonParser.getCodec();
        final JsonNode node = oc.readTree(jsonParser);
        final List<DaysTimetableDto> daysTimetableDtos = new ArrayList<>();
        for (JsonNode daysNode: node.get("days")) {
            JsonParser parser = daysNode.traverse();
            parser.setCodec(jsonParser.getCodec());
            ObjectMapper mapper = new ObjectMapper();
            daysTimetableDtos.add(mapper.readValue(parser, DaysTimetableDto.class));
        }
        return new MonthTimetableDto(
                node.get("month").asInt(),
                daysTimetableDtos
        );
    }
}
