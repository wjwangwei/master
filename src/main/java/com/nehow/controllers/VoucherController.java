package com.nehow.controllers;

import cn.mogutrip.hotel.order.entity.*;
import com.nehow.models.*;
import com.nehow.util.VelocityUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nehow.util.HtmlToPdfUtil.htmlToPdf;

@RestController
public class VoucherController {

    @Value("${voucher.dir}")
    private String voucherDir;
    @Value("${voucher.service}")
    private String voucherService;
    @Value("${voucher.emergencyphone}")
    private String voucherEmergencyPhones;
    @Value("${voucher.logourl}")
    private String logoUrl;

    public String generateHotelVoucherHtml(String orderId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Order order = (Order)restTemplate.getForObject(voucherService + "/service/hotel/order/voucher/" + orderId, Order.class);
        //which reference code should put here?
        String referenceCode = order.getSupplierReferenceCode();
        String hotelName = order.getHotelName();
        String hotelAddress = order.getHotel().getAddress();
        String hotelPhone = order.getHotel().getTelephone();
        String hotelEmail = order.getHotel().getEmail();
        String hotelConfirmationNumber = order.getHotelAffirmCode();
        String checkIn = order.getCheckIn();
        String bookingDay = order.getBookingDate().toString();
        int roomNights = order.getRoomDuration();
        int roomNumber = order.getRoomCount();
        String breakfastDescription = order.getBreakfastDescription();
        String mealDescription = order.getMealDescription();
        String breakfast = order.getBreakfast();

        String mealPolicy = "Room Only";
        String mealDesc = "";

        if(!breakfastDescription.equals("")){
            mealDesc = breakfastDescription;
            mealPolicy = "Breakfast Available";
        }
        else if(!mealDesc.equals("")){
            mealDesc = mealDescription;
            mealPolicy = "Breakfast Available";
        }
        else if(breakfast == "true"){
            mealDesc = "Breakfast is available";
            mealPolicy = "Breakfast Available";
        }
        else{
            mealDesc = "No breakfast is available";
            mealPolicy = "Room Only";
        }

        String remark = order.getRemark();
        int star = order.getHotel().getStarRating();
        String starCssWidth = "";
        if(star == 50){
            starCssWidth = "100px";
        }
        else if(star == 40){
            starCssWidth = "80px";
        }
        else if(star == 30){
            starCssWidth = "60px";
        }
        else if(star == 20){
            starCssWidth = "40px";
        }
        else if(star == 10){
            starCssWidth = "20px";
        }
        else{
            starCssWidth = "";
        }
        String starCss = "";
        if(!starCssWidth.equals("")){
            starCss = "width:" + starCssWidth + "; height:20px; background-image:url(http://hoteleverlink.com/css/images/stars-sm.png);background-position:0 -20px;";
        }
        HotelVoucherParams params = new HotelVoucherParams();
        params.setOrderId(orderId);
        params.setReferenceCode(referenceCode);
        params.setHotelName(hotelName);
        params.setStarCss(starCss);
        params.setHotelAddress(hotelAddress);
        params.setHotelPhone(hotelPhone);
        params.setHotelEmail(hotelEmail);
        params.setHotelConfirmationNumber(hotelConfirmationNumber);
        params.setCheckIn(checkIn);
        params.setBookingDay(bookingDay);
        params.setRoomNights(roomNights);
        params.setRoomNumber(roomNumber);
        params.setMealPolicy(mealPolicy);
        params.setMealDesc(mealDesc);
        //orderRequest.setAddService(addService);
        params.setRemark(remark);

        List<Room> requestRooms = new ArrayList<>();
        List<OrderRoom> rooms = order.getRooms();
        Map<String, List<Guest>> roomGuests = order.getRoomGuests();
        for(OrderRoom room : rooms){
            String roomIndex = room.getRoomIndex();
            String roomName = room.getRoomName();
            Room requestRoom = new Room();
            requestRoom.setRoomIndex(roomIndex);
            requestRoom.setRoomName(roomName);
            List<Guest> guests = roomGuests.get(roomIndex);
            int i = 0;
            List<Passenger> paxList = new ArrayList<>();
            for(Guest guest: guests){
                i++;
                Passenger pax = new Passenger();
                GuestType type = guest.getGuestType();
                if(type.equals(GuestType.ADULT)){
                    pax.setPaxType("adult");
                }
                else{
                    pax.setPaxType("child");
                }
                pax.setPaxIndex(i);
                pax.setPaxName(guest.getFullName());
                paxList.add(pax);
            }
            requestRoom.setPaxList(paxList);
            requestRooms.add(requestRoom);
        }
        params.setRoomList(requestRooms);
        params.setEmergencyPhoneList(voucherEmergencyPhones);
        String template = "tpl/hotelvoucher.vm";
        String voucherHtml = VelocityUtil.buildVoucherFromTemplateFile(template, params);
        return voucherHtml;
    }

    public String generateCustomerVoucherHtml(String orderId) throws Exception {
        CustomerVoucherParams params = new CustomerVoucherParams();
        params.setLogoUrl(logoUrl);
        params.setOrderId(orderId);

        RestTemplate restTemplate = new RestTemplate();
        Order order = (Order)restTemplate.getForObject(voucherService + "/service/hotel/order/voucher/" + orderId, Order.class);

        String referenceCode = order.getSupplierReferenceCode();
        params.setSupplierReferenceCode(referenceCode);

        String hotelEnName = order.getHotel().getHotelName();
        params.setHotelEnName(hotelEnName);

        String hotelCnName = order.getHotel().getHotelCnName();
        params.setHotelCnName(hotelCnName);

        int star = order.getHotel().getStarRating();
        String starCssWidth = "";
        if(star == 50){
            starCssWidth = "100px";
        }
        else if(star == 40){
            starCssWidth = "80px";
        }
        else if(star == 30){
            starCssWidth = "60px";
        }
        else if(star == 20){
            starCssWidth = "40px";
        }
        else if(star == 10){
            starCssWidth = "20px";
        }
        else{
            starCssWidth = "";
        }
        String starCss = "";
        if(!starCssWidth.equals("")){
            starCss = "width:" + starCssWidth + "; height:20px; background-image:url(http://hoteleverlink.com/css/images/stars-sm.png);background-position:0 -20px;";
        }
        params.setStarCss(starCss);

        String hotelAddress = order.getHotel().getAddress();
        params.setHotelAddress(hotelAddress);

        String hotelPhone = order.getHotel().getTelephone();
        params.setHotelPhone(hotelPhone);

        String hotelEmail = order.getHotel().getEmail();
        params.setHotelEmail(hotelEmail);

        String hotelConfirmationNumber = order.getHotelAffirmCode();
        params.setHotelConfirmationNumber(hotelConfirmationNumber);

        String checkIn = order.getCheckIn();
        params.setCheckIn(checkIn);

        String checkOut = order.getCheckOut();
        params.setCheckOut(checkOut);

        String currency = order.getOrderPrice().getCustomerCurrency();
        params.setCurrency(currency);

        String salePrice = order.getOrderPrice().getTotalAmount().toString();
        params.setSalePrice(salePrice);

        String groupNo = order.getRoomGuests().get("1").get(0).getCustomerGroupId();
        params.setGroupNo(groupNo);

        String nationality = order.getGuestCountryName();
        params.setNationality(nationality);

        String cancellationPolicy = "";
        /*
        String amendmentPolicy = "";
        String changeNamePolicy = "";
        */
        List<OrderPolicy> policies = order.getPolicies();
        for(OrderPolicy policy : policies){
            String policyType = policy.getPolicyType();
            if(policyType.equals("cancellation")){
                cancellationPolicy = cancellationPolicy + " " + policy.getDescription();
            }
        }
        params.setCancellationPolicy(cancellationPolicy);

        String userName = order.getOperator();
        params.setUserName(userName);

        String userMobile = order.getOperatorMobile();
        params.setUserMobile(userMobile);

        String userEmail = order.getOperatorEmail();
        params.setUserEmail(userEmail);

        /*
        String reminderMessage = "";
        String importantMessage = "";
        */


        String bookingDay = order.getBookingDate().toString();
        params.setBookingDay(bookingDay);

        int roomNights = order.getRoomDuration();
        params.setRoomNights(roomNights);

        int roomNumber = order.getRoomCount();
        params.setRoomNumber(roomNumber);

        List<Room> roomList = new ArrayList<>();
        List<OrderRoom> rooms = order.getRooms();
        Map<String, List<Guest>> roomGuests = order.getRoomGuests();
        for(OrderRoom room : rooms){
            String roomIndex = room.getRoomIndex();
            String roomName = room.getRoomName();
            Room requestRoom = new Room();
            requestRoom.setRoomIndex(roomIndex);
            requestRoom.setRoomName(roomName);
            List<Guest> guests = roomGuests.get(roomIndex);
            int i = 0;
            List<Passenger> paxList = new ArrayList<>();
            for(Guest guest: guests){
                i++;
                Passenger pax = new Passenger();
                GuestType type = guest.getGuestType();
                if(type.equals(GuestType.ADULT)){
                    pax.setPaxType("adult");
                }
                else{
                    pax.setPaxType("child");
                }
                pax.setPaxIndex(i);
                pax.setPaxName(guest.getFullName());
                paxList.add(pax);
            }
            requestRoom.setPaxList(paxList);
            roomList.add(requestRoom);
        }
        params.setRoomList(roomList);

        params.setEmergencyPhoneList(voucherEmergencyPhones);

        String breakfastDescription = order.getBreakfastDescription();
        String mealDescription = order.getMealDescription();
        String breakfast = order.getBreakfast();
        String mealPolicy = "Room Only";
        String mealDesc = "";

        if(!breakfastDescription.equals("")){
            mealDesc = breakfastDescription;
            mealPolicy = "Breakfast Available";
        }
        else if(!mealDesc.equals("")){
            mealDesc = mealDescription;
            mealPolicy = "Breakfast Available";
        }
        else if(breakfast == "true"){
            mealDesc = "Breakfast is available";
            mealPolicy = "Breakfast Available";
        }
        else{
            mealDesc = "No breakfast is available";
            mealPolicy = "Room Only";
        }
        params.setMealPolicy(mealPolicy);
        params.setMealDesc(mealDesc);

        //String addService = "";
        String specialRequest = order.getRemark();
        params.setSpecialRequest(specialRequest);


        String template = "tpl/customervoucher.vm";
        String voucherHtml = VelocityUtil.buildVoucherFromTemplateFile(template, params);
        return voucherHtml;
    }


    public boolean generatePdf(String voucherName, String voucherHtml)
    {
        File rootFolder = new File(voucherDir);
        if(!rootFolder.exists()){
            rootFolder.mkdirs();
        };
        String pdfFileName = voucherName + ".pdf";
        String htmlFilePath = voucherDir + "/" + voucherName + ".html";
        String pdfFile = voucherDir + "/" + pdfFileName;

        File file = new File(htmlFilePath);
        if(file.exists()){
            file.delete();
        }
        try{
            file.createNewFile();
            FileOutputStream fileout = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fileout,"utf-8");
            writer.write(voucherHtml.replaceAll("&","\u0026amp;"));
            writer.close();
            fileout.close();
            System.out.println("html file path:" + htmlFilePath);
            htmlToPdf(htmlFilePath, voucherDir, pdfFileName);
            return true;
        }
        catch (Exception io){
            System.out.println("create html file failed:" + htmlFilePath);
            io.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "/service/hotel/order/confirmvoucher/download/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadCustomerVoucher(@PathVariable("orderID") String orderId, HttpServletResponse response) throws IOException {
        String pdfFileName = "customervoucher_" + orderId + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", pdfFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        File pdfFile = new File(voucherDir + "/" + pdfFileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(pdfFile),
                headers, HttpStatus.OK);    }

    @RequestMapping(value = "/service/hotel/order/voucher/download/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadHotelVoucher(@PathVariable("orderID") String orderId, HttpServletResponse response) throws IOException {
        String pdfFileName = "hotelvoucher_" + orderId + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", pdfFileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        File pdfFile = new File(voucherDir + "/" + pdfFileName);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(pdfFile),
                headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/service/hotel/order/confirmvoucher/export/{orderID}", method = RequestMethod.GET)
    public Voucher exportCustomerVoucherPdf(@PathVariable("orderID") String orderId, HttpServletRequest request) throws Exception {
        String voucherHtml = generateCustomerVoucherHtml(orderId);
        String voucherName = "customervoucher_" + orderId;
        generatePdf(voucherName, voucherHtml);
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        url = url.replace(uri, "");
        url = url + "/service/hotel/order/confirmvoucher/download/" + orderId;
        return new Voucher(orderId, url);
    }


    @RequestMapping(value = "/service/hotel/order/voucher/export/{orderID}", method = RequestMethod.GET)
    public Voucher exportHotelVoucherPdf(@PathVariable("orderID") String orderId, HttpServletRequest request) throws Exception {
        String voucherHtml = generateHotelVoucherHtml(orderId);
        String voucherName = "hotelvoucher_" + orderId;
        generatePdf(voucherName, voucherHtml);
        String uri = request.getRequestURI();
        String url = request.getRequestURL().toString();
        url = url.replace(uri, "");
        url = url + "/service/hotel/order/voucher/download/" + orderId;
        return new Voucher(orderId, url);
    }
}
