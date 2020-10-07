package com.capital.scalable.api;

import com.capital.scalable.domain.CurrencyFrequency;
import com.capital.scalable.domain.CurrencyPair;
import com.capital.scalable.domain.LogMessage;
import com.capital.scalable.infra.ECBExchangeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ECBApiImpl implements ECBApi {
    private final ECBExchangeClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(ECBApiImpl.class);
    private Map<String, Integer> frequencyMap = new HashMap<>();

    @Autowired
    public ECBApiImpl(ECBExchangeClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<CurrencyPair> getECBRateAsComparedToEuro(String currency) {
        client.callExternalECBEndpoint();
        return getExchangeRate("EUR", currency);
    }

    @Override
    public ResponseEntity<CurrencyPair> getExchangeRate(String from, String to) {
        client.callExternalECBEndpoint();
        Map<String, Double> currencyRates = client.getSourceData();
        if(currencyRates.containsKey(from.toUpperCase()) && currencyRates.containsKey(to.toUpperCase())) {
            Double c1 = currencyRates.get(from.toUpperCase());
            Double c2 = currencyRates.get(to.toUpperCase());
            double amountConverted = Math.round((c2/c1) * 100.0) / 100.0;
            CurrencyPair pair = new CurrencyPair(from.toUpperCase(), to.toUpperCase(), 1.00, amountConverted);
            LOGGER.info(new LogMessage()
                    .with("action", "/exchange-rate/{from}/{to}")
                    .with("from", from.toUpperCase())
                    .with("to", to.toUpperCase())
                    .with("amountConverted", amountConverted).toString());
            updateFrequencyMap(Arrays.asList(from, to));
            return new ResponseEntity<>(pair, HttpStatus.OK);
        }
        LOGGER.error(new LogMessage()
                .with("error", "Currency Missing")
                .with("action", "/exchange-rate/{from}/{to}")
                .with("from", from.toUpperCase())
                .with("to", to.toUpperCase()).toString());
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<CurrencyFrequency>> getListOfCurrencies() {
        client.callExternalECBEndpoint();
        return ResponseEntity.ok(client.getSourceData().entrySet()
                .stream()
                .map(cur -> new CurrencyFrequency(cur.getKey().toUpperCase(),
                        frequencyMap.getOrDefault(cur.getKey().toUpperCase(), 0)))
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<CurrencyPair> convertBetweenTwoCurrency(String from, String to, double amount) {
        client.callExternalECBEndpoint();
        Map<String, Double> currencyRates = client.getSourceData();

        if(currencyRates.containsKey(from.toUpperCase()) && currencyRates.containsKey(to.toUpperCase())) {
            Double c1 = currencyRates.get(from.toUpperCase());
            Double c2 = currencyRates.get(to.toUpperCase());
            double amountConverted = Math.round((amount*(c2/c1)) * 100.0) / 100.0;
            CurrencyPair pair = new CurrencyPair(from.toUpperCase(), to.toUpperCase(), amount, amountConverted);
            LOGGER.info(new LogMessage()
                    .with("action", "/convert/{from}/{to}/{amount}")
                    .with("amount", amount)
                    .with("from", from.toUpperCase())
                    .with("to", to.toUpperCase())
                    .with("amountConverted", amountConverted).toString());
            updateFrequencyMap(Arrays.asList(from, to));
            return new ResponseEntity<>(pair, HttpStatus.OK);
        }
        LOGGER.error(new LogMessage()
                .with("action", "/convert/{from}/{to}/{amount}")
                .with("error", "Currency Not Supported")
                .with("from", from.toUpperCase())
                .with("to", to.toUpperCase()).toString());
        return ResponseEntity.notFound().build();
    }

    @Override
    public void openCurrencyTrendInBrowser(String currency) {
        try {
            if(client.getSourceData().containsKey(currency.toUpperCase())) {
                String graphLink = String.format(client.getPropConfig().getGraphLink(), currency.toLowerCase());
                System.setProperty("java.awt.headless", "false");
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(graphLink));
            }
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(new LogMessage()
                    .with("action", "/trends/{currency}")
                    .with("Exception", e)
                    .with("currency", currency.toUpperCase()).toString());
        }
    }

    private void updateFrequencyMap(List<String> currencies) {
        currencies.stream()
                .forEach(currency -> frequencyMap.put(currency.toUpperCase(),
                        frequencyMap.getOrDefault(currency.toUpperCase(), 0) + 1));
    }
}
