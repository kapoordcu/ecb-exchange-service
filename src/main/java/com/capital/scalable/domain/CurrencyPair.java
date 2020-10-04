package com.capital.scalable.domain;

public class CurrencyPair {
    private String from;
    private String to;
    private double amountFrom;
    private double amountTo;

    public CurrencyPair(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
