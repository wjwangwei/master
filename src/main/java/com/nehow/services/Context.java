package com.nehow.services;

import cn.mogutrip.hotel.common.entity.ExchangeRate;
import cn.mogutrip.hotel.common.entity.SearchAvailabilityRequest;
import cn.mogutrip.hotel.common.entity.SearchAvailabilityResponse;
import com.nehow.models.UserInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

@Component
public class Context implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Context.applicationContext == null) {
            Context.applicationContext = applicationContext;
        }
    }

    /**
     * get Picture base url from application.properties
     *
     * @return String
     */
    public static String getPicBaseUrl() {
        return applicationContext.getEnvironment().getProperty("webservice.pictureurl");
    }

    public static String getSuppliers() {
        return applicationContext.getEnvironment().getProperty("webservice.suppliers");
    }

    public static <T> T getProperty(String name, Class<T> tClass) {
        return applicationContext.getEnvironment().getProperty(name, tClass);
    }


    public static String getCurrency(){
        return Context.getProperty("application.defaultCurrency");
    }

    public static int getHotelCntPerPage()
    {
        return Context.getProperty("pagination.perpage", Integer.class);
    }

    public static String getCustomerId(){
        UserInfo userInfo = Context.getUserInfo();
        String customerId = userInfo.getCustomerId();
        return customerId;
    }


    public static Map<String, BigDecimal> getMarkup()
    {
        Map<String, BigDecimal> markup = (Map<String, BigDecimal>)Context.getObjectFromSession("MARKUP");
        return markup;
    }

    public static void setMarkup(Map<String, BigDecimal> markup)
    {
        Context.setObjectToSession("MARKUP", markup);

    }

    public static List<ExchangeRate> getExchangeRate()
    {
        List<ExchangeRate> exchangeRates = (List<ExchangeRate>)Context.getObjectFromSession("EXCHANGERATE");
        return exchangeRates;
    }

    public static void setExchangeRate(List<ExchangeRate> exchangeRates)
    {
        SecurityUtils.getSubject().getSession().setAttribute("EXCHANGERATE", exchangeRates);
    }

    public static String getProperty(String name) {
        return applicationContext.getEnvironment().getProperty(name);
    }

    public static String getIpAddr(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Real-IP");
        if(ip != null){
            ip = ip.trim();
        }
        if(ip != null && !ip.equals("") && !"unknown".equalsIgnoreCase(ip)){
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if(ip != null){
            ip = ip.trim();
        }
        if(ip != null && !ip.equals("") && !"unknown".equalsIgnoreCase(ip)){
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0, index);
            }
            else{
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getQueryId(String ip)
    {
        UserInfo userInfo = Context.getUserInfo();
        String userName = userInfo.getUserName();
        String queryId= "";
        try {
            System.currentTimeMillis();
            queryId = userName + ip + System.currentTimeMillis() + Math.random();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(StandardCharsets.UTF_8.encode(queryId));
            queryId = String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (Exception ignored) {
        }
        return queryId;
    }

    public static String getSupplierIds()
    {
        UserInfo userInfo = Context.getUserInfo();
        String suppliers = userInfo.getSuppliers();
        return suppliers;
    }



    public static void setSearchRequest(String queryId, SearchAvailabilityRequest request)
    {
        String key = "SEARCHREQUEST" + queryId;
        Context.setObjectToSession(key, request);

    }
    public static SearchAvailabilityRequest getSearchRequest(String queryId){
        String key = "SEARCHREQUEST" + queryId;

        SearchAvailabilityRequest request = (SearchAvailabilityRequest) Context.getObjectFromSession(key);
        return request;
    }



    public static void setSearchResult(String queryId, SearchAvailabilityResponse result)
    {
        String key = "SEARCHRESULT" + queryId;
        Context.setObjectToSession(key, result);

    }
    public static SearchAvailabilityResponse getSearchResult(String queryId){
        String key = "SEARCHRESULT" + queryId;

        SearchAvailabilityResponse response = (SearchAvailabilityResponse) Context.getObjectFromSession(key);
        return response;
    }

    public static void setHotelAvailability(String queryId, String hotelId, SearchAvailabilityResponse hotelAvaiability)
    {
        String key = "HOTELAVAILABILITY" + queryId + hotelId;
        Context.setObjectToSession(key, hotelAvaiability);
    }

    public static SearchAvailabilityResponse getHotelAvailability(String queryId, String hotelId)
    {
        String key = "HOTELAVAILABILITY" + queryId + hotelId;
        SearchAvailabilityResponse response = (SearchAvailabilityResponse) Context.getObjectFromSession(key);
        return response;
    }

    public static UserInfo getUserInfo()
    {
        UserInfo userInfo = (UserInfo) Context.getObjectFromSession("USERINFO");
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo)
    {
        String key = "USERINFO";
        Context.setObjectToSession(key, userInfo);
    }


    public static Object getObjectFromSession(String key){
        return SecurityUtils.getSubject().getSession().getAttribute(key);
    }

    public static void setObjectToSession(String key, Object o)
    {
        SecurityUtils.getSubject().getSession().setAttribute(key, o);
    }
}
