package com.nehow.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nehow.models.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
    @Cacheable("supplierMarkup")
    public Map<String, Double> getSupplierMarkup() {
        Object markup = restTemplate.getForObject(svcProperty.getRootUrl() + "/customer/supplier-markup/0", Map.class);

        return (Map<String, Double>) markup;
    }

    /**
     * fetch exchange rates
     *
     * @return array
     */
    @Cacheable("exchangeRate")
    public ExchangeRate[] getExchangeRate() {
        ResponseEntity<ExchangeRate[]> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/customer/exchange-rate", ExchangeRate[].class);
        return resp.getBody();
    }

    /**
     * fetch nationality suggestions
     *
     * @param key keyword
     * @return array
     */
    @Cacheable("nationality")
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
    @Cacheable("destinations")
    public Destination[] getDestination(String key) {
        ResponseEntity<Destination[]> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/suggestion/destination?key=" + key, Destination[].class);
        return resp.getBody();
    }

    /**
     * fetch hotel availabilities
     *
     * @param param json
     * @return
     */
    public HotelSearchResponse getCityAvailability(JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<String> entity = new HttpEntity<String>(param.toString(), headers);

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
        HotelSearchResponse hotelReachResp = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            hotelReachResp = mapper.readValue(jsonResponse.toString(), HotelSearchResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("RESPONSE: " + response.getBody().toString());
        return hotelReachResp;
    }

    /**
     * Fetch single hotel based on hotel ID
     *
     * @param param
     * @return
     */
    public HotelSearchResponse getHotelAvailability(JSONObject param, String hotelId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        JSONObject req = (JSONObject) param.get("request");
        req.put("hotelId", hotelId);
        param.put("request", req);
        /**
         * Mandatory to set the hotelId
         */

        HttpEntity<String> entity = new HttpEntity<String>(param.toString(), headers);

        System.out.println(param);
        // determine url
        String strUrl = "/hotel/availability/hotel";

        // call available web service
        System.out.println(svcProperty.getRootUrl() + strUrl);
        ResponseEntity<JSONObject> response = restTemplate.exchange(svcProperty.getRootUrl() + strUrl, HttpMethod.POST, entity, JSONObject.class);
        JSONObject jsonResponse = response.getBody();
        JSONArray jsonArray = jsonResponse.getJSONArray("hotelAvailabilities");

        //
        // transfer json to object
        //
        HotelSearchResponse hotelReachResp = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            hotelReachResp = mapper.readValue(jsonResponse.toString(), HotelSearchResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("RESPONSE: " + response.getBody().toString());
        return hotelReachResp;
    }

    public Hotel getHotel(String hotelId) {
        ResponseEntity<Hotel> resp = restTemplate.getForEntity(svcProperty.getRootUrl() + "/hotel/supplier/" + hotelId, Hotel.class);
        return resp.getBody();
    }

}
