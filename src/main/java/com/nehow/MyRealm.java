package com.nehow;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * Created by Igbalajobi Jamiu Okunade on 4/12/17.
 */
public class MyRealm extends AuthorizingRealm {

//    UsersHome usersHome = new UsersHome();

//    RoleHome roleHome = new RoleHome();

    protected boolean permissionsLookupEnabled = false;

    public MyRealm() {
        super();
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();

        AuthenticationInfo info = null;
//        Users user = null;// usersHome.getByUserName(username);

//        if(user == null || user.getUserPass() == null){
//            throw new UnknownAccountException("No account found for user [" + username + "]");
//        }
//        info = new SimpleAuthenticationInfo(username, user.getUserPass().toCharArray(), getName());

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

}