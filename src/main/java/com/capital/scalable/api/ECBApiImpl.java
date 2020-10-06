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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ECBApiImpl implements ECBApi {
    private final ECBExchangeClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger(ECBApiImpl.class);

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
            CurrencyPair pair = new CurrencyPair(from, to, 1.00, amountConverted);
            LOGGER.info(new LogMessage()
                    .with("action", "/exchange-rate/{from}/{to}")
                    .with("from", from)
                    .with("to", to)
                    .with("amountConverted", amountConverted).toString());
            return new ResponseEntity<>(pair, HttpStatus.OK);
        }
        LOGGER.error(new LogMessage()
                .with("error", "Currency Missing")
                .with("action", "/exchange-rate/{from}/{to}")
                .with("from", from)
                .with("to", to).toString());
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<CurrencyFrequency>> getListOfCurrencies() {
        client.callExternalECBEndpoint();
        List<String> currencies = client.getSourceData().entrySet()
                .stream().map(e -> e.getKey())
                .collect(Collectors.toList());
        return null;
    }

    @Override
    public ResponseEntity<CurrencyPair> convertBetweenTwoCurrency(String from, String to, double amount) {
        client.callExternalECBEndpoint();
        Map<String, Double> currencyRates = client.getSourceData();

        if(currencyRates.containsKey(from.toUpperCase()) && currencyRates.containsKey(to.toUpperCase())) {
            Double c1 = currencyRates.get(from.toUpperCase());
            Double c2 = currencyRates.get(to.toUpperCase());
            double amountConverted = Math.round((amount*(c2/c1)) * 100.0) / 100.0;
            CurrencyPair pair = new CurrencyPair(from, to, amount, amountConverted);
            LOGGER.info(new LogMessage()
                    .with("action", "/convert/{from}/{to}/{amount}")
                    .with("amount", amount)
                    .with("from", from)
                    .with("to", to)
                    .with("amountConverted", amountConverted).toString());
            return new ResponseEntity<>(pair, HttpStatus.OK);
        }
        LOGGER.error(new LogMessage()
                .with("action", "/convert/{from}/{to}/{amount}")
                .with("error", "Currency Not Supported")
                .with("from", from)
                .with("to", to).toString());
        return ResponseEntity.notFound().build();
    }
}
