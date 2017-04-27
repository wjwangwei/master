package com.nehow.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by andrew on 4/26/2017.
 */
@Setter
@Getter
public class HotelAvailabilityRequest {
    public HotelAvailabilityRequest() {
    }
    protected String countryId;
    protected String cityId;
    protected String checkIn;
    protected String checkOut;
    protected List<TestRoom> rooms;
    protected String hotelId;
    protected String hotelCode;
    protected String nationality;
    protected String currency;
    protected String language;
    protected String queryId;
    protected String supplierId;
    protected HotelAvailabilityRequest parent;
    protected String referenceId;
    protected int mode;
}
