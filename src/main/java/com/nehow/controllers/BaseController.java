package com.nehow.controllers;

import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

public class BaseController implements ServletContextAware {
    // servlet context to store global data
    ServletContext context;

    // key for nationality suggestion
    protected final String kNationality = "nationality";

    // key for destination suggestion
    protected final String kDestination = "destination";

    // key for hotel results
    protected final String kHotel = "hotel";

    protected final String kMarkup = "markup";
    protected final String kExchange = "exchange";

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }
}
