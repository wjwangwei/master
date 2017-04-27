package com.nehow.controllers;

import cn.mogutrip.hotel.common.entity.*;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseController implements ServletContextAware {
    // servlet context to store global data
    ServletContext context;

    // key for nationality suggestion
    protected final String kNationality = "nationality";

    // key for destination suggestion
    protected final String kDestination = "destination";

    //key for search request
    protected final String kRequest = "request";

    //key for hotel availability request
    protected final String kAvailabilityRequest = "request";

    // key for hotel results
    protected final String kHotels = "hotels";

    protected final String kHotelAvailability = "hotels";

    protected final String kMarkup = "markup";
    protected final String kExchange = "exchange";

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public List<String> buildPolicyRequestForHotel(SearchHotelAvailabilities hotelAv)  throws IOException
    {
        List<String> policyRequests = new ArrayList<>();
        JSONObject jsonParams = (JSONObject) context.getAttribute(kRequest);
        String json = jsonParams.get("request").toString();
        ObjectMapper mapper = new ObjectMapper();
        json = jsonParams.get("exchangeRates").toString();
        TypeReference<List<ExchangeRate>> tRef = new TypeReference<List<ExchangeRate>>() {};
        List<ExchangeRate> exchangeRates = mapper.readValue(json, tRef);
        json = jsonParams.get("markups").toString();
        TypeReference<Map<String, BigDecimal>> tRef1 = new TypeReference<Map<String, BigDecimal>>() {};
        Map<String, BigDecimal> markups = mapper.readValue(json, tRef1);
        List<HotelAvailability> avs = hotelAv.getAvailabilities();

        for(HotelAvailability av : avs) {
            String supplierId = av.getSupplierId();
            String hotelId = av.getHotelId();
            String hotelCode = av.getHotelCode();
            String checkIn = av.getCheckIn();
            String checkOut = av.getCheckOut();
            QueryCancellationPolicyRequest policyRequest = new QueryCancellationPolicyRequest();
            policyRequest.setSupplierId(supplierId);
            policyRequest.setHotelId(hotelId);
            policyRequest.setHotelCode(hotelCode);
            policyRequest.setCheckIn(checkIn);
            policyRequest.setCheckOut(checkOut);
            policyRequest.setExchangeRates(exchangeRates);
            policyRequest.setMarkups(markups);
            List<String> policyCodes = new ArrayList<>();
            List<HotelRoom> rms = av.getHotelRooms().getRooms();
            for(HotelRoom rm : rms){
                String policyCode = rm.getCancellationPolicyCode();
                policyCodes.add(policyCode);
            }
            policyRequest.setPolicyCodes(policyCodes);
            policyRequests.add(JsonUtil.toJson(policyRequest));
        }
        return policyRequests;

    }

    public List<String> buildVerifyRequestForHotel(SearchHotelAvailabilities hotelAv) throws IOException
    {
        List<String> verifyRequests = new ArrayList<>();
        List<HotelAvailability> avs = hotelAv.getAvailabilities();
        List<HotelAvailability> supplierAvs = hotelAv.getSupplierAvailabilities();
        JSONObject jsonParams = (JSONObject) context.getAttribute(kRequest);
        String json = jsonParams.get("request").toString();
        ObjectMapper mapper = new ObjectMapper();
        json = jsonParams.get("exchangeRates").toString();
        TypeReference<List<ExchangeRate>> tRef = new TypeReference<List<ExchangeRate>>() {};
        List<ExchangeRate> exchangeRates = mapper.readValue(json, tRef);
        json = jsonParams.get("markups").toString();
        TypeReference<Map<String, BigDecimal>> tRef1 = new TypeReference<Map<String, BigDecimal>>() {};
        Map<String, BigDecimal> markups = mapper.readValue(json, tRef1);
        int i = 0;
        for(HotelAvailability av : avs){
            VerifyAvailabilityRequest verifyRequest = new VerifyAvailabilityRequest();
            verifyRequest.setSource(VerifyAvailabilityRequest.REQUEST_SOURCE_CITY_PAGE);
            verifyRequest.setType(VerifyAvailabilityRequest.REQUEST_TYPE_FIRST_VERIFY);
            verifyRequest.setAvailability(av);
            verifyRequest.setSupplierAvailability(supplierAvs.get(i));
            HotelAvailabilityRequest request = JsonUtil.fromJson(av.getRequest(), HotelAvailabilityRequest.class);
            request.setHotelId(av.getHotelId());
            request.setHotelCode(av.getHotelCode());
            request.setReferenceId(av.getHotelRooms().getRooms().get(0).getReferenceId());
            List<Room> requestRooms = request.getRooms();
            List<HotelRoom> avRooms = av.getHotelRooms().getRooms();
            for(int j = 0; j < requestRooms.size(); j++){
                String roomRateCode = avRooms.get(j).getRoomRateCode();
                request.getRooms().get(j).setRoomRateCode(roomRateCode);
            }


            verifyRequest.setRequest(request);
            verifyRequest.setExchangeRates(exchangeRates);
            verifyRequest.setMarkups(markups);
            List<String> rateIds = new ArrayList<>();
            for(HotelRoom room : avRooms){
                String rateId = room.getRoomRateId();
                rateIds.add(rateId);
            }
            verifyRequest.setRoomRateIds(rateIds);
            verifyRequest.setCurrency(av.getCurrency());
            verifyRequest.setTotalRate(av.getTotalRate());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
            String result = objectMapper.writeValueAsString(verifyRequest);
            verifyRequests.add(JsonUtil.toJsonIgnoreAnnotations(verifyRequest));
            i++;
        }
        return verifyRequests;
    }

}
