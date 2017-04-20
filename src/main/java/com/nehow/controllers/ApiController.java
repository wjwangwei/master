package com.nehow.controllers;

import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/suggest/nationality")
    public List<Nationality> getNationality(@RequestParam("search") String search) {
        Nationality[] nationalities = apiManager.getNationality(search);
        context.setAttribute(kNationality, nationalities);
        return Arrays.asList(nationalities);
    }

    @RequestMapping("/suggest/destination")
    public List<Destination> getDestinations(@RequestParam("search") String search) {
        Destination[] destinations = apiManager.getDestination(search);
        context.setAttribute(kDestination, destinations);
        return Arrays.asList(destinations);
    }


    @RequestMapping("/hotel/search")
    public HotelSearchResponse getHotels(@RequestParam("cityId") int cityId,
                                         @RequestParam("nationalityId") int nationalityId,
                                         @RequestParam("checkIn") String checkIn,
                                         @RequestParam("checkOut") String checkOut,
                                         @RequestParam("noOfRooms") int noOfRooms,
                                         HttpServletRequest request) {
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

        if (destination != null) {
            jsonRequest.put("countryId", destination.getCountryId());
            jsonRequest.put("cityId", Objects.toString(destination.getCityId()));
        } else {
            destination = new Destination();
            destination.setCityId(Integer.parseInt(request.getParameter("cityId")));
            //TODO change the countryId hard-coded into the request
            destination.setCountryId("TH");
            jsonRequest.put("cityId", destination.getCityId());
            jsonRequest.put("countryId", destination.getCountryId());
        }

        //TEST
//        jsonRequest.put("cityId", "48201");
        jsonRequest.put("countryId", "TH");
        jsonRequest.put("nationality", "CN");

        // date convert
        try {
            SimpleDateFormat sdfFrom = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd");

            Date date = sdfFrom.parse(checkIn);
            jsonRequest.put("checkIn", sdfTo.format(date));

            date = sdfFrom.parse(checkOut);
            jsonRequest.put("checkOut", sdfTo.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JSONArray jsonRooms = new JSONArray();
        for (int roomIndex = 1; roomIndex <= noOfRooms; roomIndex++) {
            JSONObject jsonRoom = new JSONObject();
            jsonRoom.put("roomIndex", Objects.toString(roomIndex));
            int roomPerSearch = 1;
            jsonRoom.put("rooms", roomPerSearch);
            jsonRoom.put("adults", Integer.parseInt(request.getParameter("room" + roomIndex + "NoOfAdult")));
            int childrenCount = Integer.parseInt(request.getParameter("room" + roomIndex + "NoOfChild"));
            jsonRoom.put("children", childrenCount);
            List<Integer> ages = null;
            if (childrenCount > 0) {
                ages = new ArrayList<>();
                for (int childIndex = 1; childIndex <= childrenCount; childIndex++) {
                    ages.add(Integer.parseInt(request.getParameter("room" + roomIndex + "child" + childIndex + "age")));
                }
            }
            jsonRoom.put("roomType", "");
            jsonRoom.put("roomRateCode", JSONNull.getInstance());
            jsonRoom.put("ages", ages == null ? JSONNull.getInstance() : ages);
            jsonRooms.add(jsonRoom);
        }
        jsonRequest.put("rooms", jsonRooms);
        if (nationality != null) {
            jsonRequest.put("nationality", nationality.getCountryId());
        } else {
            jsonRequest.put("nationality", request.getParameter("nationality").split(",")[0]);
        }

        jsonRequest.put("currency", "USD");

        String strQueryId = "";
        try {
            System.currentTimeMillis();
            strQueryId = "nehow" + "197.149.93.214" + System.currentTimeMillis() + Math.random();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(strQueryId));
            strQueryId = String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (Exception ignored) {
        }

        jsonRequest.put("queryId", strQueryId); //71701fb329892e076175ba56c0060d70
        jsonRequest.put("supplierId", CommonUtils.getSuppliers());
        jsonParam.put("request", jsonRequest);


        // exchangeRates
        JSONArray jsonExchanges = new JSONArray();
        for (ExchangeRate er : exchangeRates) {
            JSONObject jsonExchange = new JSONObject();
            jsonExchange.put("id", er.getId());
            jsonExchange.put("date", null);
            jsonExchange.put("currency0", er.getFromCurrency());
            jsonExchange.put("currency1", er.getToCurrency());
            jsonExchange.put("rate", er.getRate());
            jsonExchange.put("rateTime", null);

            jsonExchanges.add(jsonExchange);
        }
        jsonParam.put("exchangeRates", jsonExchanges);

        // markup
        JSONObject jsonMarkup = JSONObject.fromObject(mapMarkup);
        jsonParam.put("markups", jsonMarkup);

        // starRating
        JSONObject jsonStar = new JSONObject();
        jsonStar.put("low", 0);
        jsonStar.put("high", 100);
        jsonParam.put("starRating", jsonStar);

        // score
        jsonParam.put("score", 0);

        // sort
        JSONObject jsonSort = new JSONObject();
        jsonSort.put("field", "rate");
        jsonSort.put("mode", "ascend");
        jsonParam.put("sort", jsonSort);

        // limit
        JSONObject jsonLimit = new JSONObject();
        jsonLimit.put("start", 0);
        jsonLimit.put("length", 15);
        jsonParam.put("limit", jsonLimit);


        System.out.println(jsonParam.toString());

        context.setAttribute(kRequest, jsonParam);
        HotelSearchResponse hotelSearchResponse = apiManager.getCityAvailability((JSONObject) context.getAttribute(kRequest));

        context.setAttribute(kHotels, hotelSearchResponse);

        return hotelSearchResponse;
    }


    @RequestMapping(path = {"/hotel/availability/{hotelId}"})
    public HotelSearchResponse getHotel(String hotelId) {
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
//        hotelId = "837075";
        HotelSearchResponse hotelAvailability = apiManager.getHotelAvailability(request, hotelId);
        context.setAttribute(kHotelAvailability, hotelAvailability);
        return hotelAvailability;
    }
}
