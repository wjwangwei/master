package com.nehow.controllers;

import cn.mogutrip.hotel.business.entity.*;
import cn.mogutrip.hotel.common.entity.*;
import cn.mogutrip.hotel.common.entity.ExchangeRate;
import cn.mogutrip.hotel.common.generator.supplier.sunhotel.ws.Search;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import cn.mogutrip.hotel.suggestion.entity.Destination;
import cn.mogutrip.hotel.suggestion.entity.Nationality;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import com.nehow.services.Context;
import com.nehow.services.Pagination;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
        for(Nationality nationality : nationalities){
            System.out.println(nationality.getCountryName());
        }
        return Arrays.asList(nationalities);
    }

    @RequestMapping("/suggest/destination")
    public List<Destination> getDestinations(@RequestParam("search") String search) {
        Destination[] destinations = apiManager.getDestination(search);
        return Arrays.asList(destinations);
    }


    @RequestMapping("/hotel/search")
    public String searchHotels(@RequestParam("cityId") String cityId,
                                         @RequestParam("nationalityId") String nationalityId,
                                         @RequestParam("checkIn") String checkIn,
                                         @RequestParam("checkOut") String checkOut,
                                         @RequestParam("sortOrder") String sortOrder,
                                         @RequestParam("countryId") String countryId,
                                         @RequestParam("hotelId") String hotelId,
                                         @RequestParam("noOfRooms") int noOfRooms,
                                         @RequestParam("adults") String adults,
                                         @RequestParam("children") String children,
                                         @RequestParam(value="childrenAge", defaultValue="") String childrenAge,
                                         HttpServletRequest request) {
        SearchAvailabilityRequest searchRequest = new SearchAvailabilityRequest();

        if(cityId.split(",").length > 1){
            cityId = cityId.split(",")[0];
        }
        if(nationalityId.split(",").length > 1){
            nationalityId = nationalityId.split(",")[0];
        }
        if(checkIn.split(",").length > 1){
            checkIn = checkIn.split(",")[0];
        }
        if(checkOut.split(",").length > 1){
            checkOut = checkOut.split(",")[0];
        }
        if(countryId.split(",").length > 1){
            countryId = countryId.split(",")[0];
        }

        String[] adultsArray = adults.split(",");
        String[] childrenArray = children.split(",");
        String[] childAgeArray = null;
        if(!childrenAge.equals("")){
            childAgeArray = childrenAge.split(",");
        }
        String currency = Context.getCurrency();
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("c_currency") && cookie.getValue() != null) {
                    currency = cookie.getValue().toUpperCase();
                }
            }
        }


        Map<String, BigDecimal> markups = Context.getMarkup();
        if (markups == null) {
            // call rest api for fetching supplier markup
            markups = apiManager.getSupplierMarkup();
            Context.setMarkup(markups);
        }
        searchRequest.setMarkups(markups);

        List<ExchangeRate> exchangeRates = Context.getExchangeRate();
        if (exchangeRates == null) {
            exchangeRates = new ArrayList<>();
            // call rest api for fetching exchange rate
            List<cn.mogutrip.hotel.business.entity.ExchangeRate> rates = apiManager.getExchangeRate();
            for(cn.mogutrip.hotel.business.entity.ExchangeRate rate : rates){
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setCurrency0(rate.getFromCurrency());
                exchangeRate.setCurrency1(rate.getToCurrency());
                exchangeRate.setRateTime(rate.getRateTime());
                exchangeRate.setRate(rate.getRate());
                //exchangeRate.setId(rate.getId());
                exchangeRate.setDate(null);
                exchangeRates.add(exchangeRate);
            }
            Context.setExchangeRate(exchangeRates);
        }
        searchRequest.setExchangeRates(exchangeRates);

        Range<Integer> starRating = new Range<Integer>(0, 100);
        searchRequest.setStarRating(starRating);
        searchRequest.setScore(0);

        if(sortOrder == null){
            sortOrder = "ascend";
        }
        Sort sort = new Sort("rate", sortOrder);
        searchRequest.setSort(sort);

        // limit
        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page")));
        pagination.setElementsPerPage(Context.getHotelCntPerPage());
        Limit limit = pagination.getLimitObject();
        searchRequest.setLimit(limit);

        HotelAvailabilityRequest hotelAvailabilityRequest = new HotelAvailabilityRequest();
        hotelAvailabilityRequest.setCountryId(countryId);
        hotelAvailabilityRequest.setCityId(cityId);
        try {
            SimpleDateFormat sdfFrom = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy-MM-dd");

            Date date = sdfFrom.parse(checkIn);
            checkIn = sdfTo.format(date);

            date = sdfFrom.parse(checkOut);
            checkOut = sdfTo.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hotelAvailabilityRequest.setCheckIn(checkIn);
        hotelAvailabilityRequest.setCheckOut(checkOut);

        List<Room> rooms = new ArrayList<>();
        int childAgeIndex = 0;
        for (int roomIndex = 1; roomIndex <= noOfRooms; roomIndex++) {
            Room room = new Room();
            room.setRoomIndex(String.valueOf(roomIndex));
            int roomPerSearch = 1;
            room.setRooms(roomPerSearch);
            int noOfAdult = Integer.parseInt(adultsArray[roomIndex - 1]);
            room.setAdults(noOfAdult);
            int noOfChild = Integer.parseInt(childrenArray[roomIndex - 1]);
            room.setChildren(noOfChild);

            List<Integer> ages = null;
            if (noOfChild > 0) {
                ages = new ArrayList<>();
                for (int i = 0; i < noOfChild; i++) {
                    int age = Integer.parseInt(childAgeArray[childAgeIndex]);
                    childAgeIndex++;
                    ages.add(age);
                }
            }
            if(ages != null){
                room.setAges(ages);
            }
            room.setRoomType("");
            rooms.add(room);
        }
        hotelAvailabilityRequest.setRooms(rooms);
        if(hotelId != null && !hotelId.equals("0")){
            hotelAvailabilityRequest.setHotelId(hotelId);
        }
        hotelAvailabilityRequest.setNationality(nationalityId);
        hotelAvailabilityRequest.setCurrency(currency);

        String ip = Context.getIpAddr(request);
        String queryId = Context.getQueryId(ip);
        hotelAvailabilityRequest.setQueryId(queryId);
        String supplierId = Context.getSupplierIds();
        hotelAvailabilityRequest.setSupplierId(supplierId);
        searchRequest.setRequest(hotelAvailabilityRequest);
        Context.setSearchRequest(queryId, searchRequest);

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
        System.out.println(JsonUtil.toJsonIgnoreAnnotations(searchRequest).toString());
        do {
            hotelSearchResponse = apiManager.getCityAvailability(searchRequest);
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
        Context.setSearchResult(queryId, hotelSearchResponse);
        String ret = "{\"queryId\":\"" + queryId + "\"}";
        return ret;
    }


    @RequestMapping(path = {"/hotel/availability/{queryId}/{hotelId}"})
    public SearchAvailabilityResponse getHotel(@PathVariable("queryId") String queryId, @PathVariable("hotelId") String hotelId) {
        SearchAvailabilityRequest searchRequest = Context.getSearchRequest(queryId);
        SearchAvailabilityResponse hotelAvailability = apiManager.getHotelAvailability(searchRequest, hotelId);
        Context.setHotelAvailability(queryId, hotelId, hotelAvailability);
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
        System.out.println("policy request:" + request);
        Policies policy = apiManager.getRoomPolicy(request);
        //context.setAttribute(kHotelAvailability, hotelAvailability);
        return policy;
    }

    @RequestMapping("/hotel/hotelvoucher")
    public Voucher getHotelVoucher(@RequestParam("orderId") String orderId) {
        Voucher voucherUrl = apiManager.getHotelVoucher(orderId);
        return voucherUrl;
    }


    @RequestMapping(path = {"/hotel/cancellation"}, method = RequestMethod.POST)
    public Policies cancelHotel(@RequestBody String request) {
        System.out.println("cancel request:" + request);
        Policies policy = apiManager.cancelHotel(request);
        //context.setAttribute(kHotelAvailability, hotelAvailability);
        return policy;
    }
}
