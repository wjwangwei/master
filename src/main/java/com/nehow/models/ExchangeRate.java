package com.nehow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExchangeRate extends BaseModel {
    @JsonProperty("currency0")
    private String fromCurrency;
    @JsonProperty("currency1")
    private String toCurrency;
    @JsonProperty("rate")
    protected double rate;
    @JsonProperty("rateTime")
    private String rateTime;
    @JsonProperty("date")
    private String date;

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getRateTime() {
        return rateTime;
    }

    public void setRateTime(String rateTime) {
        this.rateTime = rateTime;
        /*
        // string to date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
            this.rateTime = sdf.parse(rateTime);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        */
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
