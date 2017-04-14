package com.nehow.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/14/17.
 */
public class HotelSearchResponse {

    private int rewriteKeyCount;
    private int completeRewriteKeyCount;
    private int hotelCount;
    private int availabilityCount;
    private HotelAvailability[] hotelAvailabilities;

    public int getRewriteKeyCount() {
        return rewriteKeyCount;
    }

    public void setRewriteKeyCount(int rewriteKeyCount) {
        this.rewriteKeyCount = rewriteKeyCount;
    }

    public int getCompleteRewriteKeyCount() {
        return completeRewriteKeyCount;
    }

    public void setCompleteRewriteKeyCount(int completeRewriteKeyCount) {
        this.completeRewriteKeyCount = completeRewriteKeyCount;
    }

    public int getHotelCount() {
        return hotelCount;
    }

    public void setHotelCount(int hotelCount) {
        this.hotelCount = hotelCount;
    }

    public int getAvailabilityCount() {
        return availabilityCount;
    }

    public void setAvailabilityCount(int availabilityCount) {
        this.availabilityCount = availabilityCount;
    }

    public HotelAvailability[] getHotelAvailabilities() {
        return hotelAvailabilities;
    }

    public void setHotelAvailabilities(HotelAvailability[] hotelAvailabilities) {
        this.hotelAvailabilities = hotelAvailabilities;
    }

}
