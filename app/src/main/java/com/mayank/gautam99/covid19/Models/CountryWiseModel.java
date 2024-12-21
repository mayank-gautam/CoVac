package com.mayank.gautam99.covid19.Models;

public class CountryWiseModel {
    private String country;
    private String confirmed;
    private String newConfirmed;
    private String active;
    private String decreased;
    private String newDecreased;
    private String recovered;

    private String newRecovered;
    private String tests;
    private String flag;

    public CountryWiseModel(String country, String confirmed, String newConfirmed, String active, String decreased,
                            String newDecreased, String recovered, String tests, String flag,String newRecovered) {
        this.country = country;
        this.confirmed = confirmed;
        this.newConfirmed = newConfirmed;
        this.active = active;
        this.decreased = decreased;
        this.newDecreased = newDecreased;
        this.recovered = recovered;
        this.tests = tests;
        this.flag = flag;
        this.newRecovered = newRecovered;
    }

    public String getNewRecovered() {
        return newRecovered;
    }
    public String getCountry() {
        return country;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public String getActive() {
        return active;
    }

    public String getDecreased() {
        return decreased;
    }

    public String getNewDecreased() {
        return newDecreased;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getTests() {
        return tests;
    }

    public String getFlag() {
        return flag;
    }
}
