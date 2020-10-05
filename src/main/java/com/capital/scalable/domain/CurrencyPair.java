package com.capital.scalable.domain;

public class CurrencyPair {
    private String fromCurrency;
    private String toCurrency;
    private double fromAmount;
    private double toAmount;

    public CurrencyPair(String fromCurrency, String toCurrency, double fromAmount, double toAmount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }
}
