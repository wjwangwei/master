package com.nehow.controllers;

import com.nehow.models.Country;
import com.nehow.models.Destination;
import com.nehow.models.Nationality;
import com.nehow.ws.WebserviceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/suggest/nationality")
    public List<Nationality> getNationality(@RequestParam("search") String search) {
        return Arrays.asList(apiManager.getNationality(search));
    }

    @RequestMapping("/suggest/destination")
    public List<Destination> getDestinations(@RequestParam("search") String search) {
        return Arrays.asList(apiManager.getDestination(search));
    }


}
