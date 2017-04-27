package com.nehow.models;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by andrew on 4/26/2017.
 */
public class VerifyAvailabilityRequest {
    public static final String REQUEST_SOURCE_UNKNOWN = "unknown";
    public static final String REQUEST_SOURCE_CITY_PAGE = "city";
    public static final String REQUEST_SOURCE_HOTEL_PAGE = "hotel";
    public static final String REQUEST_SOURCE_ORDER_PAGE = "order";
    public static final int REQUEST_TYPE_FIRST_VERIFY = 0;
    public static final int REQUEST_TYPE_SECOND_VERIFY = 1;
    public static final int REQUEST_TYPE_VERIFY_MASK = 1;
    private String queryId;
    private String source;
    private int type;
    private Availability availability;
    private Availability supplierAvailability;
    private HotelAvailabilityRequest request;
    private List<ExchangeRate> exchangeRates;
    private List<String> roomRateIds;
    private Map<String, BigDecimal> markups;
    private String currency;
    private BigDecimal totalRate;
    private int safeDay;

    public VerifyAvailabilityRequest() {
    }

    public String getQueryId() {
        return this.queryId;
    }

    public String getSource() {
        return this.source;
    }

    public int getType() {
        return this.type;
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public Availability getSupplierAvailability() {
        return this.supplierAvailability;
    }

    public HotelAvailabilityRequest getRequest() {
        return this.request;
    }

    public List<ExchangeRate> getExchangeRates() {
        return this.exchangeRates;
    }

    public List<String> getRoomRateIds() {
        return this.roomRateIds;
    }

    public Map<String, BigDecimal> getMarkups() {
        return this.markups;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BigDecimal getTotalRate() {
        return this.totalRate;
    }

    public int getSafeDay() {
        return this.safeDay;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public void setSupplierAvailability(Availability supplierAvailability) {
        this.supplierAvailability = supplierAvailability;
    }

    public void setRequest(HotelAvailabilityRequest request) {
        this.request = request;
    }

    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }

    public void setRoomRateIds(List<String> roomRateIds) {
        this.roomRateIds = roomRateIds;
    }

    public void setMarkups(Map<String, BigDecimal> markups) {
        this.markups = markups;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTotalRate(BigDecimal totalRate) {
        this.totalRate = totalRate;
    }

    public void setSafeDay(int safeDay) {
        this.safeDay = safeDay;
    }
}
