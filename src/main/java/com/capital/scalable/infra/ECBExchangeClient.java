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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class ECBExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientConfiguration.class);
    private final WebClientConfiguration propConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, Double> sourceData = new HashMap<>();

    public Map<String, Double> getSourceData() {
        return sourceData;
    }

    @Autowired
    public ECBExchangeClient(WebClientConfiguration propConfig) {
        this.propConfig = propConfig;
    }

    @Bean
    public void fetchDataFromECB() {
        String ecbURI = propConfig.getEcbURI();
        final String ECB_RATES_XML = restTemplate.getForObject(ecbURI, String.class);
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
