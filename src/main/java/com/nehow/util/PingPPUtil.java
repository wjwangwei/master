package com.nehow.util;


import com.pingplusplus.exception.PingppException;
import com.pingplusplus.model.Charge;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjw on 2017/5/1.
 */
public class PingPPUtil {
    private String appId;


    public PingPPUtil(String appId) {
        this.appId = appId;
    }

    public String createCharge(String orderNo, String amount, String subject, String body, String channel, String clientIP) {

        /**
         * 或者直接设置私钥内容
         Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
         "... 私钥内容字符串 ...\n" +
         "-----END RSA PRIVATE KEY-----\n";
         */

        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", amount);
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", subject);
        chargeMap.put("body", body);
        chargeMap.put("order_no", orderNo);
        chargeMap.put("channel", channel);
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15);//15分钟失效
        long timestamp = cal.getTimeInMillis()/ 1000L;
        chargeMap.put("time_expire", timestamp);
        chargeMap.put("client_ip", clientIP); // 客户端 ip 地址(ipv4)
        Map<String, String> extramap = new HashMap<String, String>();
        //extra的参数根据文档: https://pingxx.com/document/api#api-c-new
        String url_success="http://127.0.0.1:9090/pay/paytest_success";
        String url_failure="http://127.0.0.1:9090/pay/paytest_failure";
        switch (channel) {
            case "alipay_wap":
                extramap.put("success_url",url_success);
                extramap.put("cancel_url",url_failure);
                
                break;
            case "alipay_pc_direct":
                extramap.put("success_url",url_success);
                break;
            case "upmp_wap":
                extramap.put("result_url",url_success);
               break;
            case "bfb_wap":
                extramap.put("result_url",url_success);
                extramap.put("bfb_login","true");

                break;
            case "upacp_wap":
                extramap.put("result_url",url_success);
                break;
            case "upacp_pc":
                extramap.put("result_url",url_success);
                break;
            case "wx_pub":
               extramap.put("open_id","Openid");
                break;
            case "wx_pub_qr":
                extramap.put("product_id","Productid");
                break;
            case "yeepay_wap":
                extramap.put("product_category","1");
                extramap.put("identity_id","your identity_id");
                extramap.put("identity_type","1");
                extramap.put("terminal_type","1");
                extramap.put("terminal_id","your terminal_id");
                extramap.put("user_ua","your user_ua");
                extramap.put("result_url",url_success);
                break;
            case "jdpay_wap":
                extramap.put("success_url",url_success);
                extramap.put("fail_url",url_failure);
                extramap.put("token","dsafadsfasdfadsjuyhfnhujkijunhaf");

                break;
        }
        //extramap.put("success_url", "http://127.0.0.1:8080/PartTimeJob/pinus_webview.html");
        //extramap.put("success_url", "http://127.0.0.1:8080/PartTimeJob/pinus_webview.html");
        chargeMap.put("extra", extramap);
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);
        String chargeString = null;
        try {
            //发起交易请求
            Charge charge = Charge.create(chargeMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            chargeString = charge.toString();
        } catch (PingppException e) {
            e.printStackTrace();
        }
        return chargeString;
    }

}