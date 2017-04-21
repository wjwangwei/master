package com.nehow.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index-guest";
    }

    @RequestMapping("/home")
    public String home() {
        return "index";
    }

    @RequestMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @RequestMapping("/contact-us")
    public String contactUs() {
        return "contact-us";
    }


}