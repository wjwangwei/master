package com.nehow.models;

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
