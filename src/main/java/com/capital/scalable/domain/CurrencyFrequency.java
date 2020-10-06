package com.capital.scalable.domain;

public class CurrencyFrequency {
    private String currency;
    private int requestFrequency;

    public CurrencyFrequency(String currency, int requestFrequency) {
        this.currency = currency;
        this.requestFrequency = requestFrequency;
    }

    public String getCurrency() {
        return currency;
    }

    public int getRequestFrequency() {
        return requestFrequency;
    }
}
