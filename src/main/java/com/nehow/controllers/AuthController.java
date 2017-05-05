package com.nehow.controllers;

import com.nehow.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
@Controller
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping("/create-account")
    public String signUp() {
        return "auth/sign-up";
    }

    @RequestMapping("/login")
    public String login() {
        return "index-guest";
    }

    @RequestMapping("/reset-password")
    public String resetPassword() {
        return "auth/reset-password";
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public String handleSignUp() {
        return "auth/sign-up";
    }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response) {
        try {
            authenticationService.logout();
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}