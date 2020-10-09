package com.capital.scalable.api;

import com.capital.scalable.domain.CurrencyFrequency;
import com.capital.scalable.domain.CurrencyPair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.List;

public interface ECBApi {

    @GetMapping(value = "/euro-rate/{currency}")
    ResponseEntity<CurrencyPair> getECBRateAsComparedToEuro(@PathVariable String currency);

    @GetMapping(value = "/exchange-rate/{from}/{to}")
    ResponseEntity<CurrencyPair> getExchangeRate(@PathVariable String from,
                                                 @PathVariable String to);

    @GetMapping(value = "/all-currencies")
    ResponseEntity<List<CurrencyFrequency>> getListOfCurrencies();

    @GetMapping(value = "/convert/{from}/{to}/{amount}")
    ResponseEntity<CurrencyPair> convertBetweenTwoCurrency(@PathVariable String from,
                                                            @PathVariable String to,
                                                            @PathVariable double amount);

    @GetMapping(value = "/trends/{currency}")
    URI openCurrencyTrendInBrowser(@PathVariable String currency);
}
