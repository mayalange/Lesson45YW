package org.example.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Currency {
    private String currency;

    public Currency() {}

    public Currency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}