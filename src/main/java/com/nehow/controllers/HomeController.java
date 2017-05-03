package com.nehow.controllers;

import cn.mogutrip.core.common.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

//import static com.nehow.services.TestRequests.request;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */
@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home() {
        return "index";
    }

    @RequestMapping("/")
    public  String  login(HttpServletRequest request,@RequestParam(value="username", defaultValue="") String userName, @RequestParam(value="password", defaultValue="") String password, Model model) {
        if (StringUtil.isEmpty(userName) && StringUtil.isEmpty(password) ) {
            return "index-guest";
        }

        userName = userName.trim();
        System.out.println("username:" + userName + ",password:" + password);
        Subject currentUser = SecurityUtils.getSubject();
        Object loginTimes = currentUser.getSession().getAttribute("LOGINTIMES");
        if(loginTimes != null){
            currentUser.logout();
        }
        else{
            currentUser.getSession().setAttribute("LOGINTIMES", 1);
        }
        //验证是否登录成功
        if (!currentUser.isAuthenticated()) {
            System.out.println("username && password is not correct");
            return "index-guest";
        }
        else{
            return "redirect:/home";
        }

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