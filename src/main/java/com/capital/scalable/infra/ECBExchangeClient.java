package com.capital.scalable.infra;

import com.capital.scalable.domain.LogMessage;
import com.capital.scalable.domain.dto.ECBData;
import org.json.JSONException;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ECBExchangeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientConfiguration.class);
    private final WebClientConfiguration propConfig;
    private ECBData ecbData;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public ECBExchangeClient(WebClientConfiguration propConfig) {
        this.propConfig = propConfig;
    }

    @Bean
    public void fetchDataFromECB() {
        String ecbURI = propConfig.getEcbURI();
        final String ECB_RATES_XML = restTemplate.getForObject(ecbURI, String.class);
        try {
            XML.toJSONObject(ECB_RATES_XML)
                    .getJSONObject(propConfig.getRootElement())
                    .getJSONObject(propConfig.getDataElement())
                    .getJSONObject(propConfig.getDataElement())
                    .get(propConfig.getDataElement());
            System.out.println();
        } catch (JSONException exp) {
            LOGGER.error(new LogMessage()
                    .with("error", "XML Parsing error")
                    .with("XML-data", ECB_RATES_XML)
                    .with("exception", exp).toString());
        }
    }
}
