package com.mayank.gautam99.covid19.Models;

public class VaccinationModel {
    String centerName, centerAddress, centerFromTime, centerToTime, fee_type, ageLimit, vaccineName,availableCapacity;

    public VaccinationModel(String centerName, String centerAddress, String centerFromTime, String centerToTime,
                            String fee_type, String ageLimit, String vaccineName, String availableCapacity) {
        this.centerName = centerName;
        this.centerAddress = centerAddress;
        this.centerFromTime = centerFromTime;
        this.centerToTime = centerToTime;
        this.fee_type = fee_type;
        this.ageLimit = ageLimit;
        this.vaccineName = vaccineName;
        this.availableCapacity = availableCapacity;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public String getCenterFromTime() {
        return centerFromTime;
    }

    public String getCenterToTime() {
        return centerToTime;
    }

    public String getFee_type() {
        return fee_type;
    }

    public String getAgeLimit() {
        return ageLimit;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public String getAvailableCapacity() {
        return availableCapacity;
    }
}
