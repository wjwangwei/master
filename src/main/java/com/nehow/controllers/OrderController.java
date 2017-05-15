package com.nehow.controllers;

import cn.mogutrip.hotel.order.entity.Order;
import com.nehow.dao.entity.OrdersAvailabilityResponse;
import com.nehow.services.Context;
import com.nehow.services.WebserviceManager;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
/**
 * Created by Administrator on 2017/5/7 0007.
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/queryList")
    public String allOrder(@RequestParam("order.orderId") String orderId,
                           @RequestParam("order.companyGroupCode") String companyGroupCode,
                           @RequestParam("order.referenceCode") String referenceCode,
                           @RequestParam("checkInStart") String checkInStart,
                           @RequestParam("checkInEnd") String checkInEnd,
                           @RequestParam("order.orderStatus") String orderStatus,
                           @RequestParam("order.countryName") String countryName,
                           @RequestParam("createTimeStart") String createTimeStart,
                           @RequestParam("createTimeEnd") String createTimeEnd,
                           @RequestParam("order.customerPayStatus") String customerPayStatus,
                           @RequestParam("order.cityName") String cityName,
                           @RequestParam("optionDayStart") String optionDayStart,
                           @RequestParam("optionDayEnd") String optionDayEnd,
                           @RequestParam("order.userId") String userId,
                           @RequestParam("travellerName") String travellerName,
                           HttpServletRequest request,
                           Map<String, Object> model) {

        Order orderRequest = new Order();
        orderRequest.setOrderId(orderId);
        orderRequest.setOrderId(companyGroupCode);
        orderRequest.setOrderId(referenceCode);
        orderRequest.setOrderId(checkInStart);
        orderRequest.setOrderId(checkInEnd);
        orderRequest.setOrderId(orderStatus);
        orderRequest.setOrderId(countryName);
        orderRequest.setOrderId(createTimeStart);
        orderRequest.setOrderId(createTimeEnd);
        orderRequest.setOrderId(customerPayStatus);
        orderRequest.setOrderId(cityName);
        orderRequest.setOrderId(optionDayStart);
        orderRequest.setOrderId(optionDayEnd);
        orderRequest.setOrderId(userId);
        orderRequest.setOrderId(travellerName);

        String ip = Context.getIpAddr(request);
        String queryId = Context.getQueryId(ip);
        orderRequest.setQueryId(queryId);
        Context.setOrderRequest(queryId, orderRequest);

        OrdersAvailabilityResponse orderSearchResponse = new OrdersAvailabilityResponse();
        int callTimes = 0;
        Map<Integer, Integer> waitTime = new HashedMap();
        waitTime.put(0, 0);
        waitTime.put(1, 1000);
        waitTime.put(2, 1000);
        waitTime.put(3, 1000);
        waitTime.put(4, 1000);
        waitTime.put(5, 1000);
        waitTime.put(6, 2000);
        waitTime.put(7, 2000);
        waitTime.put(8, 2000);
        waitTime.put(9, 2000);
        waitTime.put(10, 2000);
        waitTime.put(11, 4000);
        waitTime.put(12, 4000);
        waitTime.put(13, 4000);
        waitTime.put(14, 4000);
        waitTime.put(15, 4000);
        do {
            orderSearchResponse = apiManager.getOrdersAvailability(orderRequest);

            try {
                Thread.sleep(waitTime.get(callTimes));
                System.out.print("    线程睡眠1秒！\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callTimes++;

            if(callTimes >= 15){
                break;
            }
        }while(true);
        Context.setOrderResult(queryId, orderSearchResponse);

        return "_order-list";
    }

}
