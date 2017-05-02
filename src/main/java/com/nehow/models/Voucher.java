package com.nehow.models;

/**
 * Created by andrew on 4/29/2017.
 */
public class Voucher {
    private String orderId;
    private String voucherUrl;

    public Voucher(){

    }

    public Voucher(String orderId, String voucherUrl) {
        this.orderId = orderId;
        this.voucherUrl = voucherUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getVoucherUrl() {
        return voucherUrl;
    }
}
