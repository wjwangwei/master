package com.nehow.controllers;

import com.nehow.ws.WebserviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Controller
@RequestMapping("/hotel")
public class HotelController {

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
        return "hotel/search";
    }

}
