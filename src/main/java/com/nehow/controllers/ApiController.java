package com.nehow.controllers;

import cn.mogutrip.hotel.business.entity.ExchangeRate;
import cn.mogutrip.hotel.common.entity.SearchAvailabilityRequest;
import cn.mogutrip.hotel.common.entity.SearchAvailabilityResponse;
import cn.mogutrip.hotel.common.entity.VerifyAvailabilityResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.Pagination;
import com.nehow.services.TestRequests;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
    public SearchAvailabilityResponse getHotels(@RequestParam("cityId") int cityId,
                                         @RequestParam("nationalityId") int nationalityId,
                                         @RequestParam("checkIn") String checkIn,
                                         @RequestParam("checkOut") String checkOut,
                                         @RequestParam("noOfRooms") int noOfRooms,
                                         HttpServletRequest request) {
        Destination destination = null;
        Nationality nationality = null;
        String currency = CommonUtils.getProperty("application.defaultCurrency");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("c_currency") && cookie.getValue() != null) {
                    currency = cookie.getValue().toUpperCase();
                }
            }
        }

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
        List<ExchangeRate> exchangeRates = (List<ExchangeRate>) context.getAttribute(kExchange);
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
            destination.setCountryId(request.getParameter("countryCode"));
            destination.setCountryId(request.getParameter("nationalityCode"));

            jsonRequest.put("cityId", destination.getCityId());
            jsonRequest.put("countryId", destination.getCountryId());
        }

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

        jsonRequest.put("currency", currency);

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
        //TODO remove test exhange rate
        jsonExchanges = JSONArray.fromObject(TestRequests.exchangeRates);
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
        String order = request.getParameter("sort_order") != null ? request.getParameter("sort_order") : "ascend";
        jsonSort.put("mode", order);
        jsonParam.put("sort", jsonSort);

        // limit
        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page")));
        pagination.setElementsPerPage(CommonUtils.getProperty("pagination.perpage", Integer.class));

        jsonParam.put("limit", pagination.getPaginateObject());

        System.out.println(jsonParam.toString());

        context.setAttribute(kRequest, jsonParam);
        ObjectMapper mapper = new ObjectMapper();


        SearchAvailabilityResponse hotelSearchResponse = new SearchAvailabilityResponse();
        hotelSearchResponse.setHotelCount(0);
        int callTimes = 0;
        Map<Integer, Integer> waitTime = new HashedMap();
        waitTime.put(0, 0);
        waitTime.put(1, 1000);
        waitTime.put(2, 1000);
        waitTime.put(3, 1000);
        waitTime.put(4, 1000);
        waitTime.put(5, 1000);
        waitTime.put(6, 2000);
        waitTime.put(7, 2000);
        waitTime.put(8, 2000);
        waitTime.put(9, 2000);
        waitTime.put(10, 2000);
        waitTime.put(11, 4000);
        waitTime.put(12, 4000);
        waitTime.put(13, 4000);
        waitTime.put(14, 4000);
        waitTime.put(15, 4000);

        do {
            hotelSearchResponse = apiManager.getCityAvailability((JSONObject) context.getAttribute(kRequest));
            int rewriteKeyCnt = hotelSearchResponse.getRewriteKeyCount();
            int completeKeyCnt = hotelSearchResponse.getCompleteRewriteKeyCount();
            try {
                Thread.sleep(waitTime.get(callTimes));
                System.out.print("    线程睡眠1秒！\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callTimes++;
            if(rewriteKeyCnt <= completeKeyCnt){
                break;
            }
            if(callTimes >= 15){
                break;
            }
        }while(true);


        if (hotelSearchResponse.getHotelCount() > 0)
            context.setAttribute(kHotels, hotelSearchResponse);
        return hotelSearchResponse;
    }


    @RequestMapping(path = {"/hotel/availability/{hotelId}"})
    public SearchAvailabilityResponse getHotel(String hotelId) {
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
        SearchAvailabilityResponse hotelAvailability = apiManager.getHotelAvailability(request, hotelId);
        context.setAttribute(kHotelAvailability, hotelAvailability);
        return hotelAvailability;
    }

    @RequestMapping(path = {"/hotel/availability/verify"}, method = RequestMethod.POST)
    public VerifyAvailabilityResponse verifyHotel(@RequestBody String request) {
        VerifyAvailabilityResponse verifyResponse = apiManager.verifyHotel(request);
        return verifyResponse;
    }

    @RequestMapping(path = {"/hotel/room-policy"}, method = RequestMethod.POST)
    public Policies getRoomPolicy(@RequestBody String request) {
        /*
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
        try {
            policyCode = URLDecoder.decode(policyCode, "UTF-8");
        } catch (Exception ignored){}
        */
        Policies policy = apiManager.getRoomPolicy(request);
        //context.setAttribute(kHotelAvailability, hotelAvailability);
        return policy;
    }


}
