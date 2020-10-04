package com.capital.scalable.api;

import com.capital.scalable.domain.CurrencyEnum;
import com.capital.scalable.domain.CurrencyPair;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ECBApiImpl implements ECBApi {

    @Override
    public ResponseEntity<CurrencyPair> getECBRateAsComparedToEuro(String currency) {
        if(ratesChanged()) {

        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CurrencyPair> getExchangeRate(String from, String to) {
        if(ratesChanged()) {

        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CurrencyEnum>> getListOfCurrencies() {
        if(ratesChanged()) {

        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<CurrencyPair> convertBetweenTwoCurrency(String from, String to, double amount) {
        if(ratesChanged()) {

        }
        return ResponseEntity.ok().build();
    }


    private boolean ratesChanged() {

        return false;
    }
}
