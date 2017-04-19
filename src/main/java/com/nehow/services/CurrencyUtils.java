package com.nehow.services;

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

    public long getPercentage(double totalRate, double rateDiscont) {
        return Math.round((totalRate - rateDiscont) * 100 / (totalRate - rateDiscont));
    }

    public long subtractDiscount(double totalRate, double rateDiscont) {
        return Math.round(totalRate - rateDiscont);
    }

}
