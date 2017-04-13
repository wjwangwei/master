package com.nehow.models;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/13/17.
 */
public class PriceBreakDown {
    private String date;
    private String currency;
    private String rateCode;
    private String rate;
    private String originalRate;
    private String adultRate;
    private String childRates;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOriginalRate() {
        return originalRate;
    }

    public void setOriginalRate(String originalRate) {
        this.originalRate = originalRate;
    }

    public String getAdultRate() {
        return adultRate;
    }

    public void setAdultRate(String adultRate) {
        this.adultRate = adultRate;
    }

    public String getChildRates() {
        return childRates;
    }

    public void setChildRates(String childRates) {
        this.childRates = childRates;
    }
}
