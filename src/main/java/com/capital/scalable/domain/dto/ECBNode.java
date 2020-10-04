package com.capital.scalable.domain.dto;

public class ECBNode {
    private double rate;
    private String currency;

    public ECBNode(double rate, String currency) {
        this.rate = rate;
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public String getCurrency() {
        return currency;
    }
}
