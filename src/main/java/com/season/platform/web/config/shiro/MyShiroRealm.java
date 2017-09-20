package com.season.platform.web.config.shiro;


import com.season.platform.web.site.dto.User;
import com.season.platform.web.site.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by jiyc on 2017/3/3.
 */
@Component
@Slf4j
public class MyShiroRealm extends AuthorizingRealm {
    @Resource
    private UserService userService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.info("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findByLoginName(token.getUsername());
        if (null == user) {
            throw new UnknownAccountException("user is not exist");// 没找到帐号
        }

        System.out.println("user password = " + user.getPassword());
        System.out.println("token password= " + String.valueOf(token.getPassword()));

        if (!String.valueOf(token.getPassword()).equals(user.getPassword())) {
            throw new AccountException("密码错误");
        }

        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());

    }


    /**
     * 此方法调用  hasRole,hasPermission的时候才会进行回调.
     * 权限信息.(授权):
     * 1、如果用户正常退出，缓存自动清空；
     * 2、如果用户非正常退出，缓存自动清空；
     * 3、如果我们修改了用户的权限，而用户不退出系统，修改的权限无法立即生效。
     * （需要手动编程进行实现；放在service进行调用）
     * 在权限修改后调用realm中的方法，realm已经由spring管理，所以从spring中获取realm实例，
     * 调用clearCached方法；
     * :Authorization 是授权访问控制，用于对用户进行的操作授权，证明该用户是否允许进行当前操作，如访问某个链接，某个资源文件等。
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("权限认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        User authorizingUser = (User) principals.getPrimaryPrincipal();
        if (authorizingUser != null) {
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            // TODO
            return simpleAuthorizationInfo;
        }
        return null;
    }
}
