package com.nehow.controllers;

import com.nehow.models.*;
import com.nehow.ws.WebserviceManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Controller
@RequestMapping("/hotel")
public class HotelController extends BaseController {
    private final String kMarkup = "markup";
    private final String kExchange = "exchange";

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/search")
    public String search(
            @RequestParam("cityId") int cityId,
            @RequestParam("nationalityId") int nationalityId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("noOfRooms") String noOfRooms,
            HttpServletRequest request
    ) {
        Destination destination = null;
        Nationality nationality = null;

        Destination[] destinations = (Destination[]) context.getAttribute(kDestination);
        if (destinations != null) {
            for (Destination dst : destinations) {
                if (cityId == dst.getId()) {
                    destination = dst;
                    break;
                }
            }
        }

        Nationality[] nationalities = (Nationality[]) context.getAttribute(kNationality);
        if (nationalities != null) {
            for (Nationality nat : nationalities) {
                if (nationalityId == nat.getId()) {
                    nationality = nat;
                    break;
                }
            }
        }

        Map<String, Double> mapMarkup = (Map<String, Double>) context.getAttribute(kMarkup);
        if (mapMarkup == null) {
            // call rest api for fetching supplier markup
            mapMarkup = apiManager.getSupplierMarkup();
            context.setAttribute(kMarkup, mapMarkup);
        }
        ExchangeRate[] exchangeRates = (ExchangeRate[]) context.getAttribute(kExchange);
        if (exchangeRates == null) {
            // call rest api for fetching exchange rate
            exchangeRates = apiManager.getExchangeRate();
            context.setAttribute(kExchange, exchangeRates);
        }
        // request
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonRequest = new JSONObject();

        HotelAvailability[] hotelAvailabilities = apiManager.getCityAvailability(jsonParam, (destination != null && destination.isHotel()));

        return "hotel/search";
    }

}
