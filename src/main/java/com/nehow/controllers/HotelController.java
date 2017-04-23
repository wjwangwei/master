package com.nehow.controllers;

import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.CurrencyUtils;
import com.nehow.services.WebserviceManager;
import jdk.nashorn.internal.objects.Global;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
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

        model.put("adultcount", 1);
        model.put("childcount", 1);
        model.put("childage", null);

        HotelSearchResponse searchResponse = (HotelSearchResponse) context.getAttribute(kHotels);
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
