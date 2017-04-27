package com.nehow.controllers;

import cn.mogutrip.hotel.common.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.CurrencyUtils;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Controller
@RequestMapping("/hotel")
public class HotelController extends BaseController {

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/search-result")
    public String search(
            @RequestParam("cityId") int cityId,
            @RequestParam("nationalityId") int nationalityId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("noOfRooms") int noOfRooms,
            HttpServletRequest request,
            Map<String, Object> model) {

        model.put("checkin", checkIn);
        model.put("checkout", checkOut);
        model.put("roomcount", noOfRooms);

        model.put("adultcount", request.getParameter("adultCount"));
        model.put("childcount", request.getParameter("childCount"));
        model.put("childage", request.getParameter("infantCount"));

        HotelSearchResponse searchResponse = (HotelSearchResponse) context.getAttribute(kHotels);
        Arrays.asList(searchResponse.getHotelAvailabilities()).stream().map(item -> {
            final String[] prevHotelName = {""};

//            Arrays.asList(item.getAvailabilities()).stream().filter(availability -> availability.getHotelRooms().getRooms()[0].getRoomName());
            return item;
        });
        model.put("requestParam", request);
        model.put("pictureUrl", CommonUtils.getPicBaseUrl());
        model.put("searchResponse", searchResponse);



        boolean isResultAvail;
        if (searchResponse != null && searchResponse.getHotelAvailabilities().length > 0) {
            List<HotelAvailability> availabilities = Arrays.asList(searchResponse.getHotelAvailabilities());

            Map<Integer, Integer> starRatingCount = new HashMap<>();
            Set<Integer> rating = new TreeSet<>();

            Set<String> score = new HashSet<>();
            Map<String, Integer> scoreRatingCount = new HashMap<>();


            availabilities.stream().forEach(o -> rating.add(o.getHotel().getStarRatingSimple()));
            rating.stream().forEach(a -> starRatingCount.put(a, availabilities.stream().filter(o -> o.getHotel().getStarRatingSimple() == a).toArray().length));

            availabilities.stream().forEach(o -> score.add(getScoreDesc(o.getHotel().getScore())));
            score.stream().forEach(a -> scoreRatingCount.put(a, availabilities.stream().filter(o -> Objects.equals(getScoreDesc(o.getHotel().getScore()), a)).toArray().length));

            JSONObject jsonParams = (JSONObject) context.getAttribute(kRequest);

            ObjectMapper mapper = new ObjectMapper();

            try {
                String json = jsonParams.get("request").toString();
                //HotelAvailabilityRequest apiRequest = JsonUtil.fromJson(jsonParams.get("request").toString(), HotelAvailabilityRequest.class);
                HotelAvailabilityRequest apiRequest = mapper.readValue(json, HotelAvailabilityRequest.class);
                json = jsonParams.get("exchangeRates").toString();
                TypeReference<List<ExchangeRate>> tRef = new TypeReference<List<ExchangeRate>>() {};
                List<ExchangeRate> exchangeRates = mapper.readValue(json, tRef);
                json = jsonParams.get("markups").toString();
                TypeReference<Map<String, BigDecimal>> tRef1 = new TypeReference<Map<String, BigDecimal>>() {};
                Map<String, BigDecimal> markups = mapper.readValue(json, tRef1);

                Map<String, List<String>> hotelVerifyRequest = new HashMap<>();

                for(HotelAvailability hotelAv : availabilities){
                    Availability[] avs = hotelAv.getAvailabilities();
                    Availability[] supplierAvs = hotelAv.getAvailabilities();
                    List<String> verifyRequests = new ArrayList<>();
                    int i = 0;
                    String hotelId = hotelAv.getHotel().getHotelId();
                    for(Availability av : avs){
                        VerifyAvailabilityRequest verifyRequest = new VerifyAvailabilityRequest();
                        verifyRequest.setSource(VerifyAvailabilityRequest.REQUEST_SOURCE_CITY_PAGE);
                        verifyRequest.setType(VerifyAvailabilityRequest.REQUEST_TYPE_FIRST_VERIFY);
                        verifyRequest.setAvailability(av);
                        verifyRequest.setSupplierAvailability(supplierAvs[i]);
                        verifyRequest.setRequest(apiRequest);
                        verifyRequest.setExchangeRates(exchangeRates);
                        verifyRequest.setMarkups(markups);
                        Room[] rooms = av.getHotelRooms().getRooms();
                        List<String> rateIds = new ArrayList<>();
                        for(Room room : rooms){
                            String rateId = room.getRoomRateId();
                            rateIds.add(rateId);
                        }
                        verifyRequest.setRoomRateIds(rateIds);
                        verifyRequest.setCurrency(av.getCurrency());
                        verifyRequest.setTotalRate(av.getTotalRate());
                        verifyRequests.add(JsonUtil.toJson(verifyRequest));
                    }
                    hotelVerifyRequest.put(hotelId, verifyRequests);
                }
                model.put("hotelVerifyRequests", hotelVerifyRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            model.put("starRatings", rating);
            model.put("starRatingCounts", starRatingCount);
            model.put("scoreRatings", score);
            model.put("scoreRatingCounts", scoreRatingCount);
            model.put("formatter", new CurrencyUtils());


//            model.put("reviewScores", scoreRatingCount);

            try {
                int currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
                model.put("currentPage", currentPage);
                model.put("nextPage", currentPage + 1);
                model.put("paginateElipse", currentPage + 2);
            } catch (Exception e) {
            }
            isResultAvail = true;
        } else {
            isResultAvail = false;
        }
        model.put("isResult", isResultAvail);
        model.put("encoder", java.net.URLEncoder.class);

        return "hotel/search";
    }

    private String getScoreDesc(int score) {
        if (score >= 90) return "Wonderful " + score / 10.0;
        else if (score >= 80) return "Very Good " + score / 10.0;
        else if (score >= 70) return "Good " + score / 10.0;
        else if (score > 60) return "Pleasant " + score / 10.0;
        else return "";

    }

    @RequestMapping("/booking/{hotelId}/{roomCode}")
    public String booking(String hotelId, String roomCode, Map<String, Object> model) {
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
        HotelSearchResponse searchResponse = apiManager.getHotelAvailability(request, "837075");
        if (searchResponse.getHotelCount() > 0) {
            model.put("hotel", searchResponse.getHotelAvailabilities()[0].getHotel());
            model.put("availability", searchResponse.getHotelAvailabilities()[0].getAvailabilities()[0]);
            model.put("supplierAvailability", searchResponse.getHotelAvailabilities()[0].getSupplierAvailabilities()[0]);
            model.put("request", request);
            model.put("dateFormatter", new SimpleDateFormat("DDD, MM dd yyyy"));
            model.put("duration", 1);
            model.put("noOfRooms", 1);
            model.put("noOfAdults", 1);
            model.put("noOfChild", 1);
        }
        return "hotel/booking";
    }

    @RequestMapping("/detail")
    public String hotel(@RequestParam("hotelId") String hotelId, Map<String, Object> model) {
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
        HotelSearchResponse searchResponse = (HotelSearchResponse) context.getAttribute(kHotelAvailability);
        if (searchResponse == null) {
            searchResponse = apiManager.getHotelAvailability(request, hotelId);
            if (searchResponse.getRewriteKeyCount() <= searchResponse.getCompleteRewriteKeyCount()) {
                searchResponse = apiManager.getHotelAvailability(request, hotelId);
            }
        }
        Hotel hotel = apiManager.getHotel(hotelId);
        if (searchResponse.getHotelCount() > 0) {
            HotelAvailability hotelAvailability = searchResponse.getHotelAvailabilities()[0];
//            System.out.println("Hotel:" + JSONObject.fromObject(hotel));
            hotel = (hotel == null) ? hotelAvailability.getHotel() : hotel;
            model.put("hotel", hotel);
            model.put("availabilities", hotelAvailability.getAvailabilities());
            model.put("roomFacilities", JSONObject.fromObject(hotel.getFacilities()));
            model.put("hotelPolicies", JSONObject.fromObject(hotel.getPolicies()));

            model.put("request", request);
            String imgUrl = hotel.getPictureId().split(".jpg")[0];
            model.put("imgBaseUrl", CommonUtils.getPicBaseUrl() + imgUrl.substring(0, imgUrl.length() - 1));
            model.put("formatter", new CurrencyUtils());
            model.put("duration", 1);
            model.put("noOfRooms", 1);
            model.put("noOfAdults", 1);
            model.put("noOfChild", 1);
        }
        return "hotel/hotel";
    }


}
