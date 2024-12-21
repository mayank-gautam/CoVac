package com.mayank.gautam99.covid19.Models;

public class DistrictWiseModel {
    private String district;
    private String confirmed;
    private String active;
    private String recovered;
    private String decreased;
    private String newConfirmed;
    private String newRecovered;
    private String newDecreased;

    public DistrictWiseModel(String district, String confirmed, String active, String recovered, String decreased,
                             String newConfirmed, String newRecovered, String newDecreased) {
        this.district = district;
        this.confirmed = confirmed;
        this.active = active;
        this.recovered = recovered;
        this.decreased = decreased;
        this.newConfirmed = newConfirmed;
        this.newRecovered = newRecovered;
        this.newDecreased = newDecreased;
    }

    public String getDistrict() {
        return district;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getActive() {
        return active;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getDecreased() {
        return decreased;
    }

    public String getNewConfirmed() {
        return newConfirmed;
    }

    public String getNewRecovered() {
        return newRecovered;
    }

    public String getNewDecreased() {
        return newDecreased;
    }
}
