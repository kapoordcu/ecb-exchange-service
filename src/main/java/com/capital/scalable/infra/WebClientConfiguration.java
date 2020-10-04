package com.capital.scalable.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {
    @Value("${client.ecb.daily-rate-uri:https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml}")
    private String ecbURI;

    @Value("${client.ecb.root-element:'gesmes:Envelope'}")
    private String rootElement;

    @Value("${client.ecb.data-element:Cube}")
    private String dataElement;

    public String getEcbURI() {
        return ecbURI;
    }

    public String getRootElement() {
        return rootElement;
    }

    public String getDataElement() {
        return dataElement;
    }
}