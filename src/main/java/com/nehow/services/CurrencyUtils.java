package com.nehow.services;

import java.math.BigDecimal;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/19/17.
 */
public class CurrencyUtils {

    public String getCurrency(String currencyCode) {
        switch (currencyCode.toLowerCase()) {
            case "usd":
                currencyCode = "$";
                break;
            case "gbp":
                currencyCode = "£";
                break;
            case "cny":
                currencyCode = "¥";
                break;
        }
        return currencyCode;
    }

    public long getPercentage(BigDecimal totalRate, BigDecimal rateDiscont) {
        return Math.round( (totalRate.subtract(rateDiscont).doubleValue()) * 100 / (totalRate.subtract(rateDiscont).doubleValue()) );
    }

    public long subtractDiscount(BigDecimal totalRate, BigDecimal rateDiscont) {
        return Math.round(totalRate.subtract(rateDiscont).doubleValue());
    }

}
