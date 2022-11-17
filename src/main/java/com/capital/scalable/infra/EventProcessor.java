package com.capital.scalable.infra;

import com.capital.scalable.domain.LogMessage;
import com.capital.scalable.domain.pkgeneration.CombiKey;
import com.capital.scalable.domain.pkgeneration.CombiValue;
import com.capital.scalable.domain.xmltojson.Event;
import com.capital.scalable.domain.xmltojson.RootEvent;
import com.capital.scalable.domain.xmltojson.Zone;
import com.capital.scalable.repository.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class EventProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventProcessor.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final EventRepository eventRepository;

    @Autowired
    public EventProcessor(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    public void processXML(final String EVENTS_XML, String rootNode, String outputNode, String baseEventNode) {
        List<RootEvent> fetchedEventsFromProvider = new ArrayList<>();
        try {
            JSONArray eventsJson = XML.toJSONObject(EVENTS_XML)
                    .getJSONObject(rootNode)
                    .getJSONObject(outputNode)
                    .getJSONArray(baseEventNode);
            for (int i = 0; i < eventsJson.length(); i++) {
                JSONObject element = eventsJson.getJSONObject(i);
                fetchedEventsFromProvider.add(mapper.readValue(element.toString(), RootEvent.class));
            }
        } catch (JSONException exp) {
            LOGGER.error(new LogMessage()
                    .with("error", "source XML Parsing error")
                    .with("XML-data", EVENTS_XML)
                    .with("exception", exp).toString());
        } catch (JsonProcessingException processingException) {
            LOGGER.error(new LogMessage()
                    .with("exception", "JSON mapping exception in object mapper")
                    .with("json-data", EVENTS_XML)
                    .with("exception", processingException).toString());
        }
        generateUUIDfromEventsAndSaveInDB(fetchedEventsFromProvider);
    }

    private void generateUUIDfromEventsAndSaveInDB(List<RootEvent> fetchedEventsFromProvider) {
        for (RootEvent root:     fetchedEventsFromProvider) {
            String base_event_id = root.base_event_id;
            if(root.getEvent() != null) {
                Event event = root.getEvent();
                List<Zone> zones = event.getZone();
                for (Zone zone:                 zones) {
                    String zone_id = zone.zone_id;
                    boolean numbered = zone.numbered;
                    CombiKey key = new CombiKey(base_event_id, zone_id, numbered);
                    CombiValue value = new CombiValue(zone.capacity, zone.price, root.title, root.sell_mode,
                            event.event_start_date, event.event_end_date);
                    eventRepository.saveEvent(UUID.fromString(key.toString()), null);
                }
            }
        }
    }
}
