package com.nehow.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "webservice")
public class WebserviceProperties {
    // root url of web service
    private String rooturl;

    private String suppliers;

    public String getRootUrl() {
        return rooturl;
    }

    public void setRooturl(String rooturl) {
        this.rooturl = rooturl;
    }

    public String getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(String suppliers) {
        this.suppliers = suppliers;
    }
}
