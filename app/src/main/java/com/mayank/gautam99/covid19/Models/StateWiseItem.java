package com.mayank.gautam99.covid19.Models;

public class StateWiseItem {
    private String state;
    private String confirmed;
    private String confirmedNew;
    private String active;
    private String death;
    private String deathNew;
    private String recovered;
    private String recoveredNew;
    private String lastUpdate;
    private String activeNew;

    public StateWiseItem(String state, String confirmed, String confirmedNew, String active, String death, String deathNew,
                         String recovered, String recoveredNew, String lastUpdate,String activeNew) {

        this.state = state;
        this.confirmed = confirmed;
        this.confirmedNew = confirmedNew;
        this.active = active;
        this.death = death;
        this.deathNew = deathNew;
        this.recovered = recovered;
        this.recoveredNew = recoveredNew;
        this.lastUpdate = lastUpdate;
        this.activeNew = activeNew;
    }

    public String getState() {
        return state;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getConfirmedNew() {
        return confirmedNew;
    }

    public String getActive() {
        return active;
    }

    public String getDeath() {
        return death;
    }

    public String getDeathNew() {
        return deathNew;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getRecoveredNew() {
        return recoveredNew;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
