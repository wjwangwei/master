package com.nehow.models;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
public class Country extends BaseModel {

    private String countryIsoCode;
    private String countryName;

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
