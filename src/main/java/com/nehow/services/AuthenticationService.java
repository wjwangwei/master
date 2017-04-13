package com.nehow.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
@Component
public class AuthenticationService {

//    @Autowired
//    public SecurityManager securityManager;

    @Autowired
    public WebserviceManager apiManager;

    public boolean isAuthenticated() {
        return true;
    }

    public void logout() {
//        securityManager.
    }

}
