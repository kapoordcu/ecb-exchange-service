package com.capital.scalable.infra;

import com.capital.scalable.domain.LogMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ECBExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientConfiguration.class);
    private final WebClientConfiguration propConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Double> sourceData = new HashMap<>();
    private Date lastModified = new Date();

    public Map<String, Double> getSourceData() {
        return sourceData;
    }

    @Autowired
    public ECBExchangeClient(WebClientConfiguration propConfig) {
        this.propConfig = propConfig;
    }

    @Bean
    public boolean fetchDataFromECB() {
        String ecbURI = propConfig.getEcbURI();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_MODIFIED_SINCE, lastModified.toString());
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> ecbApiResponse = this.restTemplate.exchange(ecbURI, HttpMethod.GET, request, String.class, 1);
        if(ecbApiResponse.getStatusCode().is2xxSuccessful()) {
            prepareMapDataFromECBWebsite(ecbApiResponse.getBody());
            lastModified = new Date();
            return true;
        }
        return ecbApiResponse.getStatusCode().equals(HttpStatus.NOT_MODIFIED);
    }

    public void prepareMapDataFromECBWebsite(final String ECB_RATES_XML) {
        sourceData.clear();
        try {
            JSONArray scores = (JSONArray) XML.toJSONObject(ECB_RATES_XML)
                    .getJSONObject(propConfig.getRootNode())
                    .getJSONObject(propConfig.getDataNode())
                    .getJSONObject(propConfig.getDataNode())
                    .get(propConfig.getDataNode());
            for (int i = 0; i < scores.length(); i++) {
                JSONObject element = scores.getJSONObject(i);
                sourceData.put( element.getString(propConfig.getCurrencyLabel()),
                        element.getDouble(propConfig.getRateLabel()));
            }
            sourceData.put(propConfig.getBaseCurrency(), 1.00);
        } catch (JSONException exp) {
            LOGGER.error(new LogMessage()
                    .with("error", "XML Parsing error")
                    .with("XML-data", ECB_RATES_XML)
                    .with("exception", exp).toString());
        }
    }
}
