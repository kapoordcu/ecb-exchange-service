package com.capital.scalable.api;

import com.capital.scalable.domain.CurrencyEnum;
import com.capital.scalable.domain.CurrencyPair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ECBApi {

    @GetMapping(value = "/euro-rate/{currency}")
    ResponseEntity<CurrencyPair> getECBRateAsComparedToEuro(@PathVariable String currency);

    @GetMapping(value = "/other-rate/{from}/{to}")
    ResponseEntity<CurrencyPair> getExchangeRate(@PathVariable String from,
                                                              @PathVariable String to);
    @GetMapping(value = "/all-currencies")
    ResponseEntity<List<String>> getListOfCurrencies();

    @GetMapping(value = "/convert/{from}/{to}/{amount}")
    ResponseEntity<CurrencyPair> convertBetweenTwoCurrency(@PathVariable String from,
                                                            @PathVariable String to,
                                                            @PathVariable double amount);
}
