package com.nehow.controllers;

import cn.mogutrip.core.common.utils.DateUtil;
import cn.mogutrip.hotel.common.entity.*;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import cn.mogutrip.hotel.order.entity.*;
import com.nehow.models.*;
import com.nehow.services.Context;
import com.nehow.services.CurrencyUtils;
import com.nehow.services.WebserviceManager;
import net.sf.json.JSONObject;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Controller
@RequestMapping("/hotel")
public class HotelController extends BaseController {

    @Autowired
    private WebserviceManager apiManager;

    @RequestMapping("/search-result")
    public String search(
            @RequestParam("cityId") String cityId,
            @RequestParam("nationalityId") String nationalityId,
            @RequestParam("checkIn") String checkIn,
            @RequestParam("checkOut") String checkOut,
            @RequestParam("noOfRooms") int noOfRooms,
            @RequestParam("queryId") String queryId,
            HttpServletRequest request,
            Map<String, Object> model) {

        model.put("checkin", checkIn);
        model.put("checkout", checkOut);
        model.put("roomcount", noOfRooms);

        String adultCount = request.getParameter("adultCount");
        String childCount = request.getParameter("childCount");
        String infantCount = request.getParameter("infantCount");
        model.put("adultcount", adultCount);
        model.put("childcount", childCount);
        model.put("childage", infantCount);

        SearchAvailabilityResponse searchResponse = Context.getSearchResult(queryId);
        model.put("requestParam", request);

        model.put("pictureUrl", Context.getPicBaseUrl());
        model.put("searchResponse", searchResponse);
        model.put("math", new MathTool());
        model.put("queryId", queryId);



        boolean isResultAvail;
        //if (searchResponse != null && searchResponse.getHotelAvailabilities().length > 0) {
        if (searchResponse != null && searchResponse.getHotelAvailabilities().size() > 0) {

            //List<HotelAvailability> availabilities = Arrays.asList(searchResponse.getHotelAvailabilities());
            List<SearchHotelAvailabilities> availabilities = searchResponse.getHotelAvailabilities();

            Map<Integer, Integer> starRatingCount = new HashMap<Integer, Integer>();
            Set<Integer> rating = new TreeSet<Integer>();

            Set<String> score = new HashSet<String>();
            Map<String, Integer> scoreRatingCount = new HashMap<String, Integer>();

            availabilities.stream().forEach(o -> rating.add((int)Math.round(o.getHotel().getStarRating()/10.0)));
            rating.stream().forEach(a -> starRatingCount.put(a, availabilities.stream().filter(o -> (int)Math.round(o.getHotel().getStarRating()/10.0) == a).toArray().length));

            availabilities.stream().forEach(o -> score.add(getScoreDesc(o.getHotel().getScore())));
            score.stream().forEach(a -> scoreRatingCount.put(a, availabilities.stream().filter(o -> Objects.equals(getScoreDesc(o.getHotel().getScore()), a)).toArray().length));

            try {
                Map<String, List<String>> hotelVerifyRequest = new HashMap<>();
                Map<String, List<String>> hotelPolicyRequest = new HashMap<>();
                for(SearchHotelAvailabilities hotelAv : availabilities){
                    String hotelId = hotelAv.getHotel().getHotelId();
                    List<String> verifyRequests = buildVerifyRequestForHotel(queryId, hotelAv);
                    hotelVerifyRequest.put(hotelId, verifyRequests);
                    List<String> policyRequests = buildPolicyRequestForHotel(queryId, hotelAv);
                    hotelPolicyRequest.put(hotelId, policyRequests);

                }
                model.put("hotelPolicyRequests", hotelPolicyRequest);
                model.put("hotelVerifyRequests", hotelVerifyRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }

            model.put("starRatings", rating);
            model.put("starRatingCounts", starRatingCount);
            model.put("scoreRatings", score);
            model.put("scoreRatingCounts", scoreRatingCount);
            model.put("formatter", new CurrencyUtils());


//            model.put("reviewScores", scoreRatingCount);

            try {
                int currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
                int hotelCnt = searchResponse.getHotelCount();
                int hotelCntPerPage = Context.getHotelCntPerPage();
                if(hotelCnt < hotelCntPerPage){
                    model.put("pagination", "false");
                }
                else{
                    model.put("pagination", "true");
                }
                model.put("currentPage", currentPage);
                if(currentPage + hotelCntPerPage < hotelCnt){
                    model.put("nextPage", currentPage + 1);
                }
                else{
                    model.put("nextPage", currentPage);
                }
                if(currentPage + hotelCntPerPage * 2 < hotelCnt){
                    model.put("paginateElipse", currentPage + 2);
                }
                else if(currentPage + hotelCntPerPage * 1 <= hotelCnt){
                    model.put("paginateElipse", currentPage + 1);
                }
                else{
                    model.put("paginateElipse", currentPage);
                }


                //model.put("nextPage", currentPage + 1);
                //model.put("paginateElipse", currentPage + 2);
            } catch (Exception e) {
            }
            isResultAvail = true;
        } else {
            isResultAvail = false;
        }
        model.put("isResult", isResultAvail);
        model.put("encoder", java.net.URLEncoder.class);

        return "hotel/search";
    }

    private String getScoreDesc(int score) {
        if (score >= 90) return "Wonderful " + score / 10.0;
        else if (score >= 80) return "Very Good " + score / 10.0;
        else if (score >= 70) return "Good " + score / 10.0;
        else if (score > 60) return "Pleasant " + score / 10.0;
        else return "";

    }

    @RequestMapping("/booking/{hotelId}/{queryId}/{targetIndex}")
    public String booking(@PathVariable("hotelId")String hotelId,
                          @PathVariable("queryId")String queryId,
                          @PathVariable("targetIndex")String targetIndex,
                          Map<String, Object> model
    ) {
        SearchAvailabilityRequest request = Context.getSearchRequest(queryId);
        Hotel hotel = apiManager.getHotel(hotelId);
        model.put("hotel", hotel);
        VerifyAvailabilityResponse verifyResponse = Context.getVerifyResponse(queryId, hotelId);
        HotelAvailability hotelAv = verifyResponse.getAvailabilities().get(Integer.parseInt(targetIndex));
        model.put("availability", hotelAv);
        int noOfRooms = 0;
        int noOfAdults = 0;
        int noOfChild = 0;
        long duration = 0;
        List<Room> requestRooms = request.getRequest().getRooms();
        model.put("requestrooms", requestRooms);
        List<HotelRoom> roomrates = hotelAv.getHotelRooms().getRooms();
        model.put("roomrates", roomrates);
        for(Room room : requestRooms)
        {
            noOfRooms = noOfRooms + room.getRooms();
            noOfAdults = noOfAdults + room.getAdults();
            noOfChild = noOfChild + room.getChildren();
        }

        String checkIn = request.getRequest().getCheckIn();
        String checkOut = request.getRequest().getCheckOut();
        model.put("checkIn", checkIn);
        model.put("checkOut", checkOut);
        String format = "yyyy-MM-dd";
        try{
            duration = DateUtil.dateDiff(checkIn, checkOut, format);
        }
        catch(Exception e){
        }
        model.put("duration", duration);
        model.put("noOfRooms", noOfRooms);
        model.put("noOfAdults", noOfAdults);
        model.put("noOfChild", noOfChild);
        Map<String, List<HotelRoomPolicy>> policies = hotelAv.getHotelRooms().getPolicies().getPolicies();
        String cancellationPolicyText = "This rate is non-refundable and cannot be changed or cancelled - if you do choose to change or cancel this booking you will not be refunded any of the payment.";
        if(policies != null){
            List<HotelRoomPolicy> cancellationPolicy = policies.get("cancellation");
            cancellationPolicyText = getCancellationPolicyText(cancellationPolicy);
        }

        String changeNamePolicyText = "Not allowed";
        String amendmentPolicyText = "Not allowed";
        if(hotelAv.getHotelRooms().getPolicies().getAmendable().equals("true")){
            changeNamePolicyText = "Allowed";
        }
        if(hotelAv.getHotelRooms().getPolicies().getAmendable().equals("true")){
            amendmentPolicyText = "Allowed";
        }
        model.put("cancellationpolicy", cancellationPolicyText);
        model.put("changenamepolicy", changeNamePolicyText);
        model.put("amendmentpolicy", amendmentPolicyText);

        String roomText = "";
        for(HotelRoom hotelRoom : hotelAv.getHotelRooms().getRooms()){
            if(roomText.equals("")){
                roomText = hotelRoom.getRoomName() + " x " + String.valueOf(hotelRoom.getRoomCount());
            }
            else{
                roomText = roomText + ", " + hotelRoom.getRoomName() + " x " + String.valueOf(hotelRoom.getRoomCount());
            }
        }
        model.put("roomtext", roomText);
        model.put("totalrate", hotelAv.getTotalRate());
        model.put("queryid", queryId);
        model.put("targetindex", targetIndex);

        return "hotel/booking";
    }

    @RequestMapping("/detail")
    public String hotel(@RequestParam("queryId") String queryId, @RequestParam("hotelId") String hotelId, Map<String, Object> model) {
        SearchAvailabilityRequest searchRequest = Context.getSearchRequest(queryId);
        //SearchAvailabilityResponse searchResponse = Context.getHotelAvailability(queryId, hotelId);
        SearchAvailabilityResponse searchResponse = Context.getSearchResult(queryId);

        if (searchResponse == null) {
            searchResponse = apiManager.getHotelAvailability(searchRequest, hotelId);
        }
        Hotel hotel = apiManager.getHotel(hotelId);
        if (searchResponse.getHotelCount() > 0) {
            SearchHotelAvailabilities hotelAvailability = null;
            for(SearchHotelAvailabilities av : searchResponse.getHotelAvailabilities()){
                String avHotelId = av.getHotel().getHotelId();
                if(hotelId.equals(avHotelId)){
                    hotelAvailability = av;
                    break;
                }
            }
            try {
                List<String> verifyRequests = buildVerifyRequestForHotel(queryId, hotelAvailability);
                model.put("verifyRequests", verifyRequests);
                List<String> policyRequests = buildPolicyRequestForHotel(queryId, hotelAvailability);
                model.put("policyRequests", policyRequests);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //HotelAvailability hotelAvailability = searchResponse.getHotelAvailabilities()[0];
//            System.out.println("Hotel:" + JSONObject.fromObject(hotel));
            hotel = (hotel == null) ? hotelAvailability.getHotel() : hotel;
            model.put("hotel", hotel);
            model.put("availabilities", hotelAvailability.getAvailabilities());
            model.put("hotelFacilities", JSONObject.fromObject(hotel.getFacilities()));
            model.put("hotelPolicies", JSONObject.fromObject(hotel.getPolicies()));

            model.put("request", searchRequest);
            model.put("queryId", queryId);
            String imgUrl = hotel.getPictureId().split(".jpg")[0];
            model.put("imgBaseUrl", Context.getPicBaseUrl() + imgUrl.substring(0, imgUrl.length() - 1));
            model.put("formatter", new CurrencyUtils());
            model.put("duration", 1);
            model.put("noOfRooms", 1);
            model.put("noOfAdults", 1);
            model.put("noOfChild", 1);
            model.put("math", new MathTool());

        }
        return "hotel/hotel";
    }

    @RequestMapping("/bookingconfirm/{hotelId}/{orderId}")
    public String bookingConfirm(@PathVariable("hotelId")String hotelId, @PathVariable("orderId")String orderId, Map<String, Object> model) {
        Order order = apiManager.getHotelOrderDetail(orderId);
        List<OrderRoom> rooms = order.getRooms();
        Map<String, List<Guest>> orderRoomGuests = order.getRoomGuests();
        Map<String, String> roomNameWithGuests = new HashMap<>();
        for(OrderRoom room : rooms){
            String roomIndex = room.getRoomIndex();
            String roomName = room.getRoomName();
            int roomCount = room.getRoomCount();
            List<Guest> guests = orderRoomGuests.get(roomIndex);
            String guestAdult = "";
            String guestChild = "";
            for(Guest guest : guests){
                String guestName = guest.getFullName();
                if(guest.getGuestType().equals(GuestType.ADULT)){
                    if(guestAdult.equals("")){
                        guestAdult = guestName;
                    }
                    else{
                        guestAdult = guestAdult + "," + guestName;
                    }
                }
                else{
                    if(guestChild.equals("")){
                        guestChild = guestName;
                    }
                    else{
                        guestChild = guestChild + "," + guestName;
                    }
                }
            }
            String guestText = guestAdult;
            if(!guestChild.equals("")){
                guestText = guestText + " with kid(s):" + guestChild;
            }
            String roomNameText = roomName + " x " + String.valueOf(roomCount);
            roomNameWithGuests.put(roomNameText, guestText);
        }
        List<OrderPolicy> orderPolicies = order.getPolicies();
        String cancellationPolicy = "This rate is non-refundable and cannot be changed or cancelled - if you do choose to change or cancel this booking you will not be refunded any of the payment.";
        String refundable = "false";
        String changeNamePolicy = "Not allowed";
        String amdendmentPolicy = "Not allowed";

        for(OrderPolicy policy : orderPolicies){
            String policyType = policy.getPolicyType();
            String chargeable = policy.getChargeable();
            String allowable = policy.getAllowable();
            if(policyType.equals("cancellation") && chargeable.equals("false")){
                cancellationPolicy = "Free cancellation can be made before " + policy.getStartDate();
                refundable = "true";
            }
            if(policyType.equals("changename")){
                if(allowable.equals("true")){
                    changeNamePolicy = "Change name is allowed";
                }
            }
            if(policyType.equals("amendment")){
                if(allowable.equals("true")){
                    changeNamePolicy = "Order amendment is allowed";
                }
            }
        }
        String showToolbar = "true";
        String orderStatus = order.getOrderStatus().getDescription();
        System.out.println(orderStatus);
        //for status BOOKING_CONFIRMED, CANCELLATION_FAILED, use default config:
        //showToolbar = true
        if(order.getOrderStatus().equals(OrderStatus.BOOKING_FAILED)){
            showToolbar = "false";
        }
        else if(order.getOrderStatus().equals(OrderStatus.BOOKING_NO_ROOM)){
            showToolbar = "false";
        }
        else if(order.getOrderStatus().equals(OrderStatus.CANCELLATION_CONFIRMED)){
            showToolbar = "false";
            refundable = "false";
        }
        else if(order.getOrderStatus().equals(OrderStatus.ORDER_CLOSED)){
            refundable = "false";
        }

        String totalAmount = order.getOrderPrice().getTotalAmount().toString();
        totalAmount = totalAmount.substring(0, totalAmount.indexOf(".") + 2);
        //JSONObject hotelPolicies = JSONObject.fromObject(order.getHotel().getPolicies());
        Map<String, String> hotelPolicies = new HashMap<>();
        hotelPolicies.put("Cancellation Policy", cancellationPolicy);
        hotelPolicies.put("Change Name Policy", changeNamePolicy);
        hotelPolicies.put("Amendment Policy", amdendmentPolicy);
        model.put("refundable", refundable);
        model.put("showtoolbar", showToolbar);
        model.put("orderstatus", orderStatus);
        model.put("order", order);
        model.put("math", new MathTool());
        model.put("totalamount", totalAmount);
        model.put("roomlist", roomNameWithGuests);
        model.put("hotelpolicies", hotelPolicies);

        CancellationRequest cancellationRequest = new CancellationRequest();
        order.setOrderType(OrderType.CANCELLATION);
        cancellationRequest.setOrder(order);
        model.put("cancellationrequest", JsonUtil.toJson(cancellationRequest));

        return "hotel/bookingconfirm";
    }
}
