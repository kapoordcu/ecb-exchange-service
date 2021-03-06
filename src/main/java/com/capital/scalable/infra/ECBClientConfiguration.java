package com.capital.scalable.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ECBClientConfiguration {
    @Value("${client.ecb.daily-rate-uri:https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml}")
    private String ecbURI;

    @Value("${client.ecb.root-element:'gesmes:Envelope'}")
    private String rootNode;

    @Value("${client.ecb.data-element:Cube}")
    private String dataNode;

    @Value("${client.ecb.rate:rate}")
    private String rateLabel;

    @Value("${client.ecb.currency:currency}")
    private String currencyLabel;

    @Value("${client.ecb.base-currency:EUR}")
    private String baseCurrency;

    @Value("${client.ecb.graph:https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/eurofxref-graph-%s.en.html}")
    private String graphLink;

    public String getEcbURI() {
        return ecbURI;
    }

    public String getRootNode() {
        return rootNode;
    }

    public String getDataNode() {
        return dataNode;
    }

    public String getRateLabel() {
        return rateLabel;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getGraphLink() {
        return graphLink;
    }
}