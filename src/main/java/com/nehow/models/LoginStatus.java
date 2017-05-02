package com.nehow.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by andrew on 4/29/2017.
 */
@Getter
@Setter
public class LoginStatus {
    private String status;
    private String message;

    public LoginStatus(){

    }
    public LoginStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
