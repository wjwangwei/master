package com.nehow.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by andrew on 4/24/2017.
 */
@Getter
@Setter
public class HotelVoucherParams {
    private String orderId;
    private String referenceCode;
    private String hotelName;
    private String starCss;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelEmail;
    private String hotelConfirmationNumber;
    private String checkIn;
    private String bookingDay;
    private int roomNights;
    private int roomNumber;
    private List<Room> roomList;
    private String emergencyPhoneList;
    private String mealPolicy;
    private String mealDesc;
    private String addService;
    private String remark;
}
