package com.nehow.controllers;

import com.nehow.models.*;
import com.nehow.services.CommonUtils;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.System;

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

        HotelSearchResponse searchResponse = (HotelSearchResponse) context.getAttribute(kHotel);
        model.put("requestParam", request);
        model.put("pictureUrl", CommonUtils.getPicBaseUrl());
        model.put("searchResponse", searchResponse);

        boolean isResultAvail;
        if (searchResponse != null && searchResponse.getHotelAvailabilities().length > 0) {
            List<HotelAvailability> availabilities = Arrays.asList(searchResponse.getHotelAvailabilities());
            Map<Integer, Integer> starRatingCount = new HashMap<>();
            Set<Integer> rating = new TreeSet<>();

//            Map<String, Integer> reviewScoreCount = new HashMap<>();
//            Set<String> review = new HashSet<>();


            availabilities.stream().forEach(o -> rating.add(o.getHotel().getStarRatingSimple()));

            rating.stream().forEach(a -> starRatingCount.put(a, availabilities.stream().filter(o -> o.getHotel().getStarRatingSimple() == a).toArray().length));


            model.put("starRatings", rating);
            model.put("starRatingCounts", starRatingCount);
//            model.put("reviewScores", reviewScoreCount);
            isResultAvail = true;
        } else {
            isResultAvail = false;
        }
        model.put("isResult", isResultAvail);

        return "hotel/search-results";
    }

}
