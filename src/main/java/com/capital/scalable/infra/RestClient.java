package com.capital.scalable.infra;

import com.capital.scalable.domain.LogMessage;
import com.capital.scalable.domain.xmltojson.RootEvent;
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
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientConfig.class);
    private final ClientConfig propConfig;
    private final EventProcessor processor;
    private String lastModifiedSince;

    private HttpEntity request = getHttpEntity();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public RestClient(ClientConfig propConfig, EventProcessor processor) {
        this.propConfig = propConfig;
        this.processor = processor;
    }

    @Bean
    public void fetchEventsOnStartUp() {
        LOGGER.info(new LogMessage()
                        .with("info", "Events from external service provider fetched")
                        .with("Trigger-Action", "Application started").toString());
        invokeExternalProvider();
    }

    @Scheduled(cron = "${provider.api.schedule.frequency}")
    public void invokeExternalProvider() {
        String newEventsFromProvider = propConfig.getExternalEvents();
        ResponseEntity<String> externalEventsApiResponse = this.restTemplate.exchange(newEventsFromProvider, HttpMethod.GET,
                request, String.class, 1);

        if(externalEventsApiResponse.getStatusCode().is2xxSuccessful()) {
            lastModifiedSince = externalEventsApiResponse.getHeaders().get(HttpHeaders.DATE).get(0);
            processor.processXML(externalEventsApiResponse.getBody(), propConfig.getRootNode()
                    , propConfig.getOutputNode(), propConfig.getBaseEventNode());
        } else if(HttpStatus.NOT_MODIFIED.equals(externalEventsApiResponse.getStatusCode())) {
            LOGGER.info(new LogMessage()
                    .with("info", "the events have not changed in the provider")
                    .with("HttpStatus", externalEventsApiResponse.getStatusCode()).toString());
        }
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_MODIFIED_SINCE, lastModifiedSince);
        return new HttpEntity(headers);
    }
}
