package com.nehow.controllers;

import com.nehow.services.PayService;
import com.nehow.services.WebhooksVerifyService;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import com.pingplusplus.model.Refund;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sjw on 2017/5/2.
 */
@Controller
public class PayController {
    @Autowired
    private PayService payService;
    @RequestMapping("/pay/paytest")
    public void testpay(
           // @RequestParam("amount") String amount,
            HttpServletRequest request,HttpServletResponse response,
            Map<String, Object> model) {
        //String amount="1";

        String amount=request.getParameter("amount");
        String channel=request.getParameter("channel");
        String chargeString=payService.charge("order126", amount,"subject","body",channel,"127.0.0.1");
        //model.put("chargeString", chargeString);
        //return "test-pay_result";

        try {
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setContentType("application/json");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out= null;
            out = response.getWriter();
            JSONObject chargeJson = new JSONObject(chargeString);
            //System.out.println(chargeJson);
           // out.print(chargeJson);
            out.print(chargeString);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @RequestMapping("/pay/paytest_search")
    public String testpay_search(
            HttpServletRequest request,
            Map<String, Object> model) {
        ChargeCollection chargeCollection=payService.getChargeCollection(10);
        List<Charge> chargeList=chargeCollection.getData();
        System.out.print(chargeList.size());
        model.put("chargeList", chargeList);

        return "test-pay_search";
    }

    @RequestMapping("/pay/paytest_success")
    public String testpay_success(
            // @RequestParam("amount") String amount,
            HttpServletRequest request,HttpServletResponse response,
            Map<String, Object> model) {
        model.put("payresult", "success");
        return "test-pay_result";
    }
    @RequestMapping("/pay/paytest_failure")
    public String testpay_failure(
            // @RequestParam("amount") String amount,
            HttpServletRequest request,HttpServletResponse response,
            Map<String, Object> model) {
        model.put("payresult", "failure");
        return "test-pay_result";
    }

    @RequestMapping("/refund/refundtest")
    public void testrefund(
            // @RequestParam("amount") String amount,
            HttpServletRequest request,HttpServletResponse response,
            Map<String, Object> model) {
        String amount=request.getParameter("amount");
        String chid=request.getParameter("chid");
        Charge ch = null;//ch_id 是已付款的订单号
        Refund re=null;
        try {
            ch = Charge.retrieve(chid);
            Map<String, Object> refundMap = new HashMap<String, Object>();
            refundMap.put("amount", amount);
            refundMap.put("description", "Refund Description");
            re = ch.getRefunds().create(refundMap);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (RateLimitException e) {
            e.printStackTrace();
        }


        try {
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setContentType("application/json");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out= null;
            out = response.getWriter();

            out.print(re);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("webhooks")
    @ResponseBody
    public void webhooks ( HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException  {
       /*System.out.println("ping++　webhooks");*/
        request.setCharacterEncoding("UTF8");
        //获取头部所有信息
        Enumeration headerNames = request.getHeaderNames();
        String signature=null;
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if("x-pingplusplus-signature".equals(key)){
                signature=value;
            }
        }
       /*System.out.println("signature"+signature);*/
        // 获得 http body 内容
        StringBuffer eventJson=new StringBuffer();
        BufferedReader reader= null;
        try {
            reader = request.getReader();
            do{
                eventJson.append(reader.readLine());
            }while(reader.read()!=-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader.close();
        JSONObject event=new JSONObject(eventJson.toString());
        boolean verifyRS=false;
        try {
            PublicKey publicKey= WebhooksVerifyService.getPubKey();
           System.out.println(publicKey);
            verifyRS= WebhooksVerifyService.verifyData(eventJson.toString(),signature,publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // verifyRS=true;
        if(verifyRS) {
           /*System.out.println("签名验证成功");*/
            if ("charge.succeeded".equals(event.get("type"))) {
                JSONObject data = new JSONObject(event.get("data").toString());
                JSONObject object = new JSONObject(data.get("object").toString());
                String orderId = (String) object.get("order_no");
               /*System.out.println("orderId:"+orderId);*/
                String channel = (String) object.get("channel");
                String payType = null;
                int amountFen = (int) object.get("amount");
                Double amountYuan = amountFen * 1.0 / 100;//ping++扣款,精确到分，而数据库精确到元
                Double weiXinInput = null;
                Double aliPayInput = null;
                Double bankCardInput = null;

                if ("wx".equals(channel)) {
                    payType = "4";//支付类型(1:储值卡，2:现金,3:银行卡,4:微信,5:支付宝,6:优惠券，7：打白条;8:多方式付款;9:微信个人，10：支付宝（个人）)
                    weiXinInput = amountYuan;
                } else if ("alipay".equals(channel)) {
                    payType = "5";
                    aliPayInput = amountYuan;
                } else if ("upacp".equals(channel) || "upacp_wap".equals(channel) || "upacp_pc".equals(channel)) {
                    payType = "3";
                    bankCardInput = amountYuan;
                }
                Double couponInput;

                //这里写酒店订单数据库信息
                Object order = "hotel order object";

                if (order != null) {
                   //Double orderPrice = order.getShouldPrice();//应付金额
                    //couponInput = orderPrice - amountYuan;//订单金额-ping++扣款 等于优惠金额
                    //Boolean bool = reserveAppVenueConsService.saveSettlement(order, payType, amountYuan,0.0, bankCardInput, weiXinInput, aliPayInput, couponInput);
                    Boolean bool=true;
                    if (bool) {
                       System.out.println("订单结算成功");
                        response.setStatus(200);
                        //return "订单结算成功";
                    } else {
                       System.out.println("订单结算失败");
                        //return "订单结算失败";
                        response.setStatus(500);
                    }
                } else {
                   System.out.println("该订单不存在");
                    //return "该订单不存在";
                    response.setStatus(500);
                }
            }
        }else{
           /*System.out.println("签名验证失败");*/
            //return "签名验证失败";
            response.setStatus(500);
        }
    }
}
