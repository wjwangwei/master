package com.nehow;

import com.nehow.dao.mapper.*;
import com.nehow.dao.entity.*;
import cn.mogutrip.hotel.common.utils.JsonUtil;
import com.nehow.models.LoginStatus;
import com.nehow.models.UserInfo;
import com.nehow.services.Context;
import com.nehow.services.WebserviceManager;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private WebserviceManager apiManager;

    @Autowired
    private UserMapper userDao;
    @Autowired
    private RoleMapper roleDao;
    @Autowired
    private PermissionMapper permDao;
    @Autowired
    private CustomerMapper customerDao;

//    UsersHome usersHome = new UsersHome();

//    RoleHome roleHome = new RoleHome();

    protected boolean permissionsLookupEnabled = false;

    public MyRealm() {
        super();
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String userName = upToken.getUsername();
        String password = new String((char[])upToken.getPassword());
        LoginStatus loginStatus = apiManager.getUserInfo(userName, password);
        String status = loginStatus.getStatus();
        AuthenticationInfo info = null;
        if(status.equals("success")){
            String message = loginStatus.getMessage();
            UserInfo userInfo = JsonUtil.fromJson(message, UserInfo.class);
            Context.setUserInfo(userInfo);
            info = new SimpleAuthenticationInfo(userName, password, getName());
        }

        return info;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        String username = (String) getAvailablePrincipal(principals);
        System.out.println("Auth | username : " + username);


        Set<String> roleNames = null;// roleHome.getNameSetByUserName(username);


        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);
        return info;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

}