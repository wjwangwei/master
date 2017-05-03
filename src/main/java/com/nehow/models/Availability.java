package com.nehow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class Availability {
    private String supplierId;
    private String currency;

    private String checkIn;
    private String checkOut;

    private String countryId;
    private String countryCode;

    private String cityId;
    private String cityCode;

    private String hotelId;
    private String hotelCode;

    private String nationality;
    private String matched;

    private BigDecimal totalRate;
    private BigDecimal totalOriginalRate;

    private String request;

    private HotelRoom hotelRooms;

    private String reminder;
    private String promoOriginalRate;
    private String updateTime;
    private String referenceCode;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMatched() {
        return matched;
    }

    public void setMatched(String matched) {
        this.matched = matched;
    }

    public BigDecimal getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(BigDecimal totalRate) {
        this.totalRate = totalRate;
    }

    public BigDecimal getTotalOriginalRate() {
        return totalOriginalRate;
    }

    public void setTotalOriginalRate(BigDecimal totalOriginalRate) {
        this.totalOriginalRate = totalOriginalRate;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public HotelRoom getHotelRooms() {
        return hotelRooms;
    }

    public void setHotelRooms(HotelRoom hotelRooms) {
        this.hotelRooms = hotelRooms;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getPromoOriginalRate() {
        return promoOriginalRate;
    }

    public void setPromoOriginalRate(String promoOriginalRate) {
        this.promoOriginalRate = promoOriginalRate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
}
