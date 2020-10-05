package com.capital.scalable.domain;

public class CurrencyPair {
    private String from;
    private String to;
    private double amountFrom;
    private double amountTo;

    public CurrencyPair(String from, String to, double amountFrom, double amountTo) {
        this.from = from;
        this.to = to;
        this.amountFrom = amountFrom;
        this.amountTo = amountTo;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getAmountFrom() {
        return amountFrom;
    }

    public double getAmountTo() {
        return amountTo;
    }
}
