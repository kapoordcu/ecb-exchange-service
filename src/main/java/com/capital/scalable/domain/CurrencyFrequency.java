package com.capital.scalable.domain;

public class CurrencyFrequency {
    private String currency;
    private int accessCount;

    public CurrencyFrequency(String currency, int accessCount) {
        this.currency = currency;
        this.accessCount = accessCount;
    }

    public String getCurrency() {
        return currency;
    }

    public int getAccessCount() {
        return accessCount;
    }
}
