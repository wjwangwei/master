package com.nehow.controllers;

import cn.mogutrip.hotel.common.utils.JsonUtil;
import static cn.mogutrip.hotel.common.utils.StringUtil.*;
import com.nehow.dao.entity.*;
import com.nehow.dao.mapper.*;

import com.nehow.models.LoginStatus;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by andrew on 4/24/2017.
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userDao;
    @Autowired
    private CustomerMapper customerDao;
    @Autowired
    private SupplierMapper supplierDao;
    @Autowired
    private CustomerSupplierMapper customerSupplierDao;

    @RequestMapping("/userlist")
    public ModelAndView getUserlist(HttpServletRequest request,
                                    HttpServletResponse response,
                                    ModelAndView mv) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andStatusEqualTo("VALID");
        List<User> userlist = userDao.selectByExample(userExample);
        mv.addObject("userList", userlist);
        mv.setViewName("/user/user-info");
        return mv;
    }

    @RequestMapping("/addNewCustomer")
    public ModelAndView addNewCustomer(HttpServletRequest request,
            HttpServletResponse response,
            ModelAndView mv) {
        mv.setViewName("/user/addNewCustomer");
        return mv;
    }

    @RequestMapping("/addNewUser")
    public ModelAndView addNewUser(HttpServletRequest request,
                                HttpServletResponse response,
                                ModelAndView mv) {
        mv.setViewName("/user/addNewUser");
        return mv;
    }
            @RequestMapping("/service/userlist")
    public LoginStatus userlist(@RequestParam(value="customerid", defaultValue="1054") String customerid) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andCustomerIdEqualTo(parseLong(customerid));
        int count = userDao.countByExample(userExample);
        String status = "failed";
        String message = "";
        if(count > 0){
            status = "success";
            Map<String, String> userInfo = new HashMap<>();
            User  user=userDao.selectByExample(userExample).get(0);
            Customer customer = customerDao.selectByPrimaryKey(user.getCustomerId());
            userInfo.put("count", String.valueOf(count));
            userInfo.put("username", user.getUserName());
            userInfo.put("password", user.getPassword());
            userInfo.put("customerid", user.getCustomerId().toString());
            userInfo.put("usertype", user.getUserType());
            userInfo.put("mail", user.getMail());
            userInfo.put("mobile", user.getMobile());
            userInfo.put("customername", customer.getCustomerName());

            message = JsonUtil.toJson(userInfo);
        }
        else{
            message = "there is no user";
        }
        return new LoginStatus(status, message);
    }


    @RequestMapping("/service/login")
    public LoginStatus login(@RequestParam(value="user", defaultValue="") String userName, @RequestParam(value="password", defaultValue="") String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNameEqualTo(userName).andPasswordEqualTo(password).andStatusEqualTo("VALID");
        int count = userDao.countByExample(userExample);
        String status = "failed";
        String message = "";
        if(count > 0){
            status = "success";
            Map<String, String> userInfo = new HashMap<>();
            User user = userDao.selectByExample(userExample).get(0);
            String customerId = String.valueOf(user.getCustomerId());
            String customerType = user.getUserType();
            String userId=user.getId().toString();
            Customer customer = customerDao.selectByPrimaryKey(user.getCustomerId());
            String safeDay = customer.getSafeDay().toString();
            SupplierExample supplierExample = new SupplierExample();
            supplierExample.createCriteria().andStatusEqualTo("valid");
            List<Supplier> validSuppliers = supplierDao.selectByExample(supplierExample);
            Map<Long, String> supplierIdToName = new HashMap<>();
            for(Supplier supplier : validSuppliers){
                Long supplierId = supplier.getId();
                String supplierName = supplier.getSupplierName();
                supplierIdToName.put(supplierId, supplierName);
            }

            CustomerSupplierExample customerSupplierExample = new CustomerSupplierExample();
            customerSupplierExample.createCriteria().andCustomerIdEqualTo(user.getCustomerId());
            List<CustomerSupplier> customerSuppliers = customerSupplierDao.selectByExample(customerSupplierExample);
            List<String> supplierNames = new ArrayList<>();
            for(CustomerSupplier customerSupplier : customerSuppliers) {
                Long supplierId = customerSupplier.getSupplierId();
                String supplierName = supplierIdToName.get(supplierId);
                supplierNames.add(supplierName);
            }
            String suppliers = String.join(",", supplierNames);

            userInfo.put("customerId", customerId);
            userInfo.put("userName", userName);
            userInfo.put("customerType", customerType);
            userInfo.put("suppliers", suppliers);
            userInfo.put("safeDay", safeDay);
            userInfo.put("userId",userId);
            userInfo.put("customerCurrency","CNY");
            message = JsonUtil.toJson(userInfo);
        }
        else{
            message = "user or password is not right";
        }
        return new LoginStatus(status, message);
    }
}
