package com.nehow.services;


import com.nehow.util.PingPPUtil;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.Charge;
import com.pingplusplus.model.ChargeCollection;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjw on 2017/5/1.
 */
@Component
public class PayService {
    public PayService(){};
    /**
     * Pingpp 管理平台对应的 API Key
     */
    private final static String apiKey = "sk_test_nLaHePT8ajzDvLyDqDKyDSSS";


    /**
     * Pingpp 管理平台对应的应用 ID
     */
    private final static String appId = "app_GSa1GCrPOu5OrnDS";
    /**
     * 你生成的私钥路径
     */
    private final static String privateKeyFilePath = "d:\\project\\rsa_private_key.pem";


    public  String charge(String orderNo,String amount,String subject,String body,String channel,String clientIP){

        // 设置 API Key
        Pingpp.apiKey = apiKey;

        // 设置私钥路径，用于请求签名
        Pingpp.privateKeyPath = privateKeyFilePath;
       /* Pingpp.privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXQIBAAKBgQDdAdBagwIeMvkYpOgzJjFweCjiyGNr5R/N05jJdeZ0BGF8NM56\n" +
                "xytOw6RTwHmk75I/qpioO9hxe+Neo4GhcG/oWbrwbl5RiQRGkcQ2VQXRGqJakwIP\n" +
                "dATLvFxkssR6KMBewfr267kzEEyGO7sJLnlsWIl4N2S+Vn1IF0oLNM20rwIDAQAB\n" +
                "AoGBAL4QNkfwzNx9x71K7Ko4WWI45CiMXvxGMsDHDWmMLGFv9wpjO4NZT8RP+j1Q\n" +
                "lQykP2jjZJ+hv/VwrswP34af9IwLT3nMqU1Dyzl1tVD2StinYQ/OwPImYhTkiaNW\n" +
                "hCOQIxTMOR6YoxFWrn+DdgjkNQIClEwAtrRndBsDkIi4VM2hAkEA99fGR0ctFTF1\n" +
                "rAHeG+UR6gxROOcga4lYl0N9IubHOfQTNCYcz+cypapGhJ7oIl/dz5lcKzOTeJTn\n" +
                "f4bvqMsOsQJBAORH7oSpqKa2JRKoyGzRhLVngYPtBtP0ra+TbMz2/mrhzXCd4jxV\n" +
                "pGvHdjnMcdCMVfuFc+bciaECQsnrKLJxkV8CQAbPljJVQWbosgwlDP6FQAXHit/e\n" +
                "ciSiZJ3bi+/OmcD89R4kwfvwpZKp6EAywbxYGBdhZogFZdMVfHsjsShZVDECQQCC\n" +
                "kppdi2WKJflCmQQ7KgSMdm3gdf1H01ZdbSf4fPa9T/bgiY5UEHCBrY90M/qN3Rd5\n" +
                "TLYkwNSAIfjt4fJfTxJlAkBug7eNehx8oWKkdbLnt0GH3axXCNPj7MwDtxXzaZTd\n" +
                "ioZMZfDyb+fiGRNrzSmJza0VitDRFCbXZNpSJ6YBPtnN\n" +
                "-----END RSA PRIVATE KEY-----\n";*/
        PingPPUtil charge=new PingPPUtil(appId);
        String chargeString=charge.createCharge(orderNo,amount,subject,body,channel,clientIP);
        return chargeString;
    }

public ChargeCollection getChargeCollection(int limit){
        if(limit==0)
        limit=10;
    ChargeCollection chargeCollection = null;
    Pingpp.apiKey = apiKey;
    Map<String, Object> chargeParams = new HashMap<String, Object>();
    chargeParams.put("limit", limit);
    Map<String, String> app = new HashMap<String, String>();
    app.put("id", "app_GSa1GCrPOu5OrnDS");
    chargeParams.put("app", app);
    try {
        chargeCollection = Charge.list(chargeParams);
       // System.out.println(chargeCollection);
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
    return chargeCollection;
}

}