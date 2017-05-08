package com.nehow.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by andrew on 4/25/2017.
 */
@Getter
@Setter
public class CustomerVoucherParams {
    private String logoUrl;
    private String orderId;
    private String supplierReferenceCode;
    private String hotelEnName;
    private String hotelCnName;
    private String starCss;
    private String hotelAddress;
    private String hotelPhone;
    private String hotelEmail;
    private String hotelConfirmationNumber;
    private String checkIn;
    private String checkOut;
    private String currency;
    private String salePrice;
    private String groupNo;
    private String nationality;
    private String cancellationPolicy;
    private String amendmentPolicy;
    private String changeNamePolicy;
    private String userName;
    private String userMobile;
    private String userEmail;
    private String reminderMessage;
    private String importantMessage;
    private String bookingDay;
    private int roomNights;
    private int roomNumber;
    private List<Room> roomList;
    private String emergencyPhoneList;
    private String mealPolicy;
    private String mealDesc;
    private String addService;
    private String specialRequest;
}
