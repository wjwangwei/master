package com.nehow.controllers;

import cn.mogutrip.hotel.common.entity.Hotel;
import cn.mogutrip.hotel.common.entity.HotelAvailabilityResponse;
import cn.mogutrip.hotel.common.entity.SearchAvailabilityResponse;
import cn.mogutrip.hotel.common.entity.SearchHotelAvailabilities;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import cn.mogutrip.hotel.order.entity.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.CurrencyUtils;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONObject;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

        SearchAvailabilityResponse searchResponse = (SearchAvailabilityResponse) context.getAttribute(kHotels);
        model.put("requestParam", request);
        model.put("pictureUrl", CommonUtils.getPicBaseUrl());
        model.put("searchResponse", searchResponse);
        model.put("math", new MathTool());



        boolean isResultAvail;
        //if (searchResponse != null && searchResponse.getHotelAvailabilities().length > 0) {
        if (searchResponse != null && searchResponse.getHotelAvailabilities().size() > 0) {

            //List<HotelAvailability> availabilities = Arrays.asList(searchResponse.getHotelAvailabilities());
            List<SearchHotelAvailabilities> availabilities = searchResponse.getHotelAvailabilities();

            Map<Integer, Integer> starRatingCount = new HashMap<>();
            Set<Integer> rating = new TreeSet<>();

            Set<String> score = new HashSet<>();
            Map<String, Integer> scoreRatingCount = new HashMap<>();

            availabilities.stream().forEach(o -> rating.add((int)Math.round(o.getHotel().getStarRating()/10.0)));
            rating.stream().forEach(a -> starRatingCount.put(a, availabilities.stream().filter(o -> (int)Math.round(o.getHotel().getStarRating()/10.0) == a).toArray().length));

            availabilities.stream().forEach(o -> score.add(getScoreDesc(o.getHotel().getScore())));
            score.stream().forEach(a -> scoreRatingCount.put(a, availabilities.stream().filter(o -> Objects.equals(getScoreDesc(o.getHotel().getScore()), a)).toArray().length));

            try {
                Map<String, List<String>> hotelVerifyRequest = new HashMap<>();
                for(SearchHotelAvailabilities hotelAv : availabilities){
                    String hotelId = hotelAv.getHotel().getHotelId();
                    List<String> verifyRequests = buildVerifyRequestForHotel(hotelAv);
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
    public String booking(@PathVariable("hotelId")String hotelId, @PathVariable("roomCode")String roomCode, Map<String, Object> model) {
        JSONObject request = (JSONObject) context.getAttribute(kRequest);
        SearchAvailabilityResponse searchResponse = apiManager.getHotelAvailability(request, hotelId);
        if (searchResponse.getHotelCount() > 0) {
            model.put("hotel", searchResponse.getHotelAvailabilities().get(0).getHotel());
            model.put("availability", searchResponse.getHotelAvailabilities().get(0).getAvailabilities().get(0));
            model.put("supplierAvailability", searchResponse.getHotelAvailabilities().get(0).getSupplierAvailabilities().get(0));
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
        SearchAvailabilityResponse searchResponse = (SearchAvailabilityResponse) context.getAttribute(kHotelAvailability);
        if (searchResponse == null) {
            searchResponse = apiManager.getHotelAvailability(request, hotelId);
        }
        Hotel hotel = apiManager.getHotel(hotelId);
        if (searchResponse.getHotelCount() > 0) {
            SearchHotelAvailabilities hotelAvailability = null;
            for(SearchHotelAvailabilities av : searchResponse.getHotelAvailabilities()){
                String avHotelId = av.getHotel().getHotelId();
                if(hotelId.equals(avHotelId)){
                    hotelAvailability = av;
                    break;
                }
            }
            try {
                List<String> verifyRequests = buildVerifyRequestForHotel(hotelAvailability);
                model.put("verifyRequests", verifyRequests);
                List<String> policyRequests = buildPolicyRequestForHotel(hotelAvailability);
                model.put("policyRequests", policyRequests);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //HotelAvailability hotelAvailability = searchResponse.getHotelAvailabilities()[0];
//            System.out.println("Hotel:" + JSONObject.fromObject(hotel));
            hotel = (hotel == null) ? hotelAvailability.getHotel() : hotel;
            model.put("hotel", hotel);
            model.put("availabilities", hotelAvailability.getAvailabilities());
            model.put("hotelFacilities", JSONObject.fromObject(hotel.getFacilities()));
            model.put("hotelPolicies", JSONObject.fromObject(hotel.getPolicies()));

            model.put("request", request);
            String imgUrl = hotel.getPictureId().split(".jpg")[0];
            model.put("imgBaseUrl", CommonUtils.getPicBaseUrl() + imgUrl.substring(0, imgUrl.length() - 1));
            model.put("formatter", new CurrencyUtils());
            model.put("duration", 1);
            model.put("noOfRooms", 1);
            model.put("noOfAdults", 1);
            model.put("noOfChild", 1);
            model.put("math", new MathTool());
        }
        return "hotel/hotel";
    }

    @RequestMapping("/bookingconfirm/{hotelId}/{orderId}")
    public String bookingConfirm(@PathVariable("hotelId")String hotelId, @PathVariable("orderId")String orderId, Map<String, Object> model) {
        Order order = apiManager.getHotelOrderDetail(orderId);
        String orderStatus = order.getOrderStatus().getDescription();
        model.put("order", order);
        model.put("math", new MathTool());

        return "hotel/bookingconfirm";
    }
}
