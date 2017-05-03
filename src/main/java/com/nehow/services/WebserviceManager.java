package com.nehow.services;


import cn.mogutrip.hotel.business.entity.*;
import cn.mogutrip.hotel.business.entity.ExchangeRate;
import cn.mogutrip.hotel.common.entity.*;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import cn.mogutrip.hotel.order.entity.Order;
import cn.mogutrip.hotel.suggestion.entity.Destination;
import cn.mogutrip.hotel.suggestion.entity.Nationality;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/11/17.
 */

@Component
public class WebserviceManager {

    private RestTemplate restTemplate;

    @Autowired
    private WebserviceProperties svcProperty;

    public WebserviceManager() {
        restTemplate = new RestTemplate();
    }

    /**
     * fetch supplier markup
     *
     * @return
     */
    //@Cacheable("supplierMarkup")
    public Map<String, BigDecimal> getSupplierMarkup() {
        String customerId = Context.getCustomerId();
        Object markup = restTemplate.getForObject(svcProperty.getRootUrl() + "/customer/supplier-markup/" + customerId, Map.class);

        return (Map<String, BigDecimal>) markup;
    }

    /**
     * fetch exchange rates
     *
     * @return array
     */
    //@Cacheable("exchangeRate")
    public List<ExchangeRate> getExchangeRate() {
        ResponseEntity<ExchangeRate[]> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/customer/exchange-rate", ExchangeRate[].class);
        return Arrays.asList(resp.getBody());
    }

    /**
     * fetch nationality suggestions
     *
     * @param key keyword
     * @return array
     */
    //@Cacheable("nationality")
    public Nationality[] getNationality(String key) {
        ResponseEntity<Nationality[]> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/suggestion/nationality?key=" + key, Nationality[].class);
        return resp.getBody();
    }

    /**
     * fetch destination suggestions
     *
     * @param key keyword
     * @return array
     */
    //@Cacheable("destinations")
    public Destination[] getDestination(String key) {
        ResponseEntity<Destination[]> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/suggestion/destination?key=" + key, Destination[].class);
        return resp.getBody();
    }


    public SearchAvailabilityResponse getCityAvailability(SearchAvailabilityRequest searchRequest) {
        String param = JsonUtil.toJsonIgnoreAnnotations(searchRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<String> entity = new HttpEntity<String>(param, headers);

        // determine url
        String strUrl = "/hotel/availability/city";

        // call available web service
        System.out.println(svcProperty.getRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
        JSONObject jsonResponse = response.getBody();

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = jsonResponse.getJSONArray("hotelAvailabilities");
        } catch (Exception ignored) {
        }

        //
        // transfer json to object
        //
        SearchAvailabilityResponse hotelReachResp = null;
        hotelReachResp = JsonUtil.fromJsonIgnoreAnnotations(jsonResponse.toString(),SearchAvailabilityResponse.class);

        System.out.println("RESPONSE: " + response.getBody().toString());
        return hotelReachResp;
    }

    /**
     * Fetch single hotel based on hotel ID
     *
     * @param param
     * @return
     */
    public SearchAvailabilityResponse getHotelAvailability(SearchAvailabilityRequest searchRequest, String hotelId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HotelAvailabilityRequest request = searchRequest.getRequest();
        request.setHotelId(hotelId);
        searchRequest.setRequest(request);
        String requestJson = JsonUtil.toJsonIgnoreAnnotations(searchRequest);
        /**
         * Mandatory to set the hotelId
         */

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        System.out.println(requestJson);
        // determine url
        String strUrl = "/hotel/availability/hotel";

        // call available web service
        System.out.println(svcProperty.getRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
       //
        // transfer json to object
        //
        SearchAvailabilityResponse hotelSearchResp = JsonUtil.fromJsonIgnoreAnnotations(
                response.getBody().toString(),
                SearchAvailabilityResponse.class
        );
        System.out.println("RESPONSE: " + response.getBody().toString());
        return hotelSearchResp;
    }

    public Policies getRoomPolicy(String request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(request, headers);

        // determine url
        String strUrl = "/hotel/availability/cancellation-policy";

        // call available web service
        System.out.println(svcProperty.getRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
        JSONObject jsonResponse = response.getBody();
        JSONObject jsonArray = jsonResponse.getJSONObject("policies");

        String policyText = jsonArray.toString();
        String refundable = jsonArray.getString("refundable");
        String safeOptionDay = jsonArray.getString("safeOptionDate");
        if(refundable.equals("true")){
            policyText = "Free cancellation can be made before " + safeOptionDay;
        }
        else{
            policyText = "This rate is non-refundable and cannot be changed or cancelled - if you do choose to change or cancel this booking you will not be refunded any of the payment.";
        }


        System.out.println("RESPONSE: " + response.getBody().toString());
        System.out.println("POLICY TEXT:" + policyText);
        Policies policy = new Policies();
        policy.setRefundable(refundable);
        String optionDate = jsonArray.getString("optionDate");
        policy.setOptionDate(optionDate);
        int safeDay = jsonArray.getInt("safeDay");
        policy.setSafeDay(safeDay);
        policy.setSafeOptionDate(safeOptionDay);
        String amendable = jsonArray.getString("amendable");
        policy.setAmendable(amendable);
        String changeName = jsonArray.getString("changeName");
        policy.setChangeName(changeName);
        policy.setPolicies(policyText);
        return policy;
    }

    public Hotel getHotel(String hotelId) {
        ResponseEntity<Hotel> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/hotel/supplier/" + hotelId, Hotel.class);
        return resp.getBody();
    }

    public VerifyAvailabilityResponse verifyHotel(String req)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<String> entity = new HttpEntity<String>(req, headers);

        System.out.println(req.toString());
        // determine url
        String strUrl = "/hotel/availability/verify";

        // call available web service
        System.out.println(svcProperty.getRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
        JSONObject jsonResponse = response.getBody();

        //
        // transfer json to object
        //
        /*
        VerifyAvailabilityResponse verifyResp = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            verifyResp = mapper.readValue(jsonResponse.toString(), VerifyAvailabilityResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        VerifyAvailabilityResponse verifyResp = JsonUtil.fromJsonIgnoreAnnotations(jsonResponse.toString(), VerifyAvailabilityResponse.class);
        System.out.println("RESPONSE: " + response.getBody().toString());
        return verifyResp;
    }

    public Order getHotelOrderDetail(String orderId)
    {
        String requestUrl = svcProperty.getBookingRootUrl() + "/hotel/order/" + orderId;
        ResponseEntity<Order> resp = restTemplate.getForEntity(requestUrl, Order.class);
        return resp.getBody();
    }

    public Voucher getHotelVoucher(String orderId)
    {
        String requestUrl = svcProperty.getUserAndVoucherUrl() +"/hotel/order/voucher/export/" + orderId;
        ResponseEntity<Voucher> resp = restTemplate.getForEntity(requestUrl, Voucher.class);
        return resp.getBody();
    }

    public LoginStatus getUserInfo(String userName, String password)
    {
        String requestUrl = svcProperty.getUserAndVoucherUrl() +"/login?user=" + userName + "&password=" + password;
        ResponseEntity<LoginStatus> resp = restTemplate.getForEntity(requestUrl, LoginStatus.class);
        return resp.getBody();
    }

    public Policies cancelHotel(String request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(request, headers);

        // determine url
        String strUrl = "/hotel/cancellation";

        // call available web service
        System.out.println(svcProperty.getBookingRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getBookingRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
        JSONObject jsonResponse = response.getBody();
        JSONObject jsonArray = jsonResponse.getJSONObject("policies");

        String policyText = jsonArray.toString();
        String refundable = jsonArray.getString("refundable");
        String safeOptionDay = jsonArray.getString("safeOptionDate");
        if(refundable.equals("true")){
            policyText = "Free cancellation can be made before " + safeOptionDay;
        }
        else{
            policyText = "This rate is non-refundable and cannot be changed or cancelled - if you do choose to change or cancel this booking you will not be refunded any of the payment.";
        }


        System.out.println("RESPONSE: " + response.getBody().toString());
        System.out.println("POLICY TEXT:" + policyText);
        Policies policy = new Policies();
        policy.setRefundable(refundable);
        String optionDate = jsonArray.getString("optionDate");
        policy.setOptionDate(optionDate);
        int safeDay = jsonArray.getInt("safeDay");
        policy.setSafeDay(safeDay);
        policy.setSafeOptionDate(safeOptionDay);
        String amendable = jsonArray.getString("amendable");
        policy.setAmendable(amendable);
        String changeName = jsonArray.getString("changeName");
        policy.setChangeName(changeName);
        policy.setPolicies(policyText);
        return policy;
    }

    public String getOrderId()
    {
        String strUrl = "/sequence/next-value?name=order.order_id";
        String requestUrl = svcProperty.getRootUrl() + strUrl;
        ResponseEntity<String> resp = restTemplate.getForEntity(requestUrl, String.class);
        return resp.getBody();
    }
}
