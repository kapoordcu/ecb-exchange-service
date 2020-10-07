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

import java.util.Map;
import java.util.HashMap;

@Component
public class ECBExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ECBClientConfiguration.class);
    private final ECBClientConfiguration propConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    private String lastModifiedRequestTime;

    // Will be used as a data source of the application (replacement of database or other external storage)
    private Map<String, Double> sourceData = new HashMap<>();
    public Map<String, Double> getSourceData() {
        return sourceData;
    }
    public ECBClientConfiguration getPropConfig() {
        return propConfig;
    }

    @Autowired
    public ECBExchangeClient(ECBClientConfiguration propConfig) {
        this.propConfig = propConfig;
    }

    @Bean
    public boolean callExternalECBEndpoint() {
        HttpEntity request = getHttpEntity();
        String ecbURI = propConfig.getEcbURI();

        ResponseEntity<String> ecbApiResponse = this.restTemplate.exchange(ecbURI, HttpMethod.GET,
                request, String.class, 1);
        if(ecbApiResponse.getStatusCode().is2xxSuccessful()) {
            lastModifiedRequestTime = ecbApiResponse.getHeaders().get(HttpHeaders.LAST_MODIFIED).get(0);
            saveExchangeDataIntoInMemoryStorage(ecbApiResponse.getBody());
            return true;
        } else if(HttpStatus.NOT_MODIFIED.equals(ecbApiResponse.getStatusCode())) {
            LOGGER.error(new LogMessage()
                    .with("info", "Data not modified")
                    .with("last-modified", lastModifiedRequestTime)
                    .with("HttpStatus", HttpStatus.NOT_MODIFIED).toString());
            return ecbApiResponse.getStatusCode().equals(HttpStatus.NOT_MODIFIED);
        }
        return ecbApiResponse.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    private HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.IF_MODIFIED_SINCE, lastModifiedRequestTime);
        return new HttpEntity(headers);
    }

    public void saveExchangeDataIntoInMemoryStorage(final String ECB_RATES_XML) {
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
                    .with("error", "source XML Parsing error")
                    .with("XML-data", ECB_RATES_XML)
                    .with("exception", exp).toString());
        }
    }
}
