package io.qhzhou.nztrip.interceptor;

import io.qhzhou.nztrip.annotation.AuthenticationPass;
import io.qhzhou.nztrip.dto.Token;
import io.qhzhou.nztrip.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qianhao.zhou on 8/9/16.
 */
@Service("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            AuthenticationPass authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthenticationPass.class);

            //没有声明需要权限,或者声明不验证权限
            if (authPassport == null || authPassport.validate() == false) {
                return true;
            } else {
                if (needAuthentication(httpServletRequest)) {
                    httpServletResponse.sendRedirect("/signin");
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    private String getToken(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("X-TOKEN")) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private boolean needAuthentication(HttpServletRequest request) {
        String tokenString = getToken(request);
        if (tokenString == null) {
            return true;
        }
        try {
            Token token = tokenService.parseToken(tokenString);
            if (token.isExpired()) {
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException e) {
            return true;
        }
    }
}
