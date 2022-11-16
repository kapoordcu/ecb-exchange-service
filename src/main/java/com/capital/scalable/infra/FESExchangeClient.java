package com.capital.scalable.infra;

import com.capital.scalable.dto.LogMessage;
import com.capital.scalable.repository.EventRepository;
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

@Component
public class FESExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FESClientConfiguration.class);
    private final FESClientConfiguration propConfig;

    @Autowired
    private EventRepository eventRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private String lastModifiedSince;

    public FESClientConfiguration getPropConfig() {
        return propConfig;
    }

    @Autowired
    public FESExchangeClient(FESClientConfiguration propConfig) {
        this.propConfig = propConfig;
    }

    @Scheduled(cron = "${provider.api.schedule.frequency}")
    public void invokeExternalProvider() {
        long now = System.currentTimeMillis() / 1000;
        System.out.println("schedule tasks using cron jobs - " + now);
    }

    @Bean
    public boolean callExternalEventsEndpoint() {
        HttpEntity request = getHttpEntity();
        String externalEvents = propConfig.getExternalEvents();
        ResponseEntity<String> externalEventsApiResponse = this.restTemplate.exchange(externalEvents, HttpMethod.GET,
                request, String.class, 1);

        if(externalEventsApiResponse.getStatusCode().is2xxSuccessful()) {
            lastModifiedSince = externalEventsApiResponse.getHeaders().get(HttpHeaders.DATE).get(0);
            saveEventsDataToRedis(externalEventsApiResponse.getBody());
            return true;
        } else if(HttpStatus.NOT_MODIFIED.equals(externalEventsApiResponse.getStatusCode())) {
            return externalEventsApiResponse.getStatusCode().equals(HttpStatus.NOT_MODIFIED);
        }
        return externalEventsApiResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_MODIFIED_SINCE, lastModifiedSince);
        return new HttpEntity(headers);
    }

    public void saveEventsDataToRedis(final String EVENTS_XML) {
        try {
            JSONObject eventOutput = XML.toJSONObject(EVENTS_XML)
                    .getJSONObject(propConfig.getRootNode())
                    .getJSONObject(propConfig.getOutputNode());
//            for (int i = 0; i < scores.length(); i++) {
//                JSONObject element = scores.getJSONObject(i);
//                sourceData.put( element.getString(propConfig.getCurrencyLabel()),
//                        element.getDouble(propConfig.getRateLabel()));
//            }
//            sourceData.put(propConfig.getBaseCurrency(), 1.00);
            System.out.println(eventOutput);
        } catch (JSONException exp) {
            LOGGER.error(new LogMessage()
                    .with("error", "source XML Parsing error")
                    .with("XML-data", EVENTS_XML)
                    .with("exception", exp).toString());
        }
    }
}
