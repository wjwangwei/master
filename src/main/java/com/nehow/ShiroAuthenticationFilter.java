package com.nehow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Component
@Configuration
public class ShiroAuthenticationFilter {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;


    public FilterRegistrationBean filterRegistration() {
        Filter myFilter = shiroFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean();
        beanFactory.autowireBean(myFilter);
        registration.setName("shiroFilter");
        registration.addUrlPatterns("/dummy*");
        registration.setFilter(myFilter);
        Map<String, String> initParam = new HashMap<String, String>();
        initParam.put("targetFilterLifecycle", "true");
        registration.setInitParameters(initParam);
        return registration;
    }

//    @Bean(name = "shiroFilter")
    public Filter shiroFilter() {
        return new DelegatingFilterProxy();
    }

}
